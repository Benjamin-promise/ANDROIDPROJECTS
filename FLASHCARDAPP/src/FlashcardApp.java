import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FlashcardApp extends Application {

    private TextArea inputTextArea;
    private TextArea outputTextArea;
    private TextField uniqueIdField;
    private TextField serverAddressField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flashcard Learning App");

        inputTextArea = new TextArea();
        inputTextArea.setPromptText("Paste your text here...");

        outputTextArea = new TextArea();
        outputTextArea.setEditable(false);

        uniqueIdField = new TextField();
        uniqueIdField.setPromptText("Enter Unique ID");

        serverAddressField = new TextField();
        serverAddressField.setPromptText("Enter Server Address");

        Button summarizeButton = new Button("Summarize");
        summarizeButton.setOnAction(e -> summarizeText());

        Button generateQuestionsButton = new Button("Generate Questions");
        generateQuestionsButton.setOnAction(e -> generateQuestions());

        Button generateFlashcardsButton = new Button("Generate Flashcards");
        generateFlashcardsButton.setOnAction(e -> generateFlashcards());

        Button shareButton = new Button("Share");
        shareButton.setOnAction(e -> shareContent());

        Button retrieveButton = new Button("Retrieve");
        retrieveButton.setOnAction(e -> retrieveContent());

        VBox layout = new VBox(10, inputTextArea, summarizeButton, generateQuestionsButton, generateFlashcardsButton, uniqueIdField, serverAddressField, shareButton, retrieveButton, outputTextArea);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void summarizeText() {
        String text = inputTextArea.getText();
        // Implement summarization logic here
        String summary = TextProcessor.summarize(text);
        outputTextArea.setText(summary);
    }

    private void generateQuestions() {
        String text = inputTextArea.getText();
        // Implement question generation logic here
        String questions = TextProcessor.generateQuestions(text);
        outputTextArea.setText(questions);
    }

    private void generateFlashcards() {
        String text = inputTextArea.getText();
        // Implement flashcard generation logic here
        String flashcards = TextProcessor.generateFlashcards(text);
        outputTextArea.setText(flashcards);
    }

    private void shareContent() {
        String uniqueID = uniqueIdField.getText();
        String serverAddress = serverAddressField.getText();
        String content = outputTextArea.getText();

        if (!uniqueID.isEmpty() && !serverAddress.isEmpty() && !content.isEmpty()) {
            FlashcardClient.sendContent(serverAddress, 5555, uniqueID, content);
        } else {
            System.out.println("Please enter a unique ID, server address, and generate content first.");
        }
    }

    private void retrieveContent() {
        String uniqueID = uniqueIdField.getText();
        String serverAddress = serverAddressField.getText();

        if (!uniqueID.isEmpty() && !serverAddress.isEmpty()) {
            String content = FlashcardClient.retrieveContent(serverAddress, 5555, uniqueID);
            if (content != null) {
                outputTextArea.setText(content);
            } else {
                System.out.println("Content not found for the given ID.");
            }
        } else {
            System.out.println("Please enter a unique ID and server address.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
