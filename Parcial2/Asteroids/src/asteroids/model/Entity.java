package asteroids.model;

import asteroids.graphics.Rasterizador;

public abstract class Entity {
    
    protected double x;
    protected double y;
    protected double vx;
    protected double vy;
    protected double angulo; // en radianes
    protected double radio; // para colisiones y envolturas

    public Entity(double x, double y, double radio){
        this.x = x;
        this.y = y;
        this.radio = radio;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getRadio(){
        return radio;
    }

    // Movimiento básico: x = x + v * dt
    public void actualizar(double dt){
        x += vx * dt;
        y += vy * dt;
    }
    // Wrap-around: al salir por un borde aparece por el contrario
    public void envolver(int ancho, int alto){
        x = x % ancho;
        if(x<0) x+= ancho;
        y = y % alto;
        if(y<0) y+= alto;
    }
    // Diuja la figura desplazada (desX, desY) respecto a su posición
    public abstract void dibujar(Rasterizador r, double ancho, double alto);

    public void dibujarEnvolvente(Rasterizador r, int ancho, int alto){
        boolean left = x - radio < 0;
        boolean right = x + radio >= ancho;
        boolean up = y - radio < 0;
        boolean down = y + radio >= alto;

        dibujar(r,0,0);

        if(left) dibujar(r, ancho, 0);
        if(right) dibujar(r, -ancho, 0);
        if(up) dibujar(r, 0, alto);
        if(down) dibujar(r, 0, -alto);

        if(left && up) dibujar(r, ancho, alto);
        if(left && down) dibujar(r, ancho, -alto);
        if(right && up) dibujar(r, -ancho, alto);
        if(right && up) dibujar(r, -ancho, -alto);
    }
}
