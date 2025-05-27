public class WeatherInfo {

    private final String time;
    private final String weather;
    private final String waves;
    private final String winds;
    public WeatherInfo(String time, String weather, String waves, String winds) {
        this.time = time;
        this.weather = weather;
        this.waves = waves;
        this.winds = winds;
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

    public String getwinds() {
        return winds;
    }
}