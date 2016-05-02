package pong.NSGAII;

import java.util.Comparator;
import pong.Jogador.Genotipo;

public class BolasRebatidasComparator implements Comparator<Genotipo> {

    @Override
    public int compare(Genotipo c1, Genotipo c2) {
        if (c1.getBolasRebatidas() > c2.getBolasRebatidas()) {
            return -1;
        } else if (c1.getBolasRebatidas() < c2.getBolasRebatidas()) {
            return 1;
        }
        return 0;

    }

}
