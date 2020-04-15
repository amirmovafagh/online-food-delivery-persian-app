package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class UserPointRestaurantClubResponse {
    @SerializedName("points")
    private int points;

    public int getPoints() {
        return points;
    }
}
