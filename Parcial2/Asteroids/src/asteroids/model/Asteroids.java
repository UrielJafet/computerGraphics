package asteroids.model;

import asteroids.graphics.Rasterizador;
import asteroids.math.Transformations;

public class Asteroids extends Entity{

    private static final int COLOR_ASTEROID = 0xE0CBCA;

    private final int tam;
    private final double velGiro;

    private final double[] lx;
    private final double[] ly;
    private final int[] px;
    private final int[] py;

    public Asteroids(double x, double y, int tam){
        super(x,y,radioPorTamano(tam));
        this.tam = tam;

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
        r.poligono(px, py, COLOR_ASTEROID);
    }
}