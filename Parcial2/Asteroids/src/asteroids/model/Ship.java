package asteroids.model;

import asteroids.input.KeyMap;
import asteroids.graphics.Rasterizador;
import asteroids.math.Transformations;

public class Ship extends Entity{
    
    private static final double VEL_GIRO = 4.0;
    private static final double ACELERACION = 250.0;
    private static final double VEL_MAXIMA = 350.0;
    private static final double ROZAMIENTO = 0.6;

    private static final int COLOR_CASCO = 0xFFFFFF;
    private static final int COLOR_LLAMA = 0xFF9020;

    private static final double[] CASCO_X = {0,8,0,-8};
    private static final double[] CASCO_Y = {-12,10,6,10};

    private static final double[] LLAMA_X = {-4,0,4};
    private static final double[] LLAMA_Y = {8,18,8};

    private static final double RECARGA = 0.25;
    private double recarga;

    private static final double INVULNERABILITY =3.0;
    private double invulnerability;

    private final KeyMap key;

    private final int[] cx = new int[4];
    private final int[] cy = new int[4];
    private final int[] fx = new int[3];
    private final int[] fy = new int[3];

    public Ship(double x, double y, KeyMap key){
        super(x,y,12);
        this.key = key;
    }

    public boolean isInvulnerability(){ return invulnerability > 0;}

    public void reset(double cx, double cy){
        x = cx; y = cy; 
        vx = 0; vy = 0;
        angulo = 0;

        invulnerability = INVULNERABILITY;
        recarga = 0;
        active = true;
    }

    public Bullet tryToShoot(){
        if(!key.shot || recarga > 0) return null;
        recarga = RECARGA;

        double bx = x + Math.sin(angulo) * 12;
        double by = y - Math.cos(angulo) * 12;
        return new Bullet(bx, by, angulo, vx, vy);
    }

    @Override
    public void actualizar(double dt){
        //Rotación
        if(key.left) angulo -= VEL_GIRO * dt;
        if(key.right) angulo += VEL_GIRO * dt;

        // Empuje de la nave
        if(key.push){
            vx += Math.sin(angulo) * ACELERACION * dt;
            vy += -Math.cos(angulo) * ACELERACION * dt;
        }

        // Rozamiento suave
        double factor = Math.pow(ROZAMIENTO, dt);
        vx *= factor;
        vy *= factor;

        // Limitar velocidad
        double velocidad = Math.sqrt(vx * vx + vy *vy);
        if(velocidad > VEL_MAXIMA){
            vx = vx / velocidad * VEL_MAXIMA;
            vy = vy /velocidad * VEL_MAXIMA;
        }
        
        recarga -= dt;

        if(isInvulnerability()) invulnerability -= dt;
        super.actualizar(dt);
    }

    @Override
    public void dibujar(Rasterizador r, double desX, double desY){
        double tx = x + desX;
        double ty =  y + desY;
        int rs = (int) (invulnerability * 10) % 2;
        if(isInvulnerability() && rs != 0) return;

        // Rotacion + traslación de los vértices locales
        Transformations.transformar(CASCO_X, CASCO_Y, angulo, 1.0, tx, ty, cx, cy);
        r.poligono(cx, cy, COLOR_CASCO);

        if(key.push){
            Transformations.transformar(LLAMA_X, LLAMA_Y, angulo, 1.0, tx, ty, fx, fy);
            r.linea(fx[0],fy[0],fx[1],fy[1],COLOR_LLAMA);
            r.linea(fx[1],fy[1],fx[2],fy[2],COLOR_LLAMA);
        }
    }
}
