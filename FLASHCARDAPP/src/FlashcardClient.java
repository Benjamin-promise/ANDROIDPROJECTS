import java.io.*;
import java.net.Socket;

public class FlashcardClient {

    public static void sendContent(String serverAddress, int port, String uniqueID, String content) {
        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("STORE: " + uniqueID + ": " + content);
            System.out.println("Server response: " + in.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String retrieveContent(String serverAddress, int port, String uniqueID) {
        try (Socket socket = new Socket(serverAddress, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("GET: " + uniqueID);
            return in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
