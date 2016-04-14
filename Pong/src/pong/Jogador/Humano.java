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
    
    public Raquete getRaquete() { return raquete; }

    // não será usado de fato
    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola) {
        return Configuracao.MAX_VELOCIDADE_RAQUETE;
    }
    
    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola[] bolas){
        return Configuracao.MAX_VELOCIDADE_RAQUETE;
    }

    @Override
    public void resultado(int ponto) {}
    
    @Override
    public void resultado(int ponto_meu, int ponto_oponente) {}
  
}
