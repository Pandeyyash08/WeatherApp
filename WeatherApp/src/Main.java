
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Weather Information App (Console) ===");

        while (true) {
            System.out.print("\nEnter city name (or 'exit'): ");
            String city = sc.nextLine().trim();
            if (city.equalsIgnoreCase("exit")) break;
            if (city.isEmpty()) continue;

            try {
                fetchAndShow(city);
                // optional: schedule auto-refresh every 10 minutes (demonstration)
                System.out.print("Auto-refresh every 10 minutes? (y/N): ");
                String ans = sc.nextLine().trim();
                if (ans.equalsIgnoreCase("y")) {
                    scheduler.scheduleAtFixedRate(() -> {
                        try {
                            System.out.println("\n[Auto-refresh]");
                            fetchAndShow(city);
                        } catch (Exception e) {
                            System.out.println("Auto-refresh error: " + e.getMessage());
                        }
                    }, 10, 10, TimeUnit.MINUTES);
                    System.out.println("Auto-refresh scheduled. Type 'exit' to quit.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scheduler.shutdown();
        sc.close();
        System.out.println("Exiting. Bye!");
    }

    private static void fetchAndShow(String city) throws Exception {
        String xml = WeatherFetcher.getWeatherXML(city);
        WeatherData data = XMLParser.parseWeatherData(xml);
        System.out.println(data.toString());

        System.out.print("Save this report? (y/N): ");
        Scanner sc = new Scanner(System.in);
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            String fileName = city.replaceAll("\\s+","_") + "_report.txt";
            ReportExporter.exportToFile(data, fileName);
        }
    }
}
