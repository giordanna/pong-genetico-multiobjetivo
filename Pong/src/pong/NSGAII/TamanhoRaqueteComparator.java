package pong.NSGAII;

import java.util.Comparator;
import pong.Jogador.Genotipo;

public class TamanhoRaqueteComparator implements Comparator<Genotipo> {

    @Override
    public int compare(Genotipo c1, Genotipo c2) {
        if( c1.getTamanhoRaquete() > c2.getTamanhoRaquete() )
            return -1;
        else if( c1.getTamanhoRaquete() < c2.getTamanhoRaquete() )
            return 1;
        return 0;

    }

}
