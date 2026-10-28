package asteroids.model;

import asteroids.graphics.Rasterizador;
import asteroids.math.Transformations;

public class Asteroids extends Entity{

    private static final int[] RELLENOS = {0x4A3F3A, 0x3E4A52, 0x55402F, 0x2F3B3A};  // oscuros
    private static final int[] BORDES = {0xE0CBCA, 0xB8C8D4, 0xD9B99B, 0xA9C4BF};  // claros

    private final int paleta;

    private final int tam;
    private final double velGiro;

    private final double[] lx;
    private final double[] ly;
    private final int[] px;
    private final int[] py;

    public Asteroids(double x, double y, int tam) {
        this(x, y, tam, (int) (Math.random() * RELLENOS.length));
    }

    public Asteroids(double x, double y, int tam, int paleta){
        super(x,y,radioPorTamano(tam));
        this.tam = tam;
        this.paleta = paleta;
        int n = (int) (Math.random() * 3) + 10;

        lx = new double[n];
        ly = new double[n];
        px = new int[n];
        py = new int[n];

        for(int i = 0; i < n; i++){
            double a = (2 * Math.PI) * i / n;
            double f = Math.random() * (1.0 - 0.7) + 0.7;
            double rv = radio * f;
            lx[i] = rv * Math.cos(a);
            ly[i] = rv * Math.sin(a);
        }
        
        angulo = Math.random() * (2 * Math.PI);
        velGiro = (Math.random() * 2) - 1;
        double d = Math.random() * (2 * Math.PI);
        double rapidez = (30 + Math.random() * 20) * (4 - tam);
        vx = Math.cos(d) * rapidez;
        vy = Math.sin(d) * rapidez;
    }

    private static double radioPorTamano(int tam){
        switch (tam) {
            case 3: return 50;
            case 2: return 30;
            default: return 20;
        }
    }

    public int getTam(){
        return tam;
    }

    public int getPaleta(){
        return paleta;
    }

    @Override 
    public void actualizar(double dt){
        angulo += velGiro * dt;
        super.actualizar(dt);
    }

    @Override 
    public void dibujar(Rasterizador r, double desX, double desY){
        double tx = x + desX;
        double ty = y + desY;

        Transformations.transformar(lx, ly, angulo, 1.0, tx, ty, px, py);
        r.rellenarPoligono(px, py, RELLENOS[paleta]);
        r.poligono(px, py, BORDES[paleta]);
    }
}