

import java.io.FileWriter;
import java.io.IOException;

public class ReportExporter {
    public static void exportToFile(WeatherData data, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(data.toString());
            System.out.println("Weather report saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error exporting report: " + e.getMessage());
        }
    }
}
