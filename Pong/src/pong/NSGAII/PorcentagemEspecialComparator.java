package pong.NSGAII;

import java.util.Comparator;
import pong.Jogador.Genotipo;

public class PorcentagemEspecialComparator implements Comparator<Genotipo> {

    @Override
    public int compare(Genotipo c1, Genotipo c2) {
        if (c1.getPorcentagemEspecial() > c2.getPorcentagemEspecial()) {
            return -1;
        } else if (c1.getPorcentagemEspecial() < c2.getPorcentagemEspecial()) {
            return 1;
        }
        return 0;

    }

}
