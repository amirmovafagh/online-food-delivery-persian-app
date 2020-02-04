package ir.boojanco.onlinefoodorder.models.map;

import com.google.gson.annotations.SerializedName;

public class ReverseFindAddressResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("status_code")
    private int status_code;
    @SerializedName("name_area")
    private String name_area;
    @SerializedName("limitedFullAddress")
    private String limitedFullAddress;
    @SerializedName("localAddress")
    private String localAddress;
    @SerializedName("prefix")
    private String prefix;
    @SerializedName("shortAddress")
    private String shortAddress;

    public String getStatus() {
        return status;
    }

    public int getStatus_code() {
        return status_code;
    }

    public String getName_area() {
        return name_area;
    }

    public String getLimitedFullAddress() {
        return limitedFullAddress;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getShortAddress() {
        return shortAddress;
    }
}