package contenedor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ProtocoloCliente extends Thread
{
	private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    static Socket socket;
    static String id;
    
	public static void procesar(Socket socket2, BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut, String id2, String total) throws Exception
	{
        socket = socket2;
        id = id2;
        String inputLine;
        byte[] cifrado;
        String archivo = "";
        String time = "";
        boolean confirmacion = false;

		// Lee del teclado 
        // System.out.println("Escriba el mensaje para enviar: ");
        // Scanner consola = new Scanner(System.in);
        // String fromUser = consola.nextLine();

        // Envia por la red
        // Cliente al que se le está enviando
        pOut.println(id);       

        String fromServer = "";
        
        try
        {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            
            // Here we call receiveFile define new for that
            // file
            String destino = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3/lab/Laboratorio_3_1/ArchivosRecibidos/";
            destino = destino.concat("Cliente");
            destino = destino.concat(id);
            destino = destino.concat("-Prueba-");
            destino = destino.concat(total);
            destino = destino.concat(".txt");            
            
            // Tiempo inicial
            float init = System.nanoTime();

            // Recibir el archivo
            receiveFile(destino);

            // Tiempo Final
            float end = System.nanoTime();

            time = Float.toString(end-init) + " ns";
            System.out.println("Tiempo: " + time);
            
            pOut.println("Reciving the File from Server");

            inputLine = pIn.readLine();
            System.out.println(inputLine);
            cifrado = Digest.getDigestFile(destino);
            String hexaC = Digest.imprimirHexa(cifrado);
            confirmacion = inputLine.equals(hexaC);

            pOut.println(confirmacion);

            System.out.println("Concuerdan las cadenas: " + confirmacion);
            System.out.println("Reciving the File from Server");

            // Archivo que se está recibiendo
            archivo = pIn.readLine();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        String file2 = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3/lab/Laboratorio_3_1/Logs/";
        file2 = file2.concat(java.time.LocalDateTime.now().toString().replace(":","-"));
        file2 = file2.concat("-cliente.txt");

        dataInputStream.close();
        dataInputStream.close();

        log_c(file2, archivo, id, archivo, time, Boolean.toString(confirmacion));
        socket.close();
	 }
	
	// Receive file function is start here    
    private static void receiveFile(String fileName)
        throws Exception
    {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
 
        long size
            = dataInputStream.readLong(); // read file size
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0,
                       (int)Math.min(buffer.length, size))) != -1) 
        {
            // Here we write the file using write method
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes; // read upto file size
        }
        // Here we received file
        System.out.println("File is Received");
        fileOutputStream.close();
    }
    
    public static void log_c(String path, String nombre, String cliente, String tamanio, String tiempo, String estado)
    {
        try 
        {
            File file = new File(path);
            file.createNewFile();

            FileWriter writeFile = new FileWriter(path);
            writeFile.write("Cliente: " + cliente);
            writeFile.write("\n Nombre: " + nombre);
            writeFile.write("\n Tamaño: " + tamanio);
            writeFile.write("\n Estado: " + estado);
            writeFile.write("\n Tiempo: " + tiempo);
            writeFile.write("\n Log: " + "Cliente");
            writeFile.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}



