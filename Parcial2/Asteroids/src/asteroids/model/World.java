package asteroids.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import asteroids.input.KeyMap;
import asteroids.sound.SoundManager;
import asteroids.graphics.Rasterizador;
import asteroids.graphics.Starfield;

/*
* Estado del juego: lista de entidades y reglas
* Agregar más adelante: asteroides, balas, colisiones puntaje y vidas.
*/

public class World {
    
    private final int ancho;
    private final int alto;
    private int score;
    private int nivel = 1;
    private enum State { PLAYING, GAME_OVER};

    private static final int START_LIVES = 4;
    private static final double RESPAWN_DELAY = 2.0;

    private State state = State.PLAYING;
    private int lives = START_LIVES;
    private double respawnTimer;
    private final KeyMap key;
    private double delayMusicaGameOver = -1;

    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private final List<Asteroids> asteroids = new CopyOnWriteArrayList<>();

    private static final int MAX_BULLETS = 4;
    private static final int DISTANCIA_MINIMA = 150;
    private final List<Bullet> bullets = new CopyOnWriteArrayList<>();

    private final Ship ship;
    private Starfield star;
    private SoundManager audio;

    public World(int ancho, int alto, KeyMap key, SoundManager audio){
        this.ancho = ancho;
        this.alto = alto;
        this.key = key;
        this.audio = audio;
        ship = new Ship(ancho/2.0,alto/2.0,key);
        star = new Starfield(ancho, alto);

        createAsteroids(3 + nivel);
    }

    public int getScore(){ return score;}
    public int getLives(){ return lives;}
    public int getNivel(){ return nivel;}
    public boolean isGameOver(){ return state == State.GAME_OVER; }

    public void actualizar(double dt){
        if(state == State.PLAYING && ship.active && bullets.size() < MAX_BULLETS){
            Bullet nueva = ship.tryToShoot();
            if(nueva != null) {
                bullets.add(nueva);
                audio.play("fire");
            }
        }
        if (ship.active) {
            ship.actualizar(dt);
            ship.envolver(ancho, alto);
        }
        for(Asteroids a: asteroids){
            a.actualizar(dt);
            a.envolver(ancho, alto);
        }
        for(Bullet b : bullets){
            b.actualizar(dt);
            b.envolver(ancho, alto);
        }
        star.actualizar(dt);
        processCollision();

        if(state == State.PLAYING && !ship.active){
            respawnTimer -= dt;
            if(respawnTimer <= 0) ship.reset(ancho/2.0,alto/2.0);
        }
        bullets.removeIf(b -> !b.active);
        asteroids.removeIf(a -> !a.active);
        if(asteroids.isEmpty()){
            nivel++;
            createAsteroids(Math.min(3 + nivel, 10));
        }
        if (delayMusicaGameOver >= 0) {
            delayMusicaGameOver -= dt;
            if (delayMusicaGameOver <= 0) {
                audio.playLoop("load");
                delayMusicaGameOver = -1;
        }
}
        if(state == State.GAME_OVER && key.restart){
            restart();
        }
    }

    public void dibujar(Rasterizador r){
        if(ship.active) ship.dibujarEnvolvente(r, ancho, alto);
        star.dibujar(r, ship.vx, ship.vy);
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
                    b.active = false;
                    splitAsteroid(a, nuevos);
                    break;
                }
            }
        }

        if(ship.active && !ship.isInvulnerability()){
            for(Asteroids a : asteroids){
                if(!a.active) continue;

                if(collide(ship, a, ancho, alto)){
                    ship.active = false;
                    lives--;
                    if(lives <= 0) {
                        state = State.GAME_OVER;
                        audio.stop("music");
                        audio.play("gameOver");
                        delayMusicaGameOver = 1.5;
                    }
                    else respawnTimer = RESPAWN_DELAY;
                    splitAsteroid(a, nuevos);
                    break;
                }
            }
        }
        asteroids.addAll(nuevos);
    }

    private void splitAsteroid(Asteroids a, List<Asteroids> nuevos){
        a.active = false;
        audio.play("explosion");

        if(a.getTam() == 3)
            score += 20;
        else if(a.getTam() == 2)
            score += 50;
        else if(a.getTam() == 1) 
            score += 100;
        if(a.getTam() > 1){
            nuevos.add(new Asteroids(a.x, a.y, a.getTam()-1, a.getPaleta()));
            nuevos.add(new Asteroids(a.x, a.y, a.getTam()-1, a.getPaleta()));
        }        
    }

    private void createAsteroids(int ast){
        for(int i =  0; i < ast; i++){
            double px, py, d2;

            do{
                px = Math.random() * ancho;
                py = Math.random() * alto;
                double dx = px - ship.x;
                double dy = py - ship.y;
                d2 = dx * dx + dy* dy;
            } while (d2 < DISTANCIA_MINIMA * DISTANCIA_MINIMA);

            asteroids.add(new Asteroids(px, py, 3));
        }
    }

    private void restart(){
        delayMusicaGameOver = -1;
        audio.stop("load");
        audio.playLoop("music");
        bullets.clear();
        asteroids.clear();

        lives =START_LIVES;
        score = 0;
        nivel = 1;
        respawnTimer = 0;
        ship.reset(ancho / 2.0, alto / 2.0);
        createAsteroids(3 + nivel);
        state = State.PLAYING;
    }   
}
