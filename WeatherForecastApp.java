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
            "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&daily=temperature_2m_max&timezone=Australia%2FSydney&forecast_days=1" };

    // 近畿地方の都道府県名とAPI URLのペアを追加
    private static final String[][] KINKI_PREFS = {
            { "滋賀県", "https://www.jma.go.jp/bosai/forecast/data/forecast/250000.json" },
            { "京都府", "https://www.jma.go.jp/bosai/forecast/data/forecast/260000.json" },
            { "兵庫県", "https://www.jma.go.jp/bosai/forecast/data/forecast/280000.json" },
            { "奈良県", "https://www.jma.go.jp/bosai/forecast/data/forecast/290000.json" },
            { "和歌山県", "https://www.jma.go.jp/bosai/forecast/data/forecast/300000.json" }
    };

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

            // HTMLファイル出力（大阪府）
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            try (FileWriter writer = new FileWriter("weather.html")) {
                writer.write("<!DOCTYPE html>\n<html lang=\"ja\">\n<head>\n<meta charset=\"UTF-8\">\n");
                writer.write("<title>天気予報</title>\n");
                writer.write(
                        "<style>table{border-collapse:collapse;}td,th{border:1px solid #ccc;padding:8px;}</style>\n");
                writer.write("</head><body>\n<h1>天気予報</h1>\n");
                writer.write(
                        "<h2>大阪府</h2>\n<table>\n<tr><th>日付</th><th>天気</th><th>波</th><th>風</th><th>最低気温</th></tr>\n");
                for (WeatherInfo info : weatherInfoList) {
                    String formattedDate = OffsetDateTime.parse(info.getTime(), inputFormatter).format(outputFormatter);
                    writer.write("<tr>");
                    writer.write("<td>" + formattedDate + "</td>");
                    writer.write("<td>" + info.getWeather() + "</td>");
                    writer.write("<td>" + info.getWaves() + "</td>");
                    writer.write("<td>" + info.getwinds() + "</td>");
                    writer.write("<td>" + (info.getMintemps() != null ? info.getMintemps() : "-") + "</td>");
                    writer.write("</tr>\n");
                }
                writer.write("</table>\n");

                // 近畿地方の他県も追加
                for (String[] pref : KINKI_PREFS) {
                    String prefName = pref[0];
                    String url = pref[1];
                    WeatherApiClient prefApiClient = new WeatherApiClient(url);
                    WeatherDataParser prefParser = new WeatherDataParser();
                    String prefJson = prefApiClient.fetchWeatherData();
                    // 京都・滋賀は南部・北部で分けて出力
                    if (prefName.equals("京都府") || prefName.equals("滋賀県")) {
                        java.util.Map<String, java.util.List<WeatherInfo>> areaMap = prefParser.parseByArea(prefJson);
                        for (String area : areaMap.keySet()) {
                            String areaTitle = prefName + area;
                            List<WeatherInfo> areaWeatherInfoList = areaMap.get(area);
                            System.out.println("\n【" + areaTitle + "】");
                            display.display(areaWeatherInfoList);
                            // 波情報の有無を判定
                            boolean hasWave = false;
                            for (WeatherInfo info : areaWeatherInfoList) {
                                if (info.getWaves() != null && !info.getWaves().equals("-")) {
                                    hasWave = true;
                                    break;
                                }
                            }
                            writer.write("<h2>" + areaTitle + "</h2>\n<table>\n<tr><th>日付</th><th>天気</th>");
                            if (hasWave) {
                                writer.write("<th>波</th>");
                            }
                            writer.write("<th>風</th><th>最低気温</th></tr>\n");
                            for (WeatherInfo info : areaWeatherInfoList) {
                                String formattedDate = OffsetDateTime.parse(info.getTime(), inputFormatter)
                                        .format(outputFormatter);
                                writer.write("<tr>");
                                writer.write("<td>" + formattedDate + "</td>");
                                writer.write("<td>" + info.getWeather() + "</td>");
                                if (hasWave) {
                                    writer.write("<td>" + info.getWaves() + "</td>");
                                }
                                writer.write("<td>" + info.getwinds() + "</td>");
                                writer.write(
                                        "<td>" + (info.getMintemps() != null ? info.getMintemps() : "-") + "</td>");
                                writer.write("</tr>\n");
                            }
                            writer.write("</table>\n");
                        }
                    } else {
                        List<WeatherInfo> prefWeatherInfoList = prefParser.parse(prefJson);
                        System.out.println("\n【" + prefName + "】");
                        display.display(prefWeatherInfoList);
                        boolean hasWave = false;
                        for (WeatherInfo info : prefWeatherInfoList) {
                            if (info.getWaves() != null && !info.getWaves().equals("-")) {
                                hasWave = true;
                                break;
                            }
                        }
                        writer.write("<h2>" + prefName + "</h2>\n<table>\n<tr><th>日付</th><th>天気</th>");
                        if (hasWave) {
                            writer.write("<th>波</th>");
                        }
                        writer.write("<th>風</th><th>最低気温</th></tr>\n");
                        for (WeatherInfo info : prefWeatherInfoList) {
                            String formattedDate = OffsetDateTime.parse(info.getTime(), inputFormatter)
                                    .format(outputFormatter);
                            writer.write("<tr>");
                            writer.write("<td>" + formattedDate + "</td>");
                            writer.write("<td>" + info.getWeather() + "</td>");
                            if (hasWave) {
                                writer.write("<td>" + info.getWaves() + "</td>");
                            }
                            writer.write("<td>" + info.getwinds() + "</td>");
                            writer.write("<td>" + (info.getMintemps() != null ? info.getMintemps() : "-") + "</td>");
                            writer.write("</tr>\n");
                        }
                        writer.write("</table>\n");
                    }
                }

                // 最高気温情報のHTML出力処理
                boolean tempTableHeaderWritten = false;
                StringBuilder tempTableRows = new StringBuilder();
                for (int i = 0; i < Max_tempeture.length; i++) {
                    TemperatureApiClient TapiClient = new TemperatureApiClient(i, Max_tempeture);
                    TemperatureDataParser Tparser = new TemperatureDataParser();
                    TemperatureDisplay Tdisplay = new TemperatureDisplay();
                    try {
                        String tempJsonData = TapiClient.fetchTemperatureData();
                        List<TemperatureInfo> temperatureInfoList = Tparser.parse(tempJsonData);
                        Tdisplay.display(temperatureInfoList);

                        // タイムゾーン→日本語表記のマップ
                        java.util.Map<String, String> timezoneMap = new java.util.HashMap<>();
                        timezoneMap.put("Asia/Tokyo", "日本（東京）");
                        timezoneMap.put("America/New_York", "アメリカ（ニューヨーク）");
                        timezoneMap.put("Europe/London", "イギリス（ロンドン）");
                        timezoneMap.put("Australia/Sydney", "オーストラリア（シドニー）");

                        if (!tempTableHeaderWritten) {
                            tempTableRows.append(
                                    "<h2>最高気温情報</h2>\n<table>\n<tr><th>日付</th><th>国・都市</th><th>最高気温(°C)</th></tr>\n");
                            tempTableHeaderWritten = true;
                        }
                        for (TemperatureInfo info : temperatureInfoList) {
                            String jpTimezone = timezoneMap.getOrDefault(info.getTimezone(), info.getTimezone());
                            tempTableRows.append("<tr>");
                            tempTableRows.append("<td>" + info.getTime() + "</td>");
                            tempTableRows.append("<td>" + jpTimezone + "</td>");
                            tempTableRows.append("<td>" + info.getTemperature() + "</td>");
                            tempTableRows.append("</tr>\n");
                        }
                    } catch (Exception e) {
                        System.out.println("エラーが発生しました: " + e.getMessage());
                    }
                }
                if (tempTableHeaderWritten) {
                    tempTableRows.append("</table>\n");
                    writer.write(tempTableRows.toString());
                }
                writer.write("</body></html>");
            } catch (IOException e) {
                System.out.println("HTMLファイルの書き込みに失敗しました: " + e.getMessage());
            }
            // weather.htmlを書き終えた直後にブラウザで自動表示
            try {
                java.awt.Desktop.getDesktop().browse(new java.io.File("weather.html").toURI());
            } catch (Exception e) {
                System.out.println("ブラウザの自動起動に失敗しました: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("エラーが発生しました: " + e.getMessage());
        }
    }
}