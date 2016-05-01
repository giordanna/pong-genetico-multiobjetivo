package pong.NSGAII;

import java.util.Comparator;
import pong.Jogador.Genotipo;

public class CrowdingDistanceComparator implements Comparator<Genotipo> {

    @Override
    public int compare(Genotipo c1, Genotipo c2) {
        if (c1.getDistancia() > c2.getDistancia()) {
            return -1;
        } else if (c1.getDistancia() < c2.getDistancia()) {
            return 1;
        }
        return 0;
    }
}
