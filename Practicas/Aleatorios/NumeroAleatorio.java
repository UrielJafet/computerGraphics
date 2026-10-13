package Aleatorios;

public class NumeroAleatorio {
    public static void main(String[] args) {
        
        int num1 = (int) (Math.random() * 100) + 1;
        int num2 = (int) (Math.random() * 100) + 1;
        int mayor = Math.max(num1, num2);

        System.out.println("Número aleatorio entre 1 y 100: " + num1);
        System.out.println("Número aleatorio entre 1 y 100: " + num2);
        System.out.println("El número mayor es: " + mayor);
    }
    
}
