package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class AddAddressResponse {
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("address")
    private String address;
    @SerializedName("zipCode")
    private String zipCode;
    @SerializedName("region_id")
    private long region_id;
    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private String code;

    public AddAddressResponse(String mobile, String address, String zipCode, long region_id) {
        this.mobile = mobile;
        this.address = address;
        this.zipCode = zipCode;
        this.region_id = region_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public long getRegion_id() {
        return region_id;
    }

    public void setRegion_id(long region_id) {
        this.region_id = region_id;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
