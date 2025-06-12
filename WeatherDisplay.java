import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeatherDisplay {

    public void display(List<WeatherInfo> weatherInfoList) {
        System.out.println("日付\t\t天気\t\t波\t\t風\t\t最低気温\t最高気温\t降水確率");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        for (WeatherInfo info : weatherInfoList) {
            LocalDateTime dateTime = LocalDateTime.parse(info.getTime(), inputFormatter);
            System.out.println(dateTime.format(outputFormatter) + " " + info.getWeather() +
                    " 波: " + info.getWaves() +
                    " 風: " + info.getwinds() +
                    " 最低気温: " + info.getMintemps() +
                    " 最高気温: " + info.getMaxtemps() +
                    " 降水確率: " + info.getPops() + "%");
        }
    }
}