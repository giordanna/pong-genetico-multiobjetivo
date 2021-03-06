package pong.Jogador;

// AQUI onde treina 30 genótipos
// to do: fazer receber jogadores, e não genótipos

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import pong.Bola;
import pong.NSGAII.NSGAII;
import pong.Outros.Configuracao;
import pong.Raquete;

public class Treinador implements IJogador {
    private static int MAIOR_FITNESS = 1000;
    private static int qtd_treinador = 0;
    private int treinador;
    private Genotipo populacao[];
    private Genotipo oldpop[];
    private double intervalo;
    private boolean bola_passou;
    private int contraatacou;
    private int atual;
    private int ultima_distancia;
    private int posicao_inicial = Integer.MAX_VALUE;
    private int pontos_jogador = 0, pontos_adversario = 0, total = 0;
    private int geracao = 1;
    private Writer output_fitness, output_genotipo, output_placar;
    private Raquete raquete;
    private NSGAII nsga;

    public Treinador() throws IOException{

        qtd_treinador++;
        treinador = qtd_treinador;
        
        intervalo = Configuracao.INTERVALO_GENES;
        bola_passou = false;
        contraatacou = 0;
        atual = 0;
        
        
        
        File diretorio = new File("arquivos");
        if (!diretorio.exists()) {
            System.out.println("criando repositório pros arquivos");
            diretorio.mkdir();
        }

        nsga = new NSGAII(treinador);
        
        output_fitness = new BufferedWriter(new FileWriter("./arquivos/fitness" 
                + treinador + ".csv"));
        output_fitness.write("");
        output_fitness.close();
        
        output_genotipo = new BufferedWriter(new FileWriter("./arquivos/melhorgenotipo" 
                + treinador + ".csv"));
        output_genotipo.write("");
        output_genotipo.close();
        
        output_placar = new BufferedWriter(new FileWriter("./arquivos/placar" +
                treinador + ".csv"));
        output_placar.write("");
        output_placar.close();
        
        inicializaPopulacao();
    }
    
    public Treinador(int numero) throws IOException{
        this();
        raquete = new Raquete(numero);
        raquete.setAltura(populacao[0].getTamanhoRaquete());
    }
    
    @Override
    public Raquete getRaquete() { return raquete; }
    
    public void inicializaPopulacao(){
        populacao = new Genotipo[Configuracao.MAX_POPULACAO];
        oldpop = new Genotipo[Configuracao.MAX_POPULACAO];
        
        for (int i = 0 ; i < Configuracao.MAX_POPULACAO ; i++){
            populacao[i] = Genotipo.genotipoAleatorio(-intervalo, intervalo);
        }
        
        
    }
    
    public int retornaInicio(Raquete minha){
        if (posicao_inicial == Integer.MAX_VALUE)
            posicao_inicial = minha.getY();
        return posicao_inicial - minha.getY();
    }
    
