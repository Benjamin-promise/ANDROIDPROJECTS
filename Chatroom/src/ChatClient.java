import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatClient extends Application {
    private String hostname;
    private int port;
    private String userName;

    private PrintWriter writer;
    private BufferedReader reader;

    private TextArea textArea;
    private TextField inputField;
    private Button sendButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        hostname = "localhost";
        port = 12345;

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(5);

        textArea = new TextArea();
        textArea.setEditable(false);
        root.getChildren().add(textArea);

        inputField = new TextField();
        root.getChildren().add(inputField);

        sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendMessage());
        root.getChildren().add(sendButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Client");
        primaryStage.show();

        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(hostname, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = reader.readLine()) != null) {
                        String message = serverMessage;
                        Platform.runLater(() -> textArea.appendText(message + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String userNamePrompt = "Enter your name: ";
            userName = promptForUserName(userNamePrompt);
            writer.println(userName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String promptForUserName(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("User Name");
        dialog.setHeaderText(prompt);
        return dialog.showAndWait().orElse("Anonymous");
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (message != null && !message.trim().isEmpty()) {
            writer.println(message);
            inputField.clear();
        }
    }
}
