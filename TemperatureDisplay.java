import java.util.List;

public class TemperatureDisplay {
    public void display(List<TemperatureInfo> TemparetureInfoList) {


        // 各天気情報を表示
        for (TemperatureInfo info : TemparetureInfoList) {
            System.out.println(info.getTime()+" "+info.getTimezone() + ", 最高気温: " + info.getTemperature() + "°C");
        }
    }
}
