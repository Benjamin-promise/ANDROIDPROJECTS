import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextProcessor {

    public static String summarize(String text) {
        // Basic summarization logic (sentence extraction)
        List<String> sentences = Arrays.asList(text.split("\\."));
        int summaryLength = Math.max(1, sentences.size() / 3);
        return sentences.stream()
                .limit(summaryLength)
                .collect(Collectors.joining(". ")) + ".";
    }

    public static String generateQuestions(String text) {
        // Basic question generation logic
        List<String> sentences = Arrays.asList(text.split("\\."));
        return sentences.stream()
                .map(sentence -> "What is the main point of: \"" + sentence.trim() + "\"?")
                .collect(Collectors.joining("\n"));
    }

    public static String generateFlashcards(String text) {
        // Basic flashcard generation logic
        List<String> sentences = Arrays.asList(text.split("\\."));
        return sentences.stream()
                .map(sentence -> "Flashcard: " + sentence.trim())
                .collect(Collectors.joining("\n"));
    }
}
