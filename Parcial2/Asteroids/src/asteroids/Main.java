package asteroids;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import asteroids.input.KeyMap;
import asteroids.model.World;
import asteroids.core.*;


public class Main {
    private static final int ANCHO = 800;
    private static final int ALTO = 600;

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            Canvas canvas = new Canvas(ANCHO, ALTO);
            KeyMap key = new KeyMap();
            GamePanel panel = new GamePanel(canvas, key);

            JFrame frame = new JFrame("Asteroids");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            panel.requestFocusInWindow();

            World world = new World(ANCHO, ALTO, key);
            GameLoop loop = new GameLoop(world, canvas, panel);

            Thread thread = new Thread(loop, "game-loop");
            thread.setDaemon(true);
            thread.start();
        });
    }
}
