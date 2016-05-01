package pong.NSGAII;

import java.util.ArrayList;
import java.util.Collections;
import pong.Jogador.Genotipo;
import pong.Outros.Configuracao;

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

    public void crowdingDistance(){
        
        if (individuos.size() > 1) {
            
            for (int i = 0 ; i < Configuracao.NUM_OBJETIVOS ; i++){
                individuos.get(0).setDistancia(Double.POSITIVE_INFINITY);
                individuos.get(individuos.size() - 1).setDistancia(Double.POSITIVE_INFINITY);
                
                Collections.sort(individuos, FactoryComparator.getComparator(i));
                
                for (int j = 1; j < individuos.size() - 1; j++) {
                    individuos.get(j).setDistancia(individuos.get(j).getDistancia() +
                            individuos.get(j + 1).getObjetivo(i) - individuos.get(j - 1).getObjetivo(i));
                }
            }

        } else {
            individuos.get(0).setDistancia(Double.POSITIVE_INFINITY);
        }
    }

    public void addAll(ArrayList<Genotipo> pais) {
        individuos.addAll(pais);
    }
        
}
