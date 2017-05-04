package trackerapp.trackerapp.model;

import android.annotation.SuppressLint;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Krystian on 04.05.2017.
 */

public class ActivityModelDTO {

    private TypeModel activityType;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minutes;
    private HashMap<Integer, String> parameters = new HashMap<>();

    public ActivityModelDTO() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
    }

    public TypeModel getActivityType() {
        return activityType;
    }

    public void setActivityType(TypeModel activityType) {
        this.parameters.clear();
        this.activityType = activityType;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public HashMap<Integer, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<Integer, String> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Integer key, String value) {
        this.parameters.put(key, value);
    }

    public String getStartedAt() {
        @SuppressLint("DefaultLocale")
        String startedAt = String.format("%02d:%02d %02d.%02d.%02d", getHour(), getMinutes(), getDay(), getMonth()+1, getYear());

        return startedAt;
    }

}
