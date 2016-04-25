package pong.Jogador;

import java.util.ArrayList;
import pong.Outros.Configuracao;
import java.util.Arrays;
import pong.Bola;
import pong.Raquete;

public class Genotipo implements Comparable<Genotipo>{
    
    private double gene[];
    private double fitness = 0;
    
    private ArrayList<Genotipo> dominados; // indivíduos que são dominados por este genotipo
    private int dominantes = 0; // número de indivíduos que domina este genotipo
    private double distancia;
    
    
    public Genotipo(){
        gene = new double[Configuracao.TAMANHO_CROMOSSOMO];
        fitness = 0;
        gene[4] = Configuracao.RAQUETE_ALTURA;
        
        dominados = new ArrayList<>();
        dominantes = 0;
        distancia = Integer.MAX_VALUE;
        
    }
    
    public Genotipo( double [] genes ){
        
        gene = new double[genes.length];
        System.arraycopy(genes, 0, gene, 0, genes.length);
        fitness = 0;
        
        if (gene[4] > Configuracao.MAX_ALTURA_RAQUETE )
            gene[4] = Configuracao.MAX_ALTURA_RAQUETE;
        else if (gene[4] < Configuracao.MIN_ALTURA_RAQUETE )
            gene[4] = Configuracao.MIN_ALTURA_RAQUETE;
        
        dominados = new ArrayList<>();
        dominantes = 0;
        
        distancia = Integer.MAX_VALUE;
    }
    
    public Genotipo( Genotipo copia ){
        
        gene = new double[copia.gene.length];
        System.arraycopy(copia.gene, 0, gene, 0, copia.gene.length);
        this.fitness = copia.fitness;
        
        this.dominados = new ArrayList<>(copia.dominados);
        this.dominantes = copia.dominantes;
        
        distancia = copia.distancia;

    }
    
    public double getDistancia() { return distancia; }
    
    public void setDistancia( int distancia ){
        this.distancia = distancia;
    }
    
    public int getDominantes() { return dominantes; }
    
    public void setDominantes (int dominantes) {
        this.dominantes = dominantes;
    }
    
    public ArrayList<Genotipo> getDominados() { return dominados; }
    
    public double getFitness() { return fitness; }
    
    public void setFitness( double fitness ){
        this.fitness = fitness;
    }
    
    public int getTamanhoRaquete() { return (int) gene[4]; }

    public double getProbabilidadeEspecial() { return gene[3]; }
    
    public double [] getGenes() { return gene; }
    
    // retorna velocidade da raquete
    public int validaVelocidade(Raquete jogador, Raquete oponente, Bola bola){
        int velocidade = (int) (gene[0] * bola.getMovimentoY() + gene[1] * bola.getY() + 
                gene[2] * jogador.getY());
        
        return velocidade;
    }
    
    // retorna um genótipo aleatório
    public static Genotipo genotipoAleatorio(double menor, double maior){
        Genotipo novo = new Genotipo();
        for (int i = 0 ; i < 3 ; i++){
            novo.gene[i] = menor + (maior - menor) * Configuracao.R.nextDouble(true,true);
        }
        
        // se dá prioridade pra bola especial
        novo.gene[3] = Configuracao.R.nextDouble(true,true);
        
        // tamanho da raquete
        novo.gene[4] = tamanhoAleatorio(
                Configuracao.MIN_ALTURA_RAQUETE,
                Configuracao.MAX_ALTURA_RAQUETE
        );
        
        return novo;
    }
    
    public static int tamanhoAleatorio(int menor, int maior){
        return Configuracao.R.nextInt((maior - menor) + 1) + menor;
    }
    
    // retorna filho produzido por dois pais através de crossover por média aritmética
    public static Genotipo crossover(Genotipo a, Genotipo b){
        Genotipo novo = new Genotipo();
        for (int i = 0 ; i < Configuracao.TAMANHO_CROMOSSOMO ; i++){
            novo.gene[i] = (a.gene[i] + b.gene[i]) / 2.0;
        }
        
        return novo;
    }
    
    // mutação do genótipo. para tentar escapar da solução subótima local
    public static Genotipo mutacao(Genotipo a){
        Genotipo g = new Genotipo();
        
        for (int i = 0 ; i < 4 ; i++){
                g.gene[i] = a.gene[i] + ( -0.1 + 0.2 * Configuracao.R.nextDouble());
        }
        
        if (g.gene[3] > 1) g.gene[3] = 1;
        else if (g.gene[3] < 0) g.gene[3] = 0;
        
        g.gene[4] = a.gene[4] + tamanhoAleatorio(-10, 10);
        
        if (g.gene[4] > Configuracao.MAX_ALTURA_RAQUETE )
            g.gene[4] = Configuracao.MAX_ALTURA_RAQUETE;
        else if (g.gene[4] < Configuracao.MIN_ALTURA_RAQUETE )
            g.gene[4] = Configuracao.MIN_ALTURA_RAQUETE;
        
        return g;
    }
    
    @Override
    public String toString(){
        return Arrays.toString(gene) + ":" + fitness;
    }

    @Override
    public int compareTo(Genotipo outro) {
        if (this.fitness == outro.fitness)
            return 0;
        return this.fitness < outro.fitness ? 1 : -1;
    }
    
    public boolean domina( Genotipo outro ){
        // raquete menor
        if (this.gene[4] <= outro.gene[4]){
            // prioridade na bola colorida
            return this.gene[3] <= outro.gene[3];
        }
        return false;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof Genotipo){
            Genotipo outro = (Genotipo) o;
            for (int i = 3 ; i < this.gene.length ; i++)
                if (this.gene[i] != outro.gene[i]) return false;
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Arrays.hashCode(this.gene);
        return hash;
    }
    
    public void calculaDistancia(Genotipo outro){
        distancia = Math.sqrt(
                Math.pow(outro.getTamanhoRaquete() - this.getTamanhoRaquete(), 2) +
                Math.pow(outro.getProbabilidadeEspecial() - this.getProbabilidadeEspecial(), 2)
        );

    }
}
