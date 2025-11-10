
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherFetcher {

    private static final String API_KEY = "28f61f4b78b8f9cc89e00e40149d3448"; // your working key
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static String getWeatherXML(String city) throws Exception {
        String endpoint = String.format("%s?q=%s&mode=xml&units=metric&appid=%s", BASE_URL, city, API_KEY);
        URL url = new URL(endpoint);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        conn.disconnect();

        return response.toString();
    }
}
