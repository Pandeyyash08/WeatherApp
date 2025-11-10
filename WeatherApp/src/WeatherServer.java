import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class WeatherServer {
    private static final int PORT = 8081;

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/api/weather", new WeatherHandler(false));
            server.createContext("/api/forecast", new WeatherHandler(true));
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    static class WeatherHandler implements HttpHandler {
        private final boolean isForecast;

        public WeatherHandler(boolean isForecast) {
            this.isForecast = isForecast;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(204, -1);
                    return;
                }

                String query = exchange.getRequestURI().getQuery();
                String city = "";
                
                if (query != null) {
                    String[] params = query.split("=");
                    if (params.length > 1 && params[0].equals("city")) {
                        city = URLDecoder.decode(params[1], StandardCharsets.UTF_8);
                    }
                }

                if (city.isEmpty()) {
                    sendErrorResponse(exchange, 400, "City parameter is required");
                    return;
                }

                System.out.println("Fetching weather for city: " + city);

                try {
                    String weatherData;
                    if (isForecast) {
                        System.out.println("Fetching forecast for city: " + city);
                        weatherData = WeatherFetcher.getForecastXML(city);
                    } else {
                        System.out.println("Fetching current weather for city: " + city);
                        weatherData = WeatherFetcher.getWeatherXML(city);
                    }
                    exchange.getResponseHeaders().set("Content-Type", "application/xml");
                    sendResponse(exchange, 200, weatherData);
                } catch (Exception e) {
                    System.out.println("Error fetching weather data: " + e.getMessage());
                    sendErrorResponse(exchange, 500, "Error fetching weather data: " + e.getMessage());
                }

            } catch (Exception e) {
                System.out.println("Server error: " + e.getMessage());
                sendErrorResponse(exchange, 500, "Internal server error");
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }

        private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
            String errorJson = String.format("{\"error\": \"%s\"}", message.replace("\"", "'"));
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            sendResponse(exchange, statusCode, errorJson);
        }
    }
}
