package contenedor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServidor extends Thread
{
    Socket socket;
    int numeroThreads;
    String option;

    public ThreadServidor(Socket pSocket, int pNumeroThreads, String Option)
    {
        socket = pSocket;
        numeroThreads = pNumeroThreads;
        option = Option;
    }
    
    public void proceso()
    {
	    try 
		{
			// Se conectan los flujos, tanto de salida como de entrada
			PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            ProtocoloServidor protocol = new ProtocoloServidor(socket, option, lector, escritor);

			protocol.procesar();

			escritor.close();
			lector.close();
			socket.close();
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
    }

}
