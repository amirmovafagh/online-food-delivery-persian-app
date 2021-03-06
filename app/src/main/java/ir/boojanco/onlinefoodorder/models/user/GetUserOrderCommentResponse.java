package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class GetUserOrderCommentResponse {
    @SerializedName("orderId")
    private long orderId;
    @SerializedName("context")
    private String context;
    @SerializedName("foodQuality")
    private Float foodQuality;
    @SerializedName("arrivalTime")
    private Float arrivalTime;
    @SerializedName("systemEx")
    private Float systemEx;
    @SerializedName("personnelBehaviour")
    private Float personnelBehaviour;

    public GetUserOrderCommentResponse(long orderId, String context, Float foodQuality, Float arrivalTime, Float systemEx, Float personnelBehaviour) {
        this.orderId = orderId;
        this.context = context;
        this.foodQuality = foodQuality;
        this.arrivalTime = arrivalTime;
        this.systemEx = systemEx;
        this.personnelBehaviour = personnelBehaviour;
    }

    public Float getPersonnelBehaviour() {
        return personnelBehaviour;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getContext() {
        return context;
    }

    public Float getFoodQuality() {
        return foodQuality;
    }

    public Float getArrivalTime() {
        return arrivalTime;
    }

    public Float getSystemEx() {
        return systemEx;
    }
}
