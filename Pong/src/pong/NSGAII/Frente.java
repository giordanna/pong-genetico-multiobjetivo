package pong.NSGAII;

import java.util.ArrayList;
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

        Genotipo aux;
        
        // ordena por lergura da raquete
        for (int i = individuos.size() - 1 ; i >= 1 ; i--){
            for (int j = 0; j < i ; j++){
                
                if (individuos.get(j).getTamanhoRaquete() > individuos.get(j+1).getTamanhoRaquete()){
                    aux = individuos.get(j);
                    individuos.set(j, individuos.get(j+1));
                    individuos.set(j+1, aux);
                }
            }
        }
        
        // mede distancia
        crowdingDistance();
        
        // ordena por distância
        for (int i = individuos.size() - 1 ; i >= 1 ; i--){
            for (int j = 0; j < i ; j++){
                
                if (individuos.get(j).getDistancia() > individuos.get(j+1).getDistancia()){
                    aux = individuos.get(j);
                    individuos.set(j, individuos.get(j+1));
                    individuos.set(j+1, aux);
                }
            }
        }
    }
    
    public void crowdingDistance(){
        individuos.get(0).setDistancia(Integer.MAX_VALUE);
        individuos.get(individuos.size() - 1).setDistancia(Integer.MAX_VALUE);
        
        for (int i = 1 ; i < individuos.size() - 1 ; i ++){
            individuos.get(i).calculaDistancia(individuos.get(i+1));
        }
    }

    public void addAll(ArrayList<Genotipo> pais) {
        individuos.addAll(pais);
    }
        
}
