package pong.Jogador;

// utiliza UM genótipo (que é o melhor escolhido da classe treinador)

import pong.Bola;
import pong.Outros.Configuracao;
import pong.Raquete;


public class AIGenetico implements IJogador {
    
    private Genotipo genotipo;
    private int posicao_inicial = Integer.MAX_VALUE;
    private Raquete raquete;
    
    public AIGenetico(Genotipo genotipo, int numero){
        this.genotipo = genotipo;
        raquete = new Raquete(numero);
        raquete.setAltura(genotipo.getTamanhoRaquete());
    }
    
    @Override
    public Raquete getRaquete() { return raquete; }

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
        int min_indice = 0;
        int min_distancia = Integer.MAX_VALUE, distancia;
        
        // probabilidade de ir atrás da bola que dá mais pontos
        if ( Configuracao.R.nextDouble(true,true) < genotipo.getProbabilidadeEspecial())
            return verificaDirecao(minha,oponente, bolas[0]);

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
    public void resultado(int ponto_meu, int ponto_oponente) {}
}
