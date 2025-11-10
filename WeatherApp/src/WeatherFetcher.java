import java.io.*;
import java.net.*;

public class WeatherFetcher {
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast";
    private static final String API_KEY = "28f61f4b78b8f9cc89e00e40149d3448";

    public static String getWeatherXML(String city) throws Exception {
        String urlString = WEATHER_URL + "?q=" + URLEncoder.encode(city, "UTF-8") + "&mode=xml&units=metric&appid=" + API_KEY;
        return fetchData(urlString);
    }

    public static String getForecastXML(String city) throws Exception {
        String urlString = FORECAST_URL + "?q=" + URLEncoder.encode(city, "UTF-8") + "&mode=xml&units=metric&appid=" + API_KEY;
        return fetchData(urlString);
    }

    private static String fetchData(String urlString) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } finally {
            conn.disconnect();
        }
    }
}
