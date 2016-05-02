package pong;

import java.awt.Color;
import java.awt.Graphics;
import pong.Outros.Configuracao;

public class Raquete {

    private final int numero_raquete, largura = Configuracao.RAQUETE_LARGURA;
    private int altura = Configuracao.RAQUETE_ALTURA;
    private int x, y;
    
    private int bolas_rebatidas = 0;
    private int especiais_rebatidas = 0;
    private int especiais_total = 0;

    private int score;

    public Raquete(int numero_raquete) {
        this.numero_raquete = numero_raquete;

        if (numero_raquete == 1) {
            this.x = 0;
        }

        if (numero_raquete == 2) {
            this.x = Configuracao.LARGURA_TELA - largura;
        }

        this.y = Configuracao.ALTURA_TELA / 2 - this.altura / 2;
        
        bolas_rebatidas = 0;
        especiais_rebatidas = 0;
        especiais_total = 0;
    }
    
    public int getBolasRebatidas() { return bolas_rebatidas; }
    public int getEspeciaisRebatidas() { return especiais_rebatidas; }
    public int getEspeciaisTotal() { return especiais_total; }
    
    public void setBolasRebatidas( int num ){
        bolas_rebatidas = num;
    }
    
    public void setEspeciaisRebatidas( int num ){
        especiais_rebatidas = num;
    }
    
    public void setEspeciaisTotal( int num ){
        especiais_total = num;
    }
    
    public void adicionaBolasRebatidas( int num ){
        bolas_rebatidas += num;
    }
    
    public void adicionaEspeciaisRebatidas( int num ){
        especiais_rebatidas += num;
    }
    
    public void adicionaEspeciaisTotal ( int num ){
        especiais_total += num;
    }
    
    public int getLargura() { return largura; }
    
    public int getAltura() { return altura; }
    
    public int getNumeroRaquete() { return numero_raquete; }
    
    public int getX() { return x; }
    
    public int getY() { return y; }
    
    public int getScore() { return score; }
    
    public void setAltura(int altura){
        this.altura = altura;
    }
    
    public void atualizaScore(int pontos){
        score += pontos;
    }

    public void resetScore(){
        score = 0;
    }
    
    public void renderizarRaquete(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, largura, altura);
    }

    // implementação antiga, mais voltada pro jogador humano
    public void mover(boolean cima) {
        
        int velocidade = Configuracao.VELOCIDADE_RAQUETE_PADRAO;
        
        velocidade = converterAlturaVelocidade(velocidade);

        if (cima){
            if (y - velocidade > 0) {
                y -= velocidade;
            } else {
                y = 0;
            }
        } else if (y + altura + velocidade < Configuracao.ALTURA_TELA) {
            y += velocidade;
        } else {
            y = Configuracao.ALTURA_TELA - altura;
        }
    }
    
    public int determinaLimite(int valor, int min, int max) {
        return Math.min(Math.max(valor, min), max);
    }
    
    public int distancia(Bola bola){
        int dist;
        
        //esquerda
        if (numero_raquete == 1){
            dist = Math.abs(bola.getX() - this.x);
        }
        else{ // direita
            dist = Math.abs(this.x - bola.getX());
        }
        
        return dist;
    }
    
    // TESTAR
    public void mover(int velocidade) {
        
        this.y = determinaLimite(y, 0, Configuracao.ALTURA_TELA - this.altura);
        velocidade = determinaLimite(velocidade, -Configuracao.VELOCIDADE_RAQUETE_PADRAO, Configuracao.VELOCIDADE_RAQUETE_PADRAO);

        velocidade = converterAlturaVelocidade(velocidade);
        
        if (velocidade < 0) {
            if (y + velocidade > 0) {
                y += velocidade;
            } else {
                y = 0;
            }
        } else if (y + altura + velocidade < Configuracao.ALTURA_TELA) {
            y += velocidade;
        } else {
            y = Configuracao.ALTURA_TELA - altura;
        }
    }
    
    public int converterAlturaVelocidade( int velocidade ) {
        double valor = Configuracao.RAQUETE_ALTURA;
        return (int) (velocidade * (valor/altura) );
    }

}
