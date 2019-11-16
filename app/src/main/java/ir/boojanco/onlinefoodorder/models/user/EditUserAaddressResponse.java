package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class EditUserAaddressResponse {//id, address, zipCode, region_id, address_index
    @SerializedName("id")
    private long id;
    @SerializedName("address")
    private String address;
    @SerializedName("zipCode")
    private String zipCode;
    @SerializedName("region_id")
    private long region_id;
    @SerializedName("address_index")
    private int address_index;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getAddress_index() {
        return address_index;
    }

    public void setAddress_index(int address_index) {
        this.address_index = address_index;
    }
}
