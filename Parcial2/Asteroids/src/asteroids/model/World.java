package asteroids.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import asteroids.input.KeyMap;
import asteroids.graphics.Rasterizador;

/*
* Estado del juego: lista de entidades y reglas
* Agregar más adelante: asteroides, balas, colisiones puntaje y vidas.
*/

public class World {
    
    private final int ancho;
    private final int alto;
    private int score;
    private int nivel = 1;

    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private final List<Asteroids> asteroids = new CopyOnWriteArrayList<>();

    private static final int MAX_BULLETS = 4;
    private static final int DISTANCIA_MINIMA = 150;
    private final List<Bullet> bullets = new CopyOnWriteArrayList<>();

    private final Ship ship;

    public World(int ancho, int alto, KeyMap key){
        this.ancho = ancho;
        this.alto = alto;

        ship = new Ship(ancho/2.0,alto/2.0,key);
        entities.add(ship);

        createAsteroids(3 + nivel);
    }

    public void actualizar(double dt){
        if(bullets.size() < MAX_BULLETS){
            Bullet nueva = ship.tryToShoot();
            if(nueva != null) bullets.add(nueva);
        }
        for(Entity e : entities){
            e.actualizar(dt);
            e.envolver(ancho, alto);
        }
        for(Asteroids a: asteroids){
            a.actualizar(dt);
            a.envolver(ancho, alto);
        }
        for(Bullet b : bullets){
            b.actualizar(dt);
            b.envolver(ancho, alto);
        }
        processCollision();
        bullets.removeIf(b -> !b.active);
        asteroids.removeIf(a -> !a.active);

        if(asteroids.isEmpty()){
            nivel++;
            if(nivel <= 7){
                createAsteroids(3 + nivel);
            }
        }
    }

    public void dibujar(Rasterizador r){
        for(Entity e: entities){
            e.dibujarEnvolvente(r, ancho, alto);
        }
        for(Asteroids a: asteroids){
            a.dibujarEnvolvente(r, ancho, alto);
        }
        for(Bullet b: bullets){
            b.dibujarEnvolvente(r, ancho, alto);
        }
    }

    private static boolean collide(Entity a, Entity b, int ancho, int alto){
        double dx = Math.abs(a.x - b.x);
        if(dx > ancho/2.0) dx = ancho - dx;

        double dy = Math.abs(a.y - b.y);
        if(dy > alto/2.0) dy = alto - dy;

        double sum = a.radio + b.radio;
        return dx * dx + dy * dy < sum * sum;
    }

    private void processCollision(){
        List<Asteroids> nuevos = new ArrayList<>();

        for(Bullet b : bullets){
            if(!b.active) continue;

            for(Asteroids a : asteroids){
                if(!a.active) continue;

                if(collide(a, b, ancho, alto)){
                    a.active = false;
                    b.active = false;

                    if(a.getTam() == 3)
                        score += 20;
                    else if(a.getTam() == 2)
                        score += 50;
                    else if(a.getTam() == 1) 
                        score += 100;
                    System.out.println(score);
                    if(a.getTam() > 1){
                        nuevos.add(new Asteroids(a.x, a.y, a.getTam()-1));
                        
                    }
                    break;
                }
            }
        }
        asteroids.addAll(nuevos);
    }

    private void createAsteroids(int ast){
        for(int i =  0; i < ast; i++){
            double px, py;

            do{
                px = Math.random() * ancho;
                py = Math.random() * alto;
            } while ((px*px + py*py) < DISTANCIA_MINIMA);

            asteroids.add(new Asteroids(px, py, 3));
        }
    }
}
