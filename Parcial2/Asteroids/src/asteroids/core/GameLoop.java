package asteroids.core;

import asteroids.graphics.Rasterizador;
import asteroids.model.World;

/*
* Game loop con paso fijo de actualización (60 fps)
* 
*/

public class GameLoop implements Runnable{
    private static final double STEPS = 1.0/60.0;
    private static final long NANOS_X_CUADRO = 16_666_667L;
    private static final int COLOR_BACK = 0x000000;

    private final World world;
    private final Canvas canvas;
    private final Rasterizador r;
    private final GamePanel panel;

    private volatile boolean running = true;

    public GameLoop(World world, Canvas canvas, GamePanel panel){
        this.world = world;
        this.canvas = canvas;
        this.panel = panel;
        this.r = new Rasterizador(canvas);
    }

    public void stop(){
        running = false;
    }

    @Override 
    public void run(){
        long previous = System.nanoTime();
        double accumulator = 0;

        while(running){
            long start = System.nanoTime();

            accumulator += (start - previous) / 1_000_000_000.0;
            previous = start;

            if(accumulator > 0.25) accumulator = 0.25;

            while (accumulator >= STEPS){
                world.actualizar(STEPS);
                accumulator -= STEPS;
            }

            canvas.limpiar(COLOR_BACK);
            world.dibujar(r);
            canvas.intercambiar();
            panel.repaint();

            long used = System.nanoTime() - start;
            long waitMS = (NANOS_X_CUADRO - used) / 1_000_000L;

            if(waitMS > 0){
                try {
                    Thread.sleep(waitMS);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    running = false;
                }
            }
        }
    }
}
