import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class TemperatureApiClient {
    private final String[] apiUrl;
    int i;

    public TemperatureApiClient(int i,String[] apiUrl) {
        this.apiUrl = apiUrl;
        this.i = i;
    }

    public String fetchTemperatureData() throws Exception {
    Exception lastException = null;
        try {
            URI uri = new URI(apiUrl[i]);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder responseBody = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBody.append(line);
                    }
                }
                return responseBody.toString();
            }
        } catch (Exception e) {
            lastException = e;
            // 次のURLを試す
        }
    throw new Exception("全てのURLで天気データの取得に失敗しました", lastException);
}
}
