
import java.io.FileWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class WeatherForecastApp {

    private static final String TARGET_URL = "https://www.jma.go.jp/bosai/forecast/data/forecast/270000.json";// 天気予報APIのURL(大阪府)
    private static final String[] Max_tempeture = {
            "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&daily=temperature_2m_max&timezone=Asia%2FTokyo&forecast_days=1",
            "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&daily=temperature_2m_max&timezone=America%2FNew_York&forecast_days=1",
            "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&daily=temperature_2m_max&timezone=Europe%2FLondon&forecast_days=1",
            "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&daily=temperature_2m_max&timezone=Australia%2FSydney&forecast_days=1"};
    /**
     * メイン処理:天気予報の取得と表示を実行します
     * 
     * @param args コマンドライン引数(使用しません)
     */
    public static void main(String[] args) {

        WeatherApiClient apiClient = new WeatherApiClient(TARGET_URL);
        WeatherDataParser parser = new WeatherDataParser();
        WeatherDisplay display = new WeatherDisplay();
        

        try {
            String jsonData = apiClient.fetchWeatherData();
            List<WeatherInfo> weatherInfoList = parser.parse(jsonData);
            display.display(weatherInfoList);

            // HTMLファイル出力
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            try (FileWriter writer = new FileWriter("weather.html")) {
                writer.write("<!DOCTYPE html>\n<html lang=\"ja\">\n<head>\n<meta charset=\"UTF-8\">\n");
                writer.write("<title>天気予報</title>\n");
                writer.write(
                        "<style>table{border-collapse:collapse;}td,th{border:1px solid #ccc;padding:8px;}</style>\n");
                writer.write("</head><body>\n<h1>天気予報</h1>\n<table>\n<tr><th>日付</th><th>天気</th><th>波</th></tr>\n");
                for (WeatherInfo info : weatherInfoList) {
                    String formattedDate = OffsetDateTime.parse(info.getTime(), inputFormatter).format(outputFormatter);
                    writer.write("<tr>");
                    writer.write("<td>" + formattedDate + "</td>");
                    writer.write("<td>" + info.getWeather() + "</td>");
                    writer.write("<td>" + info.getWaves() + "</td>");
                    writer.write("</tr>\n");
                }
                writer.write("</table>\n</body></html>");
            } catch (IOException e) {
                System.out.println("HTMLファイルの書き込みに失敗しました: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("エラーが発生しました: " + e.getMessage());
        }
        for(int i=0;i<Max_tempeture.length;i++){
        TemperatureApiClient TapiClient = new TemperatureApiClient(i,Max_tempeture);
        TemperatureDataParser Tparser = new TemperatureDataParser();
        TemperatureDisplay Tdisplay = new TemperatureDisplay();
        try {
            String jsonData = TapiClient.fetchTemperatureData();
            List<TemperatureInfo> temperatureInfoList = Tparser.parse(jsonData);
            Tdisplay.display(temperatureInfoList);
        } catch (Exception e) {
            System.out.println("エラーが発生しました: " + e.getMessage());
        }
        }
    }
}
// ...既存のコード...