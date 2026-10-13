package OrdenarListaNumeros;
import java.util.Arrays;

public class OrdenNumeros {
    public static void main(String[] args){
        int[] numeros = new int[args.length];
        for (int i = 0; i < args.length; i++){
            numeros[i] = Integer.parseInt(args[i]);
        }
        Arrays.sort(numeros);
        for (int num : numeros) {
            System.out.println(num);
        }
    }
}
