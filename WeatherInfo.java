public class WeatherInfo {

    private final String time;
    private final String weather;
    private final String waves;

    public WeatherInfo(String time, String weather, String waves) {
        this.time = time;
        this.weather = weather;
        this.waves = waves;
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
}