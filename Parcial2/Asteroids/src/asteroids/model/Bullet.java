package asteroids.model;

import asteroids.graphics.Rasterizador;

public class Bullet extends Entity{
    
    private static final double SPEED = 400.0;
    private static final double LIFE = 1.0;
    private static final int COLOR = 0x5CFAFF;

    private double life = LIFE;

    public Bullet(double x, double y, double angulo, double vxShip, double vyShip){
        super(x, y, 2);
        this.angulo = angulo;

        vx = Math.sin(angulo) * SPEED + vxShip;
        vy = -Math.cos(angulo) * SPEED + vyShip;
    }

    @Override
    public void actualizar(double dt){
        life -= dt;
        if(life <= 0) active = false;
        super.actualizar(dt);
    }

    @Override
    public void dibujar(Rasterizador r, double desX, double desY){
        int tx = (int) Math.round(x + desX);
        int ty = (int) Math.round(y + desY);

        r.circunferencia(tx, ty, 2, COLOR);
    }
}
