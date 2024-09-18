import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static final Set<String> userNames = new HashSet<>();
    private static final Set<UserThread> userThreads = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat server is listening on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                UserThread newUser = new UserThread(socket, userThreads, userNames);
                userThreads.add(newUser);
                newUser.start();
            }
        } catch (IOException e) {
            System.out.println("Error in the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, UserThread excludeUser) {
        for (UserThread user : userThreads) {
            if (user != excludeUser) {
                user.sendMessage(message);
            }
        }
    }

    public static void addUserName(String userName) {
        userNames.add(userName);
    }

    public static void removeUser(String userName, UserThread user) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(user);
            System.out.println("The user " + userName + " quitted");
        }
    }

    public static Set<String> getUserNames() {
        return userNames;
    }

    public static boolean hasUsers() {
        return !userNames.isEmpty();
    }
}

class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private Set<UserThread> userThreads;
    private Set<String> userNames;

    public UserThread(Socket socket, Set<UserThread> userThreads, Set<String> userNames) {
        this.socket = socket;
        this.userThreads = userThreads;
        this.userNames = userNames;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
            }

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}
