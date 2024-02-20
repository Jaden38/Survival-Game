package Game;

public class GameTime {
    private int day;
    private int hour;
    private int minute;

    public GameTime() {
        this.day = 1;
        this.hour = 0;
        this.minute = 0;
    }

    public void updateTime() {
        minute++;
        if (minute >= 60) {
            minute = 0;
            hour++;
            if (hour >= 24) {
                hour = 0;
                day++;
            }
        }
    }

    public String getTimeString() {
        return "Day " + day + " " + String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }


}
