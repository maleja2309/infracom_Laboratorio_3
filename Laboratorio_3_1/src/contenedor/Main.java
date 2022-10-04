package contenedor;

import java.io.IOException;
import java.util.Scanner;

public class Main 
{
    public static void main(final String[] args) throws Exception
    {
        System.out.println("Especifique cuantos clientes estan en el sistema y reciben el archivo: ");
        Scanner consola = new Scanner(System.in);
        String total = consola.nextLine();
        
        for (int i = 0; i < Integer.parseInt(total); i ++)
        {
            Cliente cliente = new Cliente(); 
            cliente.inicializar(Integer.toString(i), total);
        }
    }
}
