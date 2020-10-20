import java.io.*;
import java.net.*;


//This client program requests a file from a server and saves it in the client directory.
//It uses streamers to received and write data with no more than a specified amount of bytes at a time.
public class Client {

    protected String serverName;
    protected int serverPort;
    protected String fileName;
    protected final int BUFF_SIZE = 1024;

    public Client(String serverName, int serverPort, String fileName) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.fileName = fileName;
    }

    //core method for requesting a file from a server. It sends a file name to a server and then receives 
    //file data using streamers, reading no more than 1024 bytes at a time.
    public void getFile() {
        try (
            //socket
            Socket socket = new Socket(serverName, serverPort);
            //streamers
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream fileOut = new BufferedOutputStream(
                new FileOutputStream(fileName));
        ) {
            //sending a file name to request a file
            out.println(fileName);
            System.out.println("Sent request for file: " + fileName);

            //byte array to hold streamed data
            byte[] buffer = new byte[BUFF_SIZE];
            int count = 0;

            //This loop uses a counter to receive and write no more than BUFF_SIZE amount of bytes 
            //at a time.
            while((count = in.read(buffer, 0, BUFF_SIZE)) > 0) {
                fileOut.write(buffer, 0, count);
            }
            fileOut.flush();

        
        } catch (UnknownHostException e) {
            System.err.println(e);
            System.exit(-1);
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-2);
        } catch (SecurityException e) {
            System.err.println(e);
            System.exit(-3);
        } catch (IllegalArgumentException e) {
            System.err.println(e);
            System.exit(-4);
        }
}

    //main client method
    public static void main(String[] args) {
        Client client = new Client("localhost", 12345, args[0]);
        client.getFile();
    }


}