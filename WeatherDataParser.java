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
        JSONArray wavesArray = areaObject.getJSONArray("waves");
        JSONArray windsArray = areaObject.getJSONArray("winds");

        JSONObject timeSeriesObject2 = rootArray.getJSONObject(1)
                .getJSONArray("timeSeries").getJSONObject(1);
        JSONObject areaObject2 = timeSeriesObject2.getJSONArray("areas").getJSONObject(0);
        JSONArray MintempsArray = areaObject2.getJSONArray("tempsMin");
        for (int i = 0; i < timeDefinesArray.length(); i++) {
            String time = timeDefinesArray.getString(i);
            String weather = weathersArray.getString(i);
            String waves = wavesArray.getString(i);
            String windds = windsArray.getString(i);
            String Mintemps = MintempsArray.getString(i+1);
            weatherInfoList.add(new WeatherInfo(time, weather, waves,windds,Mintemps));
        }

        return weatherInfoList;
    }
}