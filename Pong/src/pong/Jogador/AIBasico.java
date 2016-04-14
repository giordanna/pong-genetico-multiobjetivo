package pong.Jogador;

// verifica apenas a posição Y da bola

import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;

public class AIBasico implements IJogador {
    
    private Raquete raquete;
    
    public AIBasico() {}
    
    public AIBasico(int numero){
        raquete = new Raquete(numero);
    }
    
    public Raquete getRaquete() { return raquete; }

    @Override
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola) {
        return Configuracao.MAX_VELOCIDADE_RAQUETE * 
                ((bola.getY() - minha.getY() - minha.getAltura() / 2) < 0 ? -1 : 1);
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

    @Override
    public void resultado(int ponto) {}

    @Override
    public void resultado(int ponto_meu, int ponto_oponente) {}
    
}

