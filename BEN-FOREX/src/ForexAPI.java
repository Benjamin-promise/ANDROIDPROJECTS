import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ForexAPI {
    public static double getRealTimePrice(String currencyPair) throws Exception {
        String apiKey = "YOUR_API_KEY";
        String urlString = "https://api.example.com/forex?pair=" + currencyPair + "&apikey=" + apiKey;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        JSONObject json = new JSONObject(content.toString());
        return json.getDouble("price"); // Adjust based on API response structure
    }

    static double getRealTimePrice(String selectedCurrencyPair) {
    }
}
