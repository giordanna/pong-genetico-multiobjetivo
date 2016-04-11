package pong.Jogador;

// utiliza UM genótipo (que é o melhor escolhido da classe treinador)

import pong.Bola;
import pong.Raquete;


public class AIGenetico implements IJogador {
    
    private Genotipo genotipo;
    private int posicao_inicial = Integer.MAX_VALUE;
    
    public AIGenetico( Genotipo genotipo ){
        this.genotipo = genotipo;
    }

    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola) {
        // se ainda não foi iniciada
        if (posicao_inicial == Integer.MAX_VALUE)
            posicao_inicial = minha.getY();
        
        if (bola.getMovimentoX() == 0 && bola.getMovimentoY() == 0)
            return posicao_inicial - minha.getY();
        
        return genotipo.validaVelocidade(minha, oponente, bola);
    }
    
    @Override
     public int verificaDirecao(Raquete minha, Raquete oponente, Bola[] bolas) {
        int vel = 0;
        
        for (Bola bola : bolas) {
            vel += verificaDirecao(minha, oponente, bola);
        }
        
        vel /= bolas.length;
        return vel;
    }

    @Override
    public void resultado(int ponto) {}
    
    @Override
    public void resultado(int ponto_meu, int ponto_oponente) {}
}
