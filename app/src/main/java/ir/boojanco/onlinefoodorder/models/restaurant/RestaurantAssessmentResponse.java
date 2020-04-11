package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

public class RestaurantAssessmentResponse {
    @SerializedName("arrivalTime")
    private float arrivalTime;
    @SerializedName("personnelBehaviour")
    private float personnelBehaviour;
    @SerializedName("foodQuality")
    private float foodQuality;
    @SerializedName("commentCount")
    private int commentCount;
    @SerializedName("orderCount")
    private int orderCount;

    public float getArrivalTime() {
        return arrivalTime;
    }

    public float getPersonnelBehaviour() {
        return personnelBehaviour;
    }

    public float getFoodQuality() {
        return foodQuality;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getOrderCount() {
        return orderCount;
    }
}
