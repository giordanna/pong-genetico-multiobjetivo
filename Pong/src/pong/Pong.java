package pong;

// copia do pong.java onde está sendo executado testes
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.Timer;
import pong.Jogador.*;
import pong.Jogador.IJogador;
import static pong.Pong.Status.*;
import pong.Outros.Configuracao;
import pong.Outros.Renderizador;

public class Pong implements ActionListener, KeyListener {
    
    public Configuracao conf = new Configuracao();
    
    public static Timer timer;

    public JFrame jframe;
    public Renderizador renderizador;
    
    public static Pong pong;
    
    // dimensões da tela
    public int largura = Configuracao.LARGURA_TELA, altura = Configuracao.ALTURA_TELA;

    public IJogador jogador_esquerda, jogador_direita; // os que vão ser usados de fato
    
    public IJogador instancias_esquerda[]; // humano, ai basico, ai perfeito, ai genético, treinador
    public IJogador instancias_direita[]; // humano, ai basico, ai perfeito, ai genético, treinador

    //public Bola bola;
    
    public Bola [] bolas;

    public boolean seleciona_jogador_esquerda = true;
    
    // botões pros humanos usarem
    public boolean w, s, cima, baixo;

    // enum com Menu, Pausado, Jogando (não existe limite)
    public Status status_jogo = Menu;
    
    // humano = 0, ai basico = 1, ai perfeito = 2, ai genético = 3, treinador = 4
    public int opcao_jogador_esquerda = 0, opcao_jogador_direita = 0;
    
    public int partida = 0;
    
    public static int velocidade = 3;

    public Pong() throws IOException {
        instancias_esquerda = new IJogador[6];
        instancias_esquerda[0] = new Humano(1);
        instancias_esquerda[1] = new AIBasico(1);
        instancias_esquerda[2] = new AIFisico(1);
        instancias_esquerda[3] = new AIGenetico(Genotipo.genotipoAleatorio(0, 0), 1);
        instancias_esquerda[4] = null;
        instancias_esquerda[5] = null;
        
        
        instancias_direita = new IJogador[6];
        instancias_direita[0] = new Humano(2);
        instancias_direita[1] = new AIBasico(2);
        instancias_direita[2] = new AIFisico(2);
        instancias_direita[3] = new AIGenetico(Genotipo.genotipoAleatorio(0, 0), 2);
        instancias_direita[4] = null;
        instancias_direita[5] = null;
        
        // tempo de atualização dos frames. mexer nisso aqui
        timer = new Timer(20, this);

        jframe = new JFrame("Pong Genético");

        renderizador = new Renderizador();
        
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderizador);
        jframe.addKeyListener(this);
        jframe.setContentPane(renderizador);
        jframe.pack();
        jframe.setVisible(true);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jframe.setLocation(dim.width/2-jframe.getSize().width/2, dim.height/2-jframe.getSize().height/2);

