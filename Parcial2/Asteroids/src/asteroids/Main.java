package asteroids;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import asteroids.input.KeyMap;
import asteroids.model.World;
import asteroids.sound.SoundManager;
import asteroids.core.*;
import asteroids.graphics.Hud;


public class Main {
    private static final int ANCHO = 800;
    private static final int ALTO = 600;

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            Canvas canvas = new Canvas(ANCHO, ALTO);
            KeyMap key = new KeyMap();
            GamePanel panel = new GamePanel(canvas, key);
            Hud hud = new Hud(ANCHO, ALTO);

            SoundManager audio = new SoundManager();
            audio.cargar("fire", "/resources/sounds/fire.wav");
            audio.cargar("explosion", "/resources/sounds/exp-small.wav");
            audio.cargar("gameOver", "/resources/sounds/gameOver.wav");
            audio.cargar("load", "/resources/sounds/3-load-screen.wav");
            audio.cargar("music", "/resources/sounds/music.wav");

            JFrame frame = new JFrame("Asteroids");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            panel.requestFocusInWindow();

            audio.playLoop("music");
            World world = new World(ANCHO, ALTO, key, audio);
            GameLoop loop = new GameLoop(world, canvas, panel, hud);

            Thread thread = new Thread(loop, "game-loop");
            thread.setDaemon(true);
            thread.start();
        });
    }
}
