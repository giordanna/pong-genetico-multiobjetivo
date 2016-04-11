package pong.Jogador;

// verifica apenas a posição Y da bola

import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;

public class AIBasico implements IJogador {

    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola) {
        return Configuracao.MAX_VELOCIDADE_RAQUETE * 
                ((bola.getY() - minha.getY() - minha.getAltura() / 2) < 0 ? -1 : 1);
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

