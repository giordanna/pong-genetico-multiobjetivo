package pong.NSGAII;

import java.util.ArrayList;
import pong.Jogador.Genotipo;

public class NSGAII {
    ArrayList<Frente> frentes;
    
    public NSGAII(){
        frentes = new ArrayList<>();
        
        frentes.add(new Frente());
    }
    
    public void NSGAII( ArrayList<Genotipo> pais, ArrayList<Genotipo> filhos, int N){
        
        Genotipo [] reuniao = new Genotipo [N * 2];
        int geracao = 0;
        while (true){
            
            int i = 0;
            for (Genotipo x: pais){
                reuniao[i] = new Genotipo(x);
                i++;
            }
            
            for (Genotipo x: filhos){
                reuniao[i] = new Genotipo(x);
                i++;
            }
            
            fastNonDominatedSort(reuniao);
            pais.clear();
            
            
            int j = 1;
            while (pais.size() + frentes.get(j).getIndividuos().size() <= N){
                pais.addAll(frentes.get(j).getIndividuos());
                j++;
            }
            
            frentes.get(j).sort();
            
            int falta = N - pais.size();
            
            for (i = 0 ; i < falta ; i++){
                pais.add(frentes.get(j).get(i));
            }

            // torneio
            // seleção, cruzamento e mutação
            geracao++;
        }
    }
    
    public void fastNonDominatedSort(Genotipo [] genotipos){
        for (Genotipo x: genotipos){
            x.setDominantes(0);
            x.getDominados().clear();
            
            for (Genotipo y: genotipos){
                if (x.dominates(y))
                    x.getDominados().add(y);
                else
                    x.setDominantes(x.getDominantes() + 1);
            }
            
            if (x.getDominantes() == 0){
                frentes.get(0).add(x);
            }
            
        }
        
        int k = 0;
        while (!frentes.get(k).getIndividuos().isEmpty()){
            ArrayList<Genotipo> Q = new ArrayList<>();
            for (Genotipo x: frentes.get(k).getIndividuos()){
                for (Genotipo y: x.getDominados()){
                    y.setDominantes(y.getDominantes() - 1);
                    if (y.getDominantes() == 0){
                        Q.add(y);
                    }
                }
            }
            k++;
            if (frentes.get(k) == null) frentes.add(new Frente());
            frentes.get(k).addAll(Q);
        }
    }

}
