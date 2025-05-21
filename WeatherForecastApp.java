import java.util.List;

/**
 * 天気予報アプリ - 本体
 * このアプリケーションは、気象庁のweb APIから大阪府の天気予報データを取得して表示します
 * 
 * @author n.katayama
 * @version 1.0
 */
public class WeatherForecastApp {

    /**
     * 気諸王朝の天気予報APIのエンドポイントURL
     * 大阪府の天気予報データを提供します
     */
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