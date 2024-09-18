import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForexGUI extends Application {
    @Override
    public void start(Stage stage) {
        ComboBox<String> currencyDropdown = new ComboBox<>();
        currencyDropdown.setEditable(true);

        // Populate with a long list of currency pairs (could also fetch from an API)
        currencyDropdown.getItems().addAll("EUR/USD", "USD/JPY", "GBP/USD", "USD/CAD", "AUD/USD", "NZD/USD");

        // Searchable functionality
        TextField editor = currencyDropdown.getEditor();
        editor.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            String filter = editor.getText();
            if (filter.isEmpty()) {
                currencyDropdown.setItems(FXCollections.observableArrayList("EUR/USD", "USD/JPY", "GBP/USD", "USD/CAD", "AUD/USD", "NZD/USD"));
            } else {
                ObservableList<String> filteredList = FXCollections.observableArrayList();
                for (String item : currencyDropdown.getItems()) {
                    if (item.toLowerCase().contains(filter.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                currencyDropdown.setItems(filteredList);
            }
        });

        TextField balanceField = new TextField();
        balanceField.setPromptText("Enter Balance");

        Button fetchPriceButton = new Button("Fetch Price");
        Label priceLabel = new Label();

        fetchPriceButton.setOnAction((ActionEvent e) -> {
            String selectedCurrencyPair = currencyDropdown.getValue();
            try {
                double price = ForexAPI.getRealTimePrice(selectedCurrencyPair);
                priceLabel.setText("Current Price: " + price);
            } catch (Exception ex) {
                priceLabel.setText("Error fetching price");
            }
        });

        VBox layout = new VBox(10, currencyDropdown, balanceField, fetchPriceButton, priceLabel);
        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}
