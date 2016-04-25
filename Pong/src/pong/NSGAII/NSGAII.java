package pong.NSGAII;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import pong.Jogador.Genotipo;

public class NSGAII {
    private ArrayList<Frente> frentes;
    private Writer output_frentes;
    private int numero;
    
    public NSGAII(int numero) throws IOException{
        frentes = new ArrayList<>();
        this.numero = numero;
        
        output_frentes = new BufferedWriter(new FileWriter("./arquivos/frentes" 
                + numero + ".csv"));
        output_frentes.write("");
        output_frentes.close();
    }
    
    public void NSGAII( ArrayList<Genotipo> pais, ArrayList<Genotipo> filhos, int N, int geracao) throws IOException{
        
        // zera as frentes
        frentes.clear();
        frentes.add(new Frente());
        
        Genotipo [] reuniao = new Genotipo [N * 2];
            
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
        for (Frente x: frentes)
            x.sort();
        
        salvaFrentes(geracao);
        
        pais.clear();

        int j = 0;
        while ( (j < frentes.size()) && (pais.size() + frentes.get(j).getIndividuos().size() <= N)){
            pais.addAll(frentes.get(j).getIndividuos());
            j++;
        }

        if (pais.size() < N){
            if (j < frentes.size()){
                int falta = N - pais.size();

                for (i = 0 ; i < falta ; i++){
                    pais.add(frentes.get(j).get(i));
                }
            }
        }

    }
    
    // RESOLVER PROBLEMA AQUI
    public void fastNonDominatedSort(Genotipo [] genotipos){
        for (Genotipo x: genotipos){
            x.setDominantes(0);
            x.getDominados().clear();
            
            for (Genotipo y: genotipos){
                if (!x.equals(y)){
                    
                    if (x.domina(y))
                        x.getDominados().add(y);
                    if (y.domina(x))
                        x.setDominantes(x.getDominantes() + 1);
                }
            }
            if (x.getDominantes() == 0){
                frentes.get(0).add(x);
            }
            
        }
        
        int k = 0;
        while (!frentes.get(k).getIndividuos().isEmpty()){
            
            ArrayList<Genotipo> lista = new ArrayList<>();
            
            for (Genotipo x: frentes.get(k).getIndividuos()){
                
                for (Genotipo y: x.getDominados()){
                    
                    y.setDominantes(y.getDominantes() - 1);
                    if (y.getDominantes() == 0){
                        lista.add(y);
                    }
                    
                }
            }
            
            k++;
            if (frentes.size() - 1 < k ) frentes.add(new Frente());
            frentes.get(k).addAll(lista);
            
        }
        
        if (frentes.get(k).getIndividuos().isEmpty()) frentes.remove(k);
    }
    
    public void salvaFrentes(int geracao) throws IOException{
        
        // salva num(s) arquivo(s)
        output_frentes = new BufferedWriter(new FileWriter("./arquivos/frentes"
                + numero + ".csv", true));
        
        output_frentes.append("Geracao:" + geracao + "\n");
        for (int i = 0 ; i < frentes.size() ; i++){
            
            output_frentes.append("Frente:" + (i + 1) + "\n");
            
            for (int j = 0 ; j < frentes.get(i).getIndividuos().size() ; j++){
                
                for (double x: frentes.get(i).get(j).getGenes()){
                    output_frentes.append(String.valueOf(x).replace(".",","));
                    output_frentes.append("; ");
                }
                output_frentes.append("\n");
                
            }
            
            output_frentes.append("\n");
        }
        
        output_frentes.append("\n");
        
        output_frentes.close();
    }
}
