package pong.Jogador;

import pong.Outros.Configuracao;
import java.util.Arrays;
import pong.Bola;
import pong.Raquete;

public class Genotipo implements Comparable<Genotipo>{
    
    private double gene[];
    private double fitness = 0;
    
    public Genotipo(){
        gene = new double[Configuracao.TAMANHO_CROMOSSOMO];
        fitness = 0;
        gene[4] = Configuracao.RAQUETE_ALTURA;
        
    }
    
    public Genotipo( double [] genes ){
        
        gene = new double[genes.length];
        System.arraycopy(genes, 0, gene, 0, genes.length);
        fitness = 0;
        
        if (gene[4] > Configuracao.MAX_ALTURA_RAQUETE )
            gene[4] = Configuracao.MAX_ALTURA_RAQUETE;
        else if (gene[4] < Configuracao.MIN_ALTURA_RAQUETE )
            gene[4] = Configuracao.MIN_ALTURA_RAQUETE;
    }
    
    public Genotipo( Genotipo copia ){
        
        gene = new double[copia.gene.length];
        System.arraycopy(copia.gene, 0, gene, 0, copia.gene.length);
        this.fitness = copia.fitness;
    }
    
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
}
