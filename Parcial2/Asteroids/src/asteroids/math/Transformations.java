package src.asteroids.math;

/**
* Transformaciones 2D. En pantalla Y crece hacia abajo, así que un ángulo
* positivo gira en el sentido de las manecillas del reloj.
*/

public final class Transformations {

    private Transformations(){}

    /**
    * Toma los vértices locales (alrededor de 0,0) y los convierte en
    * coordenadas de pantalla aplicando, en este orden:
    *   1) escala
    *   2) rotación:   x' = x*cos - y*sin     y' = x*sin + y*cos
    *   3) traslación: (tx, ty)
    * El resultado se guarda en px y py (sin crear arreglos nuevos).
    */

    public static void transformar(
        double[] lx, double[] ly,
        double angulo, double escala,
        double tx, double ty,
        int[] px, int[]py) {
        
        double c = Math.cos(angulo);
        double s = Math.sin(angulo);

        for(int i = 0; i < lx.length;i++){
            double x = lx[i] * escala;
            double y = ly[i] * escala;

            px[i] = (int) Math.round(x * c - y * s + tx);
            py[i] = (int) Math.round(x * s + y * c + tx);
        }

    }
}
