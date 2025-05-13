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
        JSONArray weathersArray = timeSeriesObject.getJSONArray("areas").getJSONObject(0)
                .getJSONArray("weathers");

        for (int i = 0; i < timeDefinesArray.length(); i++) {
            String time = timeDefinesArray.getString(i);
            String weather = weathersArray.getString(i);
            weatherInfoList.add(new WeatherInfo(time, weather));
        }

        return weatherInfoList;
    }
}