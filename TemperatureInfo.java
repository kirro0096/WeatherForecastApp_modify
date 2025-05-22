public class TemperatureInfo {
    private final String time;
    private final String temperature;
    private final String timezone;

    public TemperatureInfo(String timezone ,String time, String temperature) {
        this.timezone = timezone;
        this.time = time;
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }
    public String getTimezone() {
        return timezone;
    }
}
