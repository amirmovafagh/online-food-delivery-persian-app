package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuType {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("start")
    private String start;
    @SerializedName("end")
    private String end;
    @SerializedName("days")
    private List<String> days;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public List<String> getDays() {
        return days;
    }

    /*public String getDaysList() {
        StringBuilder daysListString = new StringBuilder();
        daysListString.append(" ");
        if (days != null)
            for (String d : days) {
                daysListString.append(d).append(", ");
            }
        return daysListString.toString();
    }*/
    public String getTypeAndTime() {
        return name + " (" + end + " - " + start + ")";
    }

    public String getDaysList() {
        StringBuilder daysListString = new StringBuilder();
        daysListString.append(" ");
        if (days != null)
            for (int i = 0; i < days.size(); i++) {
                String t = days.get(i);
                if (i == days.size() - 1) {
                    daysListString.append(t);
                } else {
                    daysListString.append(t).append(" • ");
                }

            }
        return daysListString.toString();
    }
}
