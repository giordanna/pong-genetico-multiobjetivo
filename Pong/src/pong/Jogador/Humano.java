package pong.Jogador;

import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;

public class Humano implements IJogador {
    
    private Raquete raquete;
    
    public Humano(){}
    
    public Humano(int numero){
        raquete = new Raquete(numero);
    }
    
    @Override
    public Raquete getRaquete() { return raquete; }

    // não será usado de fato
    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola) {
        return Configuracao.VELOCIDADE_RAQUETE_PADRAO;
    }
    
    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola[] bolas){
        return Configuracao.VELOCIDADE_RAQUETE_PADRAO;
    }

    @Override
    public void resultado(int ponto) {}
    
    @Override
    public void resultado(int ponto_meu, int ponto_oponente) {}
  
}