        timer.start();
    }
    
    public enum Status {
        Menu, Pausado, Jogando;
    }

    public void inicializarPong() {
        status_jogo = Jogando;
        
        //bola = new Bola(pong);
        bolas = new Bola [Configuracao.QUANTIDADE_BOLAS];
        for (int i = 0 ; i < Configuracao.QUANTIDADE_BOLAS ; i++)
            bolas[i] = new Bola(pong);
        bolas[0].setEspecial();

    }
    
    // escreve o texto centralizado
    private void escreveTexto(Graphics2D g, String texto, int x, int y){
        int comprimento = (int)
            g.getFontMetrics().getStringBounds(texto, g).getWidth();
        int inicio = largura/2 - comprimento/2;
        g.drawString(texto, inicio + x, y);
    }
    
    public void modoMenu(Graphics2D g){
        
        String[] string = {"Humano", "AI Basico", "AI Fisico", "AI Genetico", "AI Treinador", "AI Gen. de Arquivo"};
        if (status_jogo == Menu) {
            g.setColor(Color.WHITE);
            g.setFont(Configuracao.FONTE_TIPO.deriveFont(Font.BOLD,50));
            escreveTexto(g, "PONG GENETICO", 0, 60);

            if (seleciona_jogador_esquerda) {
                
                g.setFont(Configuracao.FONTE_TIPO.deriveFont(Font.BOLD,20));
                escreveTexto(g, "<< Jogador 1: " + string[opcao_jogador_esquerda] + " >>" , 0, altura / 2 - 25);
                 
                g.setFont(Configuracao.FONTE_TIPO.deriveFont(Font.PLAIN,20));
                escreveTexto(g, "Jogador 2: " + string[opcao_jogador_direita] , 0, altura / 2 + 25);
                escreveTexto(g, "Pressione Space para jogar" , 0, altura / 2 + 100);
            }
            
            if (!seleciona_jogador_esquerda) {

                g.setFont(Configuracao.FONTE_TIPO.deriveFont(Font.PLAIN,20));
                escreveTexto(g, "Jogador 1: " + string[opcao_jogador_esquerda], 0, altura / 2 - 25);
                escreveTexto(g, "Pressione Space para jogar" , 0, altura / 2 + 100);

                g.setFont(Configuracao.FONTE_TIPO.deriveFont(Font.BOLD,20));
                escreveTexto(g, "<< Jogador 2: " + string[opcao_jogador_direita] + " >>" , 0, altura / 2 + 25);
            }
        }
    }
    
    public void modoPausado(Graphics2D g){
        if (status_jogo == Pausado) {
            g.setColor(Color.WHITE);
            g.setFont(Configuracao.FONTE_TIPO.deriveFont(1,50));
            escreveTexto(g, "PAUSA", 0, altura / 2);
        }
    }
    
    public void modoJogando(Graphics2D g){
        // porque também tem que mostrar o fundo quando o jogo tá pausado
        if (status_jogo == Pausado || status_jogo == Jogando) {
            g.setColor(Color.WHITE);

            g.setStroke(new BasicStroke(5f));

            // faz a linha do meio
            g.drawLine(largura / 2, 0, largura / 2, altura);

            g.setStroke(new BasicStroke(5f));

            // faz o círculo do meio
            g.drawOval(largura / 2 - 150, altura / 2 - 150, 300, 300);

            g.setFont(Configuracao.FONTE_TIPO.deriveFont(1,40));
            
            // faz a pontuação
            escreveTexto(g, String.valueOf(jogador_esquerda.getRaquete().getScore()), -150, 50);
            escreveTexto(g, String.valueOf(jogador_direita.getRaquete().getScore()), 150, 50);
            //g.drawString(String.valueOf(jogador_esquerda.getRaquete().getScore()), largura / 2 - 90, 50);
            //g.drawString(String.valueOf(jogador_direita.getRaquete().getScore()), largura / 2 + 65, 50);
            
            // informa qual o indivíduo atual e a geração caso o jogador da esquerda seja treinador
            if (opcao_jogador_esquerda == 4){
                g.setFont(Configuracao.FONTE_TIPO.deriveFont(1,20));
                g.drawString("Atual:" + ((Treinador) jogador_esquerda).getAtual(), 50, 30);
                
                g.setFont(Configuracao.FONTE_TIPO.deriveFont(1,15));
                g.drawString("Geracao:" + ((Treinador) jogador_esquerda).getGeracao(), 50, altura - 15);
            }
            
            // informa qual o indivíduo atual e a geração caso o jogador da direita seja treinador
            if (opcao_jogador_direita == 4){
                g.setFont(Configuracao.FONTE_TIPO.deriveFont(1,20));
                g.drawString("Atual:" + ((Treinador) jogador_direita).getAtual(), 3*largura/ 4, 30);
                
                g.setFont(Configuracao.FONTE_TIPO.deriveFont(1,15));
                g.drawString("Geracao:" + ((Treinador) jogador_direita).getGeracao(), 3*largura/ 4, altura - 15);
            }

            g.setFont(Configuracao.FONTE_TIPO.deriveFont(1,15));
            escreveTexto(g, "Velocidade:" + getVelocidade(), 0, altura - 40);
            
            // aqui que acontece a mágica das raquetes e da bola
            for (int i = 0 ; i < Configuracao.QUANTIDADE_BOLAS ; i++)
                bolas[i].renderizarBola(g);
            
            jogador_esquerda.getRaquete().renderizarRaquete(g);
            jogador_direita.getRaquete().renderizarRaquete(g);
        }
    }

    public void renderizarPong(Graphics2D g) {
        g.setColor(new Color(49,41,96));
        g.fillRect(0, 0, largura, altura);
        
        // formas e letras não ficarão serrilhadas
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        modoMenu(g);
        modoJogando(g);
        modoPausado(g);
    }
    
    public void atualizaInstancias() throws IOException{
        
        boolean flag = false;
        
        if (opcao_jogador_direita == 5){ // carregar Genótipo do arquivo
            if (instancias_direita[opcao_jogador_direita] == null){
                double genotipo [] = new double [Configuracao.TAMANHO_CROMOSSOMO];
                
                File f = new File("./arquivos/melhordireita.txt");
                if(f.exists() && !f.isDirectory()) {
                    int i = 0;
                    try (Scanner scan = new Scanner(f)) {
                        while (scan.hasNextLine()){
                            genotipo[i] = scan.nextDouble();
                            i++;
                        }
                    }
                    instancias_direita[opcao_jogador_direita] = new AIGenetico(new Genotipo(genotipo), 2);
                }
                else{
                    // deu erro: arquivo não existe
                    flag = true;
                }
                
            }
        }
        
        if (opcao_jogador_esquerda == 5){ // carregar Genótipo do arquivo
            if (instancias_esquerda[opcao_jogador_esquerda] == null){
                double genotipo [] = new double [Configuracao.TAMANHO_CROMOSSOMO];
                
                File f = new File("./arquivos/melhoresquerda.txt");
                if(f.exists() && !f.isDirectory()) {
                    int i = 0;
                    try (Scanner scan = new Scanner(f)) {
                        while (scan.hasNextLine()){
                            genotipo[i] = scan.nextDouble();
                            i++;
                        }
                    }
                    instancias_esquerda[opcao_jogador_esquerda] = new AIGenetico(new Genotipo(genotipo), 1);
                }
                else{
                    // deu erro: arquivo não existe
                    flag = true;
                }
                
            }
        }
        
        if (opcao_jogador_esquerda == 4) // AI Treinador
            if (instancias_esquerda[opcao_jogador_esquerda] == null){
                instancias_esquerda[opcao_jogador_esquerda] = new Treinador(1);
            }
        
        if (opcao_jogador_direita == 4) // AI Treinador
            if (instancias_direita[opcao_jogador_direita] == null){
                instancias_direita[opcao_jogador_direita] = new Treinador(2);
            }
        
        if (opcao_jogador_esquerda == 3){ // AI genético
            if (instancias_esquerda[4] != null)
                instancias_esquerda[opcao_jogador_esquerda] = new AIGenetico(((Treinador) instancias_esquerda[4]).melhorGenotipo(), 1);
        }
        
        if (opcao_jogador_direita == 3){ // AI genético
            if (instancias_direita[4] != null)
                instancias_direita[opcao_jogador_direita] = new AIGenetico(((Treinador) instancias_direita[4]).melhorGenotipo(), 2);
        }
        
        jogador_esquerda = instancias_esquerda[opcao_jogador_esquerda];
        jogador_direita = instancias_direita[opcao_jogador_direita];
        
        status_jogo = Jogando;
        if (flag) status_jogo = Menu; // se ocorreu algum erro no carregamento do arquivo
    }
    
    // to do: mexer nisso aqui
    public void atualizarPong() {
        
        int pontos_esquerda = 0;
        int pontos_direita = 0;

        // humano esquerda
        if (opcao_jogador_esquerda == 0){
            if (w) {
                jogador_esquerda.getRaquete().mover(true);
            }
            if (s) {
                jogador_esquerda.getRaquete().mover(false);
            }
        }
        else{ // então é AI
            jogador_esquerda.getRaquete().mover(jogador_esquerda.verificaDirecao(
                    jogador_esquerda.getRaquete(), jogador_direita.getRaquete(), bolas));
        }
        
        // humano direita
        if (opcao_jogador_direita == 0){
            if (cima) {
                jogador_direita.getRaquete().mover(true);
            }
            if (baixo) {
                jogador_direita.getRaquete().mover(false);
            }
        }
        else{ // então é AI
            jogador_direita.getRaquete().mover(jogador_direita.verificaDirecao(
                    jogador_direita.getRaquete(), jogador_esquerda.getRaquete(), bolas));
        }
        int ponto;
        for (Bola x: bolas){
            ponto = x.atualizarBola(jogador_esquerda.getRaquete(), jogador_direita.getRaquete());
            if (ponto > 0)
                pontos_esquerda += ponto;
            else if (ponto < 0)
                pontos_direita += -ponto;
        }
        
        if (pontos_esquerda != 0 || pontos_direita != 0){
            
            jogador_esquerda.resultado(pontos_esquerda, pontos_direita); // só tem efeito mesmo se for treinador
            jogador_direita.resultado(pontos_direita, pontos_esquerda); // só tem efeito mesmo se for treinador
            
            partida += pontos_esquerda + pontos_direita;
            
            if (jogador_esquerda instanceof Treinador || jogador_direita instanceof Treinador){
                if (partida >= Configuracao.MAX_PONTUACAO){
                    jogador_esquerda.getRaquete().resetScore();
                    jogador_direita.getRaquete().resetScore();
                    partida = 0;
                }
            }
        }
        
        if (jogador_esquerda instanceof Treinador)
            if (((Treinador) jogador_esquerda).getGeracao() > Configuracao.MAX_GERACOES){
                status_jogo = Menu;
                velocidade = 3;
                mudarVelocidade();
            }
        if (jogador_direita instanceof Treinador)
            if (((Treinador) jogador_direita).getGeracao() > Configuracao.MAX_GERACOES){
                status_jogo = Menu;
                velocidade = 3;
                mudarVelocidade();
            }
    }

    // onde realiza o update dos frames
    @Override
    public void actionPerformed(ActionEvent e) {
        if (status_jogo == Jogando) {
            atualizarPong();
        }

        renderizador.repaint();
    }
 
    public static void main(String[] args) throws IOException {
        pong = new Pong();
    }
    
    public static void mudarVelocidade(){
        switch (velocidade){
            case 1:{
                timer.stop();
                timer = new Timer(0,pong);
                timer.start();
                break;
            }
            case 2:{
                timer.stop();
                timer = new Timer(10,pong);
                timer.start();
                break;
            }
            case 3:{
                timer.stop();
                timer = new Timer(20,pong);
                timer.start();
                break;
            }
        }
    }
    
    public String getVelocidade(){
        switch (velocidade){
            case 1:{
                return "Super Rapido";
            }
            case 2:{
                return "Rapido";
            }
            case 3:{
                return "Normal";
            }
        }
        return "Normal";
    }

    // o que fazer quando pressionar teclas
    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = true;
        } else if (id == KeyEvent.VK_S) {
            s = true;
        } else if (id == KeyEvent.VK_UP) { // quando pressionar seta pra cima
            cima = true;
            if (status_jogo == Menu) {
                if (!seleciona_jogador_esquerda) seleciona_jogador_esquerda = true;
            }
        } else if (id == KeyEvent.VK_DOWN) { // quando pressionar seta pra baixo
            baixo = true;
            if (status_jogo == Menu) {
                if (seleciona_jogador_esquerda) seleciona_jogador_esquerda = false;
            }
        } else if (id == KeyEvent.VK_RIGHT) { // quando pressionar seta pra direita
            if (status_jogo == Menu){
                if (seleciona_jogador_esquerda) {
                    if (opcao_jogador_esquerda < 5) {
                        opcao_jogador_esquerda++;
                    } else {
                        opcao_jogador_esquerda = 0;
                    }
                }
                else{
                    if (opcao_jogador_direita < 5) {
                        opcao_jogador_direita++;
                    } else {
                        opcao_jogador_direita = 0;
                    }
                }
            }
        } else if (id == KeyEvent.VK_LEFT) { // quando pressionar seta pra esquerda
            if (status_jogo == Menu){
                if (seleciona_jogador_esquerda) {
                    if (opcao_jogador_esquerda > 0) {
                        opcao_jogador_esquerda--;
                    } else {
                        opcao_jogador_esquerda = 5;
                    }
                }
                else{
                    if (opcao_jogador_direita > 0) {
                        opcao_jogador_direita--;
                    } else {
                        opcao_jogador_direita = 5;
                    }
                }
            }
            // se apertou Esc no jogo ou quando tava pausado, volta pro menu
        } else if (id == KeyEvent.VK_ESCAPE && ((status_jogo == Jogando) || (status_jogo == Pausado))) {
            status_jogo = Menu;
            velocidade = 3;
            mudarVelocidade();
        } else if (id == KeyEvent.VK_SPACE) {
            if (status_jogo == Menu){
                
                try {
                    // instâncias escolhidas
                    atualizaInstancias();
                } catch (IOException ex) {
                    Logger.getLogger(Pong.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (status_jogo == Jogando){
                    partida = 0;
                    inicializarPong();
                }
            } else if (status_jogo == Pausado) {
                status_jogo = Jogando;
            } else if (status_jogo == Jogando) {
                status_jogo = Pausado;
            }
        } else if (id >= 49 && id <= 51){ // 49 = tecla 1 e 51 = tecla 3
            if (status_jogo == Jogando){
                velocidade = 52 - id;
                mudarVelocidade();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getKeyCode();

        switch (id) {
            case KeyEvent.VK_W:
                w = false;
                break;
            case KeyEvent.VK_S:
                s = false;
                break;
            case KeyEvent.VK_UP:
                cima = false;
                break;
            case KeyEvent.VK_DOWN:
                baixo = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}