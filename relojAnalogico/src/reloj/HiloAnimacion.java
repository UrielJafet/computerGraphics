package reloj;


import java.time.LocalTime;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.SwingUtilities;


public class HiloAnimacion extends Thread{
    private PanelReloj panel;
    private boolean corriendo = true;
    private Clip clip;
    private int ultimoSegundo = -1;

    public HiloAnimacion(PanelReloj panel) {
        this.panel = panel;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                getClass().getResource("clock_fixed.wav")
            );
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (corriendo) {
            LocalTime ahora = LocalTime.now();

            // Calcular ángulos
            double seg = ahora.getSecond() * 6;                    // 360/60
            double min = ahora.getMinute() * 6 + seg / 60.0;
            double hora = (ahora.getHour() % 12) * 30 + min / 12.0;

            // Solo actualiza el panel (NO dibuja)
            SwingUtilities.invokeLater(() ->
                panel.actualizarAngulos(seg, min, hora)
            );

            //play();

            if (ahora.getSecond() != ultimoSegundo) {
                ultimoSegundo = ahora.getSecond();
                play(); // una vez por segundo
            }

            try {
                Thread.sleep(1000); // o 1000 si quieres solo cada segundo
            } catch (InterruptedException e) {
                corriendo = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    public void play(){
        if (clip == null) return;
        clip.setFramePosition(0); // rebobina al inicio
        clip.start();
    }
}
