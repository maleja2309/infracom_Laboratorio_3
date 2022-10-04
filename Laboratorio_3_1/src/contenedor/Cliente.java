package contenedor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente 
{
	public static final int PUERTO = 3400;
    public static final String SERVIDOR = "localhost";
    
    public void inicializar(String id, String total) throws Exception
    {
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;
        
        System.out.println("Cliente ... " + id);
       
        try 
        {
            // Crea el Socket en el lado cliente 
            socket = new Socket(SERVIDOR, PUERTO);
            
            // Se conectan los flujos, tanto de salida como de entrada
            escritor = new PrintWriter(socket.getOutputStream(), true);
            lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            System.exit(-1);           
        }

        // Crea el flujo para leer lo que escriba el cliente por el teclado 
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        // Se ejecuta el protocolo en el lado cliente
        ProtocoloCliente.procesar(socket, stdIn, lector, escritor, id, total);

        // se cierran los flujos y el socket 
        stdIn.close();
        escritor.close();
        lector.close();
        socket.close();
    }

}
