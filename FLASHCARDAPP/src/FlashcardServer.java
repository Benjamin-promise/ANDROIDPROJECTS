import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class FlashcardServer {

    private static Map<String, String> dataStore = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            System.out.println("Server is listening on port 5555");
            while (true) {
                new FlashcardServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class FlashcardServerThread extends Thread {
        private Socket socket;

        public FlashcardServerThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String request = in.readLine();
                if (request != null) {
                    String[] parts = request.split(": ", 3);
                    if (parts.length == 3) {
                        String command = parts[0];
                        String uniqueID = parts[1];
                        String content = parts[2];

                        if ("STORE".equals(command)) {
                            dataStore.put(uniqueID, content);
                            out.println("Content received and stored with ID: " + uniqueID);
                        } else {
                            out.println("Invalid request format");
                        }
                    } else if (parts.length == 2) {
                        String command = parts[0];
                        String uniqueID = parts[1];

                        if ("GET".equals(command)) {
                            String content = dataStore.get(uniqueID);
                            out.println(content != null ? content : "No content found for ID: " + uniqueID);
                        } else {
                            out.println("Invalid request format");
                        }
                    } else {
                        out.println("Invalid request format");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