    public int distanciaPercorrida(Raquete minha, Bola bola){
        int distancia = 0;
        if ( (minha.getX() >= (Configuracao.ALTURA_TELA / 2) && bola.getX() > minha.getX() + 1 ) ||
                (minha.getX() < Configuracao.ALTURA_TELA / 2 && bola.getX() < minha.getX() - 1) ){
            bola_passou = true;
            if (bola.getY() > minha.getY())
                distancia = bola.getY() - (minha.getY() + minha.getAltura());
            else
                distancia = minha.getY() - (bola.getY() + bola.getAltura());
        }
        return distancia;
    }
    
    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola){
        if (bola.getMovimentoX() == 0 && bola.getMovimentoY() == 0)
            return retornaInicio(minha);
        
        if (!bola_passou)
            ultima_distancia = distanciaPercorrida(minha, bola);
        
        return populacao[atual].validaVelocidade(minha, oponente, bola);
    }
    
    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola[] bolas) {
        int min_indice = 0;
        int min_distancia = Integer.MAX_VALUE, distancia;
        
        for (int i = 0 ; i < bolas.length ; i++) {
            distancia = minha.distancia(bolas[i]);
            if (distancia < min_distancia){
                min_indice = i;
                min_distancia = distancia;
            }
        }
        
        return verificaDirecao(minha,oponente, bolas[min_indice]);
    }
    
    // cálculo do fitness
    @Override
    public void resultado(int ponto_meu, int ponto_oponente){
        
        int fitness;
        
        contraatacou = raquete.getBolasRebatidas() - populacao[atual].getBolasRebatidas();
        
        populacao[atual].setBolasRebatidas(raquete.getBolasRebatidas());
        populacao[atual].setEspeciaisRebatidas(raquete.getEspeciaisRebatidas());
        populacao[atual].setEspeciaisTotal(raquete.getEspeciaisTotal());
        
        total += ponto_meu + ponto_oponente;
        
        fitness = MAIOR_FITNESS * ponto_meu; // se marcou um ponto então ele é bom mesmo
        pontos_jogador += ponto_meu;
        
        pontos_adversario += ponto_oponente;
        
        fitness += contraatacou;
        fitness += Configuracao.ALTURA_TELA - 3 * ultima_distancia;
        bola_passou = false;
        
        if (populacao[atual].getFitness() != 0){
            // faz uma média com o fitness antigo
            populacao[atual].setFitness(populacao[atual].getFitness() + fitness);
            populacao[atual].setFitness(populacao[atual].getFitness() / 2);
        }
        else{
            populacao[atual].setFitness(fitness);
        }
        
        if (total >= Configuracao.MAX_PONTUACAO){
            
            try {
                salvaPopulacao();
            } catch (IOException ex) {
                Logger.getLogger(Treinador.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            raquete.setBolasRebatidas(0);
            raquete.setEspeciaisRebatidas(0);
            raquete.setEspeciaisTotal(0);
            raquete.setAltura(populacao[atual].getTamanhoRaquete());
            
            pontos_jogador = 0;
            pontos_adversario = 0;
            total = 0;
        }
    }
    
    public String placar(){
        return pontos_jogador + " - " + pontos_adversario;
    }
    
    // muito feio mas fazer o que
    public int getTotalPlacar(){
        return total;
    }
    
    public int getAtual(){
        return atual + 1;
    }
    
    public int getGeracao(){
        return geracao;
    }
    
    public Genotipo melhorGenotipo(){
        Genotipo melhor = new Genotipo(Genotipo.genotipoAleatorio(0, 0));
        
        for (int i = 0 ; i < Configuracao.MAX_POPULACAO ; i++){
            if ( populacao[i].getFitness() > melhor.getFitness() ){
                melhor = new Genotipo(populacao[i]);
            }
        }
        
        return melhor;
    }
    
    public void salvaPopulacao() throws IOException{
        // salva num(s) arquivo(s)
        output_fitness = new BufferedWriter(new FileWriter("./arquivos/fitness"
                + treinador + ".csv", true));
        output_fitness.append( (populacao[atual].getFitness() + "; ").replace(".",",") );   
        
        output_placar = new BufferedWriter(new FileWriter("./arquivos/placar" +
                treinador + ".csv", true));
        output_placar.append(" " + placar() + "; ");
        atual++; 
        
        if (atual == Configuracao.MAX_POPULACAO){
            output_genotipo = new BufferedWriter(new FileWriter("./arquivos/melhorgenotipo"
                    + treinador + ".csv", true));
            
            // mais bonito que a forma anterior
            for (double x: melhorGenotipo().getGenes()){
                output_genotipo.append(String.valueOf(x).replace(".",","));
                output_genotipo.append("; ");
            }
            output_genotipo.append("\n");
            output_genotipo.close();
            
            output_fitness.append("\n");
            output_placar.append("\n");
            
            repopularPopulacao();
            atual = 0;
            geracao++;
        }
        output_fitness.close();
        output_placar.close();
        
    }
    
    public void geraFilhos(){
        int i;
        // método utilizado no início
        
        //torna os piores 3/4 nulos
        for (i = populacao.length/4 ; i < populacao.length ; i++){
            populacao[i] = null;
        }
        
        int j = 0;
        int outro;
        // preenche metade com os melhores + um genótipo aleatório da mesma geração
        for (i = populacao.length/4 ; i < 3*populacao.length/4 ; i++){
            while (true){
                outro = Configuracao.R.nextInt(populacao.length/4);
                if (j != outro) break;
            }
            populacao[i] = new Genotipo(Genotipo.crossover(populacao[j], populacao[outro]));
            j++;
        }
        
        // adiciona alguns poucos genótipos com mutação
        for (i = 3*populacao.length/4 ; i < populacao.length ; i++){
            outro = Configuracao.R.nextInt(3*populacao.length/4-1);
            populacao[i] = new Genotipo(Genotipo.mutacao(populacao[outro]));
        }
        
        // começa fresh af
        for (Genotipo x: populacao){
            x.setBolasRebatidas(0);
            x.setEspeciaisRebatidas(0);
        }
    }
    
    public void repopularPopulacao() throws IOException{
        
        int i;
        
        if (geracao == 1){
            for (i = 0 ; i < populacao.length ; i++)
                oldpop[i] = new Genotipo (populacao[i]);
            Arrays.sort(populacao);
        }
        else{
            ArrayList<Genotipo> populacaoNSGA = new ArrayList<>();
            for (Genotipo x: oldpop)
                populacaoNSGA.add(new Genotipo(x));
            
            ArrayList<Genotipo> filhosNSGA = new ArrayList<>();
            for (Genotipo x: populacao)
                filhosNSGA.add(new Genotipo(x));
            
            // salva oldpop antes do nsga
            for (i = 0 ; i < populacao.length ; i++)
                oldpop[i] = new Genotipo(populacao[i]);

            nsga.NSGAII(populacaoNSGA, filhosNSGA, populacao.length, geracao);
            
            // a nova
            for (i = 0 ; i < populacao.length ; i ++){
                populacao[i] = new Genotipo(populacaoNSGA.get(i));
            }
        }
        
        geraFilhos();
    }
}