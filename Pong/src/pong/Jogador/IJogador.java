package pong.Jogador;

import pong.Bola;
import pong.Raquete;

public interface IJogador {

    // verifica a direção que a raquete deve se mover
    public int verificaDirecao(Raquete minha, Raquete oponente, Bola bola);
    
    public void resultado(int ponto_meu, int ponto_oponente);

    public int verificaDirecao(Raquete minha, Raquete oponente, Bola[] bolas);
    
    public Raquete getRaquete();
}
