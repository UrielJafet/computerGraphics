package Subcadena;

public class Subcadenas {
    public static void main(String[] args) {
        String cadena = args[0];
        int inicio = 0;
        int longitud = cadena.length();
        int fin;
        for(int i = 0; i < cadena.length(); i++){
            fin = longitud - i;
            String subcadena = cadena.substring(inicio, fin);
            System.out.println(subcadena);
            
        }
        for(int i = 1; i <= cadena.length(); i++){
            fin = longitud;
            inicio = longitud - i;
            String subcadena = cadena.substring(inicio, fin);
            System.out.println(subcadena);
        }
    }
}
