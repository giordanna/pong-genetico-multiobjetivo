package pong.NSGAII;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import pong.Jogador.Genotipo;

public class Frente {
    private ArrayList<Genotipo> individuos;
    
    public Frente (){
        individuos = new ArrayList<>();
    }
    
    public ArrayList<Genotipo> getIndividuos(){ return individuos; }

    public void add(Genotipo novo){
        individuos.add(novo);
    }
    
    public void remove( int index ){
        individuos.remove(index);
    }
    
    public void remove( Genotipo gen ){
        individuos.remove(gen);
    }
    
    public Genotipo get( int index ){
        return individuos.get(index);
    }

    public void sort(){
        
        if (individuos.size() > 1) {
            
            individuos.get(0).setDistancia(Double.POSITIVE_INFINITY);
            individuos.get(individuos.size() - 1).setDistancia(Double.POSITIVE_INFINITY);
            
            Collections.sort(individuos, new TamanhoRaqueteComparator());

            for (int j = 1; j < individuos.size() - 1; j++) {
                individuos.get(j).setDistancia(individuos.get(j).getDistancia() +
                        individuos.get(j + 1).getTamanhoRaquete() - individuos.get(j - 1).getTamanhoRaquete());
            }
            
            Collections.sort(individuos, new ProbabilidadeComparator());
            
            for (int j = 1; j < individuos.size() - 1; j++) {
                individuos.get(j).setDistancia(individuos.get(j).getDistancia() +
                        individuos.get(j + 1).getProbabilidadeEspecial() - individuos.get(j - 1).getProbabilidadeEspecial());
            }

        } else {
            individuos.get(0).setDistancia(Double.POSITIVE_INFINITY);
        }
    }

    public void addAll(ArrayList<Genotipo> pais) {
        individuos.addAll(pais);
    }
        
}
