package asteroids.model;

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

    private final List<Entity> entities = new CopyOnWriteArrayList<>();

    private final Ship ship;

    public World(int ancho, int alto, KeyMap key){
        this.ancho = ancho;
        this.alto = alto;

        ship = new Ship(ancho/2.0,alto/2.0,key);
        entities.add(ship);
    }

    public void actualizar(double dt){
        for(Entity e : entities){
            e.actualizar(dt);
            e.envolver(ancho, alto);
        }
    }

    public void dibujar(Rasterizador r){
        for(Entity e: entities){
            e.dibujarEnvolvente(r, ancho, alto);
        }
    }
}
