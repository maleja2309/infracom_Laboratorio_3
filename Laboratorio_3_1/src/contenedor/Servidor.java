package contenedor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;

public class Servidor
{
    public static final int PUERTO = 3400;
	
	public static void main(String args[]) throws IOException
	{
		ServerSocket ss = null;
		boolean continuar = true;
		int numeroThreads = 0;
		String option = null;

		System.out.println("Main Server ...");
		System.out.println("Seleccione una de las opciones para el envio del archivo: ");
		System.out.println("1. 100 MB ");
		System.out.println("2. 250 MB ");

		try
		{
			// Espacio para seleccionar el archivo que se le envia a los clientes
			Scanner scanner = new Scanner(System.in);	
			option = scanner.nextLine();
			System.out.println("");
			System.out.println("Servidor escuchando ... ");
			ss = new ServerSocket(PUERTO);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		while (continuar)
		{
			// Crear socket 
			Socket socket = ss.accept();
			
			// Crear el thread con el socket y el id 
			ThreadServidor thread = new ThreadServidor(socket, numeroThreads, option);
			thread.proceso();
			
			thread.start();

			numeroThreads++;
		}
		ss.close();		
	}
}
