import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NoteServer {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private Map<String, String> notes; // Key: userKey, Value: note content

    public NoteServer() {
        notes = new HashMap<>();
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Server started on port " + PORT);
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String command;
                while ((command = in.readLine()) != null) {
                    switch (command) {
                        case "GET":
                            handleGet();
                            break;
                        case "PUT":
                            handlePut();
                            break;
                        default:
                            out.println("Unsupported command: " + command);
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleGet() throws IOException {
            String userKey = in.readLine();
            String note = notes.get(userKey);
            out.println(note != null ? note : "Note not found");
        }

        private void handlePut() throws IOException {
            String userKey = in.readLine();
            String newNote = in.readLine();
            notes.put(userKey, newNote);
            out.println("Note saved successfully");
        }
    }

    public static void main(String[] args) {
        NoteServer server = new NoteServer();
        server.start();
    }
}
