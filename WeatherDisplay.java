import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeatherDisplay {

    public void display(List<WeatherInfo> weatherInfoList) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        for (WeatherInfo info : weatherInfoList) {
            LocalDateTime dateTime = LocalDateTime.parse(info.getTime(), inputFormatter);
            System.out.println(dateTime.format(outputFormatter) + " " + info.getWeather() + " 波: " + info.getWaves());
        }
    }
}