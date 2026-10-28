package asteroids.graphics;

import asteroids.model.Ship;
import asteroids.model.World;

public class Hud {
    
    private static final int COLOR = 0xFFFFFF;
    private static final int MARGEN = 20;

    private final int ancho;
    private final int alto;

    public Hud(int ancho, int alto){
        this.ancho = ancho;
        this.alto = alto;
    }

    public void dibujar(Rasterizador r, World w){
        // ===== SCORE (arriba a la izquierda) =====
        int yPuntaje = MARGEN + 7 * 2 + 8;
        Fonts.dibujarTexto(r, "SCORE", MARGEN, MARGEN, 2, COLOR);
        Fonts.dibujarTexto(r, String.valueOf(w.getScore()), MARGEN, yPuntaje, 3, COLOR);

        int yVidas = yPuntaje + 7 * 3 + 22;
        for (int i = 0; i < w.getLives(); i++) {
            Ship.dibujarIcono(r, MARGEN + 10 + i * 24, yVidas, 0.6);
        }

        // ===== LEVEL (arriba a la derecha) =====
        int scale = 2;
        String textLevel = "LEVEL " + w.getNivel();
        int xLevel = ancho - MARGEN - Fonts.anchoTexto(textLevel, scale);
        Fonts.dibujarTexto(r, textLevel, xLevel, MARGEN, scale, COLOR);

        // ===== GAME OVER =====
        if (w.isGameOver()) {
            // GAME OVER centrado
            int escalaGO = 6;
            String go = "GAME OVER";
            int xGO = (ancho - Fonts.anchoTexto(go, escalaGO)) / 2;
            int yGO = alto / 2 - 40;
            Fonts.dibujarTexto(r, go, xGO, yGO, escalaGO, 0xFF4040);

            // PRESS R debajo
            int escalaPR = 2;
            String pr = "PRESS R";
            int xPR = (ancho - Fonts.anchoTexto(pr, escalaPR)) / 2;
            int yPR = yGO + (7 * escalaGO) + 20;   // 7 = alto del glifo
            Fonts.dibujarTexto(r, pr, xPR, yPR, escalaPR, COLOR);
        }
    }
}
