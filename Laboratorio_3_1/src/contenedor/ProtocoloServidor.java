package contenedor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class ProtocoloServidor extends Thread
{
    static String option;
    static BufferedReader pIn;
    static PrintWriter pOut;
    static Socket socket;
    static ServerSocket ss;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    
    public  ProtocoloServidor(Socket socket2, String Option, BufferedReader PIn, PrintWriter POut)
    {
        option = Option;
        pIn = PIn;
        pOut = POut;  
        socket = socket2; 
    }
    
    public void procesar() throws IOException
    {
        String inputLine;
        byte[] cifrado;
        String file;
        String name;
        float init = 0;

        // Cliente al que se le está realizando la transferencia
        String cliente = pIn.readLine();
        
        // Here we define Server Socket running on port 3400
        try 
        {            
           // System.out.println("Entrada a procesar: " + inputLine);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
                       
            // Call SendFile Method
            if (option.equals("1"))
            {
                init = System.nanoTime();
                file = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3/lab/Laboratorio_3_1/Archivos_de_Envio/100MB.txt"; 
                            // Tiempo inicial

                sendFile(file);
                cifrado = Digest.getDigestFile(file);
                name = "100MB";

            }
            else
            {
                file = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3/lab/Laboratorio_3_1/Archivos_de_Envio/250MB.txt"; 
                sendFile(file);
                cifrado = Digest.getDigestFile(file);
                name = "250MB";
            }
            // Procesa la entrada 
            // Lee del flujo de entrada

            inputLine = pIn.readLine();

            // Escribe el flujo de salida 
            pOut.println(Digest.imprimirHexa(cifrado));
          
            System.out.println("Cifrado: " + Digest.imprimirHexa(cifrado));
            System.out.println("Salida procesada: " + inputLine);

            // Tiempo Final
            float end = System.nanoTime();

            String time = Float.toString(end-init) + " ns";

            String estado = pIn.readLine();

            String file2 = "C:/Users/malej/OneDrive - Universidad de los Andes/Semestres/Semestre 9/infracom/Laboratorios/3/lab/Laboratorio_3_1/Logs/";
            file2 = file2.concat(java.time.LocalDateTime.now().toString().replace(":","-"));
            file2 = file2.concat("-servidor.txt");

            // Tamaño y nombre del archivo
            pOut.println(name);   

            log_s(file2, name, cliente, name, time, estado);

            dataInputStream.close();
            dataOutputStream.close();                         
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    // sendFile function define here
    private static void sendFile(String path) throws Exception
    {
        int bytes = 0;
        // Open the File where he located in your pc
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
 
        // Here we send the File to Server
        dataOutputStream.writeLong(file.length());
        // Here we  break file into chunks
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) 
        {
          // Send the file to Server Socket 
          dataOutputStream.write(buffer, 0, bytes);
          dataOutputStream.flush();
        }
        // close the file here
        fileInputStream.close();
    }

    public static void log_s(String path, String nombre, String cliente, String tamanio, String tiempo, String estado)
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
            writeFile.write("\n Log: " + "Servidor");
            writeFile.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

    }
}
