

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Application;

public class NoteClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public NoteClient() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String getNote(String userKey) throws IOException {
        out.println("GET");
        out.println(userKey);
        return in.readLine();
    }

    public void putNote(String userKey, String noteContent) throws IOException {
        out.println("PUT");
        out.println(userKey);
        out.println(noteContent);
        System.out.println(in.readLine()); // Print server response
    }

    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) {
        Application.launch(NoteClientapp.class, args);
    }

   
}