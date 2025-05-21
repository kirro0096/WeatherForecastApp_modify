import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TemperatureDataParser {
    public List<TemperatureInfo> parse(String jsonData) {
        List<TemperatureInfo> temperatureInfoList = new ArrayList<>();

        // Open-Meteo APIのレスポンスはJSONObject
        JSONObject rootObject = new JSONObject(jsonData);

        // timezone取得
        String timezone = rootObject.getString("timezone");

        // dailyオブジェクトからtimeとtemperature_2m_max配列を取得
        JSONObject dailyObject = rootObject.getJSONObject("daily");
        JSONArray timeArray = dailyObject.getJSONArray("time");
        JSONArray tempArray = dailyObject.getJSONArray("temperature_2m_max");

        for (int i = 0; i < timeArray.length(); i++) {
            String time = timeArray.getString(i);
            String temperature = tempArray.get(i).toString();
            // timezoneもTemperatureInfoに渡す場合はコンストラクタを修正してください
            temperatureInfoList.add(new TemperatureInfo(timezone, time, temperature));
        }

        return temperatureInfoList;
    }
}
