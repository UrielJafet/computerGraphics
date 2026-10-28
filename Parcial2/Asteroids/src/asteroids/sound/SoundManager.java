package asteroids.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private final Map<String, Clip> sonidos = new HashMap<>();

    /**
     * Carga un sonido y lo guarda con un nombre.
     * Ejemplo: cargar("disparo", "/sonidos/shoot.wav");
     */
    public void cargar(String nombre, String ruta) {
        try {
            URL url = getClass().getResource(ruta);
            if (url == null) {
                System.err.println("No se encontró el sonido: " + ruta);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            sonidos.put(nombre, clip);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error al cargar sonido: " + nombre);
            e.printStackTrace();
        }
    }

    /**
     * Reproduce el sonido desde el inicio.
     * Si ya se está reproduciendo, lo reinicia.
     */
    public void play(String nombre) {
        Clip clip = sonidos.get(nombre);
        if (clip == null) {
            System.err.println("Sonido no cargado: " + nombre);
            return;
        }
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Reproduce el sonido en bucle (útil para música de fondo).
     */
    public void playLoop(String nombre) {
        Clip clip = sonidos.get(nombre);
        if (clip == null) return;

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Detiene un sonido.
     */
    public void stop(String nombre) {
        Clip clip = sonidos.get(nombre);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    /**
     * Detiene todos los sonidos.
     */
    public void stopAll() {
        for (Clip clip : sonidos.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }
}
