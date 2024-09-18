

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class NoteClientapp extends Application {
    private NoteClient client;

    @Override
    public void start(Stage primaryStage) {
        try {
            client = new NoteClient();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(5);

        Label userKeyLabel = new Label("User Key:");
        TextField userKeyField = new TextField();

        Label noteLabel = new Label("Note:");
        TextArea noteArea = new TextArea();

        Button getNoteButton = new Button("Get Note");
        getNoteButton.setOnAction((var e) -> {
            String userKey = userKeyField.getText();
            try {
                String note = client.getNote(userKey);
                noteArea.setText(note);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button putNoteButton = new Button("Save Note");
        putNoteButton.setOnAction(e -> {
            String userKey = userKeyField.getText();
            String noteContent = noteArea.getText();
            try {
                client.putNote(userKey, noteContent);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(userKeyLabel, userKeyField, noteLabel, noteArea, getNoteButton, putNoteButton);

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Note Client");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
