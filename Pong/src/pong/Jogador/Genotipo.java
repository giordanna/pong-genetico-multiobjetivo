package pong.Jogador;

import pong.Outros.Configuracao;
import java.util.Arrays;
import pong.Bola;
import pong.Raquete;

public class Genotipo implements Comparable<Genotipo>{
    
    private double gene[];
    private double fitness = 0;
    private double especial = 0;
    private int tamanho_raquete = Configuracao.RAQUETE_ALTURA;
    
    public Genotipo(){
        gene = new double[Configuracao.TAMANHO_CROMOSSOMO];
        especial = 0;
        fitness = 0;
        tamanho_raquete = Configuracao.RAQUETE_ALTURA;
        
    }
    
    public Genotipo( double [] genes ){
        
        gene = new double[genes.length];
        System.arraycopy(genes, 0, gene, 0, genes.length);
        fitness = 0;
        tamanho_raquete = Configuracao.RAQUETE_ALTURA;
    }
    
    public Genotipo( Genotipo copia ){
        
        gene = new double[copia.gene.length];
        System.arraycopy(copia.gene, 0, gene, 0, copia.gene.length);
        this.fitness = copia.fitness;
        this.tamanho_raquete = copia.tamanho_raquete;
    }
    
    public double getFitness() { return fitness; }
    
    public void setFitness( double fitness ){
        this.fitness = fitness;
    }
    
    public int getTamanhoRaquete() { return tamanho_raquete; }
    
    public void setTamanhoRaquete(int tamanho_raquete){
        this.tamanho_raquete = tamanho_raquete;
    }
    
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
        for (int i = 0 ; i < Configuracao.TAMANHO_CROMOSSOMO ; i++){
            novo.gene[i] = menor + (maior - menor) * Configuracao.R.nextDouble(true,true);
        }
        
        novo.tamanho_raquete = tamanhoAleatorio(
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
        
        novo.tamanho_raquete = (a.tamanho_raquete + b.tamanho_raquete) / 2;
        
        return novo;
    }
    
    // mutação do genótipo. para tentar escapar da solução subótima local
    public static Genotipo mutacao(Genotipo a){
        Genotipo g = new Genotipo();
        int maior = Configuracao.MIN_ALTURA_RAQUETE, menor = -maior;
        
        for (int i = 0 ; i < Configuracao.TAMANHO_CROMOSSOMO ; i++){
                g.gene[i] = a.gene[i] + ( -0.1 + 0.2 * Configuracao.R.nextDouble());
        }
        
        g.tamanho_raquete = a.tamanho_raquete + tamanhoAleatorio(menor, maior);
        
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
