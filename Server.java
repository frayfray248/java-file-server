import java.io.*;
import java.net.*;
import java.nio.channels.IllegalBlockingModeException;


//This server program receives file requests from client programs and sends each on a file.
//It uses streamers to read and send data with no more than a specified amount of bytes at a time.
public class Server {

    //class variables
    protected final String HOST = "localhost";
    protected int port;
    protected final int BUFF_SIZE = 1024;

    //constructor
    public Server(int port) {
        this.port = port;
    }

    //This method reads a file from "library" and sends it to the client. Reading and sending uses buffered streamers
    //reading no more than BUFF_SIZE bytes at a time. Parameters include the file name, and a BufferedOutputStream
    //set with the client's OutputStream
    private void sendFile(String fileName, BufferedOutputStream out) {
        try (  
            //stream reader object for reading the file
            BufferedInputStream fileIn = new BufferedInputStream(
                new FileInputStream("library/" + fileName));
        ) {
            //byte array to hold streamed data
            byte[] buffer = new byte[BUFF_SIZE];
            int count = 0;
            //this loop uses a counter to read and send no more than BUFF_SIZE amount of bytes 
            //at a time.
            while((count = fileIn.read(buffer, 0, BUFF_SIZE)) > 0) {
                out.write(buffer, 0, count);
            }
            out.flush();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    //Core method for serving files. Every call waits for a new client to connect
    public void serve() {
        try (
            //sockets and streamers setup
            ServerSocket serverSocket = new ServerSocket(port); 
            Socket clientSocket = serverSocket.accept();
            BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
            //receiving file name
            String fileName = in.readLine();
            System.out.println("Received file request for file: " + fileName);

            //sending file
            sendFile(fileName, out);  
            System.out.println("Sent file: " + fileName);
            
        } catch (IOException e) {
			System.err.println(e);
			System.exit(-2);
		} catch (SecurityException e) {
			System.err.println(e);
			System.exit(-3);
		} catch (IllegalArgumentException e) {
			System.err.println(e);
			System.exit(-4);
		} catch (IllegalBlockingModeException e) {
			System.err.println(e);
			System.exit(-6);
		}
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
        //main server loop
        while (true) {
            server.serve();
        }
    }
}