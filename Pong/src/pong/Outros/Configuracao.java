package pong.Outros;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuracao {
    
    public static MersenneTwisterFast R = new MersenneTwisterFast();
    
    public static final int NUM_OBJETIVOS = 2;
    
    public static int LARGURA_TELA = 800;
    public static int ALTURA_TELA = 600;
    
    public static int RAQUETE_LARGURA = 25;
    public static int RAQUETE_ALTURA = 150;
    public static int BOLA_RAIO = 20;
    
    public static int MAX_ALTURA_RAQUETE = 2 * ALTURA_TELA / 5;
    public static int MIN_ALTURA_RAQUETE = RAQUETE_ALTURA / 2;
    
    public static int QUANTIDADE_BOLAS = 5;   
    public static int PTS_BOLA_ESPECIAL = QUANTIDADE_BOLAS * 2;

    public static int VELOCIDADE_RAQUETE_PADRAO = 15;
    private static double valor = VELOCIDADE_RAQUETE_PADRAO;
    public static int MAX_VELOCIDADE_RAQUETE = (int) valor * RAQUETE_ALTURA/MIN_ALTURA_RAQUETE;
    public static int MIN_VELOCIDADE_RAQUETE = (int) valor * RAQUETE_ALTURA/MAX_ALTURA_RAQUETE;
    public static int VELOCIDADE_BOLA = 5;

    public static int RAQUETE_INCLINACAO = 3;
    
    public static int TAMANHO_CROMOSSOMO = 5;
    public static int MAX_POPULACAO = 30;
    public static double INTERVALO_GENES = 1.5;
    public static int RODADAS = 3;
    public static int MAX_PONTUACAO = (PTS_BOLA_ESPECIAL + QUANTIDADE_BOLAS - 1) * RODADAS;
    public static int MAX_GERACOES = 500;
    
    public static Font FONTE_TIPO;

    public static List<Double> config = new ArrayList<>();
    
    public Configuracao() throws FileNotFoundException, IOException{
        
        
        try {
            FONTE_TIPO = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResourceAsStream("prstartk.ttf"));
        } catch (FontFormatException ex) {
            Logger.getLogger(Configuracao.class.getName()).log(Level.SEVERE, null, ex);
        }
        GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        genv.registerFont(FONTE_TIPO);
        
        File f = new File("./arquivos/input.txt");
        if(f.exists() && !f.isDirectory()) { 
            try (Scanner s = new Scanner(f)) {
                while (s.hasNextLine()){
                    config.add(s.nextDouble());
                }
            }
            
            LARGURA_TELA = config.get(0).intValue();
            ALTURA_TELA = config.get(1).intValue();

            RAQUETE_LARGURA = config.get(2).intValue();
            RAQUETE_ALTURA = config.get(3).intValue();
            BOLA_RAIO = config.get(4).intValue();
            QUANTIDADE_BOLAS = config.get(5).intValue(); 

            VELOCIDADE_RAQUETE_PADRAO = config.get(6).intValue();
            VELOCIDADE_BOLA = config.get(7).intValue();

            TAMANHO_CROMOSSOMO = config.get(8).intValue();
            MAX_POPULACAO = config.get(9).intValue();
            INTERVALO_GENES = config.get(10);
            RODADAS = config.get(11).intValue();
            MAX_GERACOES = config.get(12).intValue();
            
            // reconfigura os outros valores que dependem de outros aspectos
            MAX_ALTURA_RAQUETE = 2 * ALTURA_TELA / 5;
            MIN_ALTURA_RAQUETE = RAQUETE_ALTURA / 2;
            PTS_BOLA_ESPECIAL = QUANTIDADE_BOLAS * 2;
            MAX_PONTUACAO = (PTS_BOLA_ESPECIAL + QUANTIDADE_BOLAS - 1) * RODADAS;
            valor = VELOCIDADE_RAQUETE_PADRAO;
            MAX_VELOCIDADE_RAQUETE = (int) valor * RAQUETE_ALTURA/MIN_ALTURA_RAQUETE;
            MIN_VELOCIDADE_RAQUETE = (int) valor * RAQUETE_ALTURA/MAX_ALTURA_RAQUETE;  
        }   
    }   
}
