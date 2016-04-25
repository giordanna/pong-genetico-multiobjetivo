package pong.NSGAII;

import java.util.Comparator;
import pong.Jogador.Genotipo;

public class ProbabilidadeComparator implements Comparator<Genotipo> {

    @Override
    public int compare(Genotipo c1, Genotipo c2) {
        if( c1.getProbabilidadeEspecial() > c2.getProbabilidadeEspecial() )
            return -1;
        else if( c1.getProbabilidadeEspecial() < c2.getProbabilidadeEspecial() )
            return 1;
        return 0;

    }

}
