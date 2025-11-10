

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class XMLParser {
    public static WeatherData parseWeatherData(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document doc = builder.parse(stream);

        Element cityElem = (Element) doc.getElementsByTagName("city").item(0);
        String city = cityElem.getAttribute("name");

        Element tempElem = (Element) doc.getElementsByTagName("temperature").item(0);
        double temp = Double.parseDouble(tempElem.getAttribute("value"));

        Element humidityElem = (Element) doc.getElementsByTagName("humidity").item(0);
        int humidity = Integer.parseInt(humidityElem.getAttribute("value"));

        Element pressureElem = (Element) doc.getElementsByTagName("pressure").item(0);
        double pressure = Double.parseDouble(pressureElem.getAttribute("value"));

        Element windSpeedElem = (Element) doc.getElementsByTagName("speed").item(0);
        double windSpeed = Double.parseDouble(windSpeedElem.getAttribute("value"));

        Element weatherElem = (Element) doc.getElementsByTagName("weather").item(0);
        String condition = weatherElem.getAttribute("value");

        return new WeatherData(city, temp, humidity, pressure, windSpeed, condition);
    }
}
