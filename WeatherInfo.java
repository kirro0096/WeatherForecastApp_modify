public class WeatherInfo {

    private final String time;
    private final String weather;
    private final String waves;
    private final String Mintemps;

    public WeatherInfo(String time, String weather, String waves, String Mintemps) {
        this.time = time;
        this.weather = weather;
        this.waves = waves;
        this.Mintemps = Mintemps;
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
}