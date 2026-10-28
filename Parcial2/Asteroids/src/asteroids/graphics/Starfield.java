package asteroids.graphics;

public class Starfield {

    private static final int NUM_ESTRELLAS = 150;

    private final double[] x;         // double para poder moverlas
    private final double[] y;
    private final int[] brillo;
    private final double[] fase;
    private final double[] velocidadParpadeo;
    private final double[] factorParalaje;   // 0.2, 0.5 o 1.0

    private double t = 0.0;
    private final int ancho;
    private final int alto;

    public Starfield(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;

        x = new double[NUM_ESTRELLAS];
        y = new double[NUM_ESTRELLAS];
        brillo = new int[NUM_ESTRELLAS];
        fase = new double[NUM_ESTRELLAS];
        velocidadParpadeo = new double[NUM_ESTRELLAS];
        factorParalaje = new double[NUM_ESTRELLAS];

        for (int i = 0; i < NUM_ESTRELLAS; i++) {
            x[i] = Math.random() * ancho;
            y[i] = Math.random() * alto;
            brillo[i] = 80 + (int) (Math.random() * 175);
            fase[i] = Math.random() * Math.PI * 2;
            velocidadParpadeo[i] = 1.5 + Math.random() * 3.5;

            // 3 capas de paralaje
            int capa = i % 3;
            if (capa == 0) factorParalaje[i] = 0.2;      // lejanas
            else if (capa == 1) factorParalaje[i] = 0.5; // medias
            else factorParalaje[i] = 1.0;                // cercanas
        }
    }

    public void actualizar(double dt) {
        t += dt;
    }

    /**
     * @param velNaveX  velocidad X de la nave (desde World)
     * @param velNaveY  velocidad Y de la nave
     */
    public void dibujar(Rasterizador r, double velNaveX, double velNaveY) {
        for (int i = 0; i < NUM_ESTRELLAS; i++) {
            // Movimiento en sentido contrario a la nave (paralaje)
            x[i] -= velNaveX * factorParalaje[i] * 0.016; // ~dt
            y[i] -= velNaveY * factorParalaje[i] * 0.016;

            // Wrap-around
            if (x[i] < 0) x[i] += ancho;
            if (x[i] >= ancho) x[i] -= ancho;
            if (y[i] < 0) y[i] += alto;
            if (y[i] >= alto) y[i] -= alto;

            // Parpadeo
            double f = 0.65 + 0.35 * Math.sin(t * velocidadParpadeo[i] + fase[i]);
            int v = (int) (brillo[i] * f);
            if (v < 0) v = 0;
            if (v > 255) v = 255;

            int color = (v << 16) | (v << 8) | v;
            int px = (int) x[i];
            int py = (int) y[i];

            r.pixel(px, py, color);

            if (brillo[i] > 200) {
                r.pixel(px - 1, py, color);
                r.pixel(px + 1, py, color);
                r.pixel(px, py - 1, color);
                r.pixel(px, py + 1, color);
            }
        }
    }
}
