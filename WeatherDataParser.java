import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataParser {

    public List<WeatherInfo> parse(String jsonData) {
        List<WeatherInfo> weatherInfoList = new ArrayList<>();

        JSONArray rootArray = new JSONArray(jsonData);
        JSONObject timeSeriesObject = rootArray.getJSONObject(0)
                .getJSONArray("timeSeries").getJSONObject(0);
        JSONArray timeDefinesArray = timeSeriesObject.getJSONArray("timeDefines");
        JSONObject areaObject = timeSeriesObject.getJSONArray("areas").getJSONObject(0);
        JSONArray weathersArray = areaObject.getJSONArray("weathers");
        JSONArray windsArray = areaObject.has("winds") ? areaObject.getJSONArray("winds") : new JSONArray();
        JSONArray wavesArray = areaObject.has("waves") ? areaObject.getJSONArray("waves") : new JSONArray();
        // ---最低気温取得ロジックを修正---
        JSONArray MintempsArray = new JSONArray();
        try {
            // 2つ目のtimeSeriesからtempsMinを探す
            for (int idx = 0; idx < rootArray.length(); idx++) {
                JSONArray tsArr = rootArray.getJSONObject(idx).getJSONArray("timeSeries");
                for (int ts = 0; ts < tsArr.length(); ts++) {
                    JSONObject tsObj = tsArr.getJSONObject(ts);
                    if (tsObj.has("areas")) {
                        JSONArray areas = tsObj.getJSONArray("areas");
                        if (areas.length() > 0 && areas.getJSONObject(0).has("tempsMin")) {
                            MintempsArray = areas.getJSONObject(0).getJSONArray("tempsMin");
                            break;
                        }
                    }
                }
                if (MintempsArray.length() > 0)
                    break;
            }
        } catch (Exception e) {
            // 何もしない（MintempsArrayは空のまま）
        }
        // ---降水確率取得ロジックを追加---
        JSONArray popsArray = new JSONArray();
        try {
            for (int idx = 0; idx < rootArray.length(); idx++) {
                JSONArray tsArr = rootArray.getJSONObject(idx).getJSONArray("timeSeries");
                for (int ts = 0; ts < tsArr.length(); ts++) {
                    JSONObject tsObj = tsArr.getJSONObject(ts);
                    if (tsObj.has("areas")) {
                        JSONArray areas = tsObj.getJSONArray("areas");
                        if (areas.length() > 0 && areas.getJSONObject(0).has("pops")) {
                            popsArray = areas.getJSONObject(0).getJSONArray("pops");
                            break;
                        }
                    }
                }
                if (popsArray.length() > 0)
                    break;
            }
        } catch (Exception e) {
            // 何もしない（popsArrayは空のまま）
        }
        for (int i = 0; i < timeDefinesArray.length(); i++) {
            String time = timeDefinesArray.getString(i);
            String weather = weathersArray.getString(i);

            String waves = wavesArray.length() > i ? wavesArray.getString(i) : "-";
            String windds = windsArray.length() > i ? windsArray.getString(i) : "-";
            weatherInfoList.add(new WeatherInfo(time, weather, waves, windds));

            String waves = wavesArray.getString(i);
            String windds = windsArray.getString(i);
            String Mintemps = MintempsArray.getString(i+1);
            weatherInfoList.add(new WeatherInfo(time, weather, waves,windds,Mintemps));
        }

        return weatherInfoList;
    }

    // エリア名ごとにリストを返す
    public Map<String, List<WeatherInfo>> parseByArea(String jsonData) {
        Map<String, List<WeatherInfo>> areaWeatherMap = new HashMap<>();
        JSONArray rootArray = new JSONArray(jsonData);
        JSONArray timeSeriesArray = rootArray.getJSONObject(0).getJSONArray("timeSeries");
        JSONArray areasArray = timeSeriesArray.getJSONObject(0).getJSONArray("areas");
        JSONArray timeDefinesArray = timeSeriesArray.getJSONObject(0).getJSONArray("timeDefines");
        for (int i = 0; i < areasArray.length(); i++) {
            JSONObject areaObject = areasArray.getJSONObject(i);
            String areaName = "";
            if (areaObject.has("area")) {
                // areaフィールドがJSONObjectの場合
                JSONObject areaObj = areaObject.getJSONObject("area");
                areaName = areaObj.getString("name");
            } else if (areaObject.has("name")) {
                areaName = areaObject.getString("name");
            }
            JSONArray weathersArray = areaObject.getJSONArray("weathers");
            JSONArray windsArray = areaObject.has("winds") ? areaObject.getJSONArray("winds") : new JSONArray();
            JSONArray wavesArray = areaObject.has("waves") ? areaObject.getJSONArray("waves") : new JSONArray();
            List<WeatherInfo> weatherInfoList = new ArrayList<>();
            for (int j = 0; j < timeDefinesArray.length(); j++) {
                String time = timeDefinesArray.getString(j);
                String weather = weathersArray.getString(j);
                String waves = wavesArray.length() > j ? wavesArray.getString(j) : "-";
                String windds = windsArray.length() > j ? windsArray.getString(j) : "-";
                weatherInfoList.add(new WeatherInfo(time, weather, waves, windds));
            }
            areaWeatherMap.put(areaName, weatherInfoList);
        }
        return areaWeatherMap;
    }
}