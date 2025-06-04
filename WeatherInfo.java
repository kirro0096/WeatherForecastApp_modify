public class WeatherInfo {

    private final String time;
    private final String weather;
    private final String waves;
    private final String winds;
    private final String Mintemps;
    private final String pops; // 降水確率を追加

    public WeatherInfo(String time, String weather, String waves, String winds, String Mintemps, String pops) {
        this.time = time;
        this.weather = weather;
        this.waves = waves;
        this.Mintemps = Mintemps;
        this.winds = winds;
        this.pops = pops; // 追加
    }

    public String getTime() {
        return time;
    }

    public String getWeather() {
        return weather;
    }

    public String getWaves() {
        return waves;
    }

    public String getMintemps() {
        return Mintemps;
    }

    public String getwinds() {
        return winds;
    }

    public String getPops() {
        return pops;
    }
}