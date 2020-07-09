package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class WalletActivity {
    @SerializedName("cost")
    int cost;

    @SerializedName("date")
    Long date;

    @SerializedName("type")
    String type;

    @SerializedName("description")
    String description;

    public int getCost() {
        return cost;
    }

    public Long getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
