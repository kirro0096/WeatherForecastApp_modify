import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class WeatherDisplay {

    public void display(List<WeatherInfo> weatherInfoList) {
        System.out.println("日付\t\t天気\t\t波\t\t風");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        for (WeatherInfo info : weatherInfoList) {
            LocalDateTime dateTime = LocalDateTime.parse(info.getTime(), inputFormatter);
            //System.out.printf("%s\t%s\t%s\t%s\n", info.getTime(), info.getWeather(), info.getWaves(), info.getwinds());
            System.out.println(dateTime.format(outputFormatter) + " " + info.getWeather() + " 波: " + info.getWaves() + "風" + info.getwinds());
        }
    }
}