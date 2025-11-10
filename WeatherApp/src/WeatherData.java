
public class WeatherData {
    private String city;
    private double temperature;
    private int humidity;
    private double pressure;
    private double windSpeed;
    private String condition;

    public WeatherData(String city, double temperature, int humidity, double pressure, double windSpeed, String condition) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Weather in " + city + ":\n" +
               "  Temperature: " + temperature + " °C\n" +
               "  Humidity: " + humidity + " %\n" +
               "  Pressure: " + pressure + " hPa\n" +
               "  Wind Speed: " + windSpeed + " m/s\n" +
               "  Condition: " + condition;
    }
}
