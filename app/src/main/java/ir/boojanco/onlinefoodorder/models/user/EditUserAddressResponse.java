package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class EditUserAddressResponse {

    @SerializedName("exactAddress")
    private String exactAddress;

    @SerializedName("tag")
    private String tag;

    @SerializedName("cityId")
    private long cityId;

    @SerializedName("defaultAddress")
    private boolean defaultAddress;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("region")
    private String region;


    public EditUserAddressResponse(String exactAddress, String tag, long cityId, boolean defaultAddress, double latitude, double longitude, String region) {
        this.exactAddress = exactAddress;
        this.tag = tag;
        this.cityId = cityId;
        this.defaultAddress = defaultAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
    }

    public String getExactAddress() {
        return exactAddress;
    }

    public void setExactAddress(String exactAddress) {
        this.exactAddress = exactAddress;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
