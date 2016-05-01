package pong.NSGAII;

import java.util.Comparator;
import pong.Jogador.Genotipo;

public class FactoryComparator {

    public static Comparator<Genotipo> getComparator(int objetivo) {
        switch (objetivo) {
            case 0:
                return new BolasRebatidasComparator();
            case 1:
                return new PorcentagemEspecialComparator();
            default:
                return null;
        }
    }
}
