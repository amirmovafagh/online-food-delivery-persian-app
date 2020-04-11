package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static ir.boojanco.onlinefoodorder.dagger.App.webServerMediaRoute;

public class RestaurantInfoResponse implements Serializable {
    @SerializedName("averageScore")
    private Float averageScore;
    @SerializedName("branch")
    private String branch;
    @SerializedName("cover")
    private String cover;
    @SerializedName("working")
    private boolean working;
    @SerializedName("delivery")
    private boolean delivery;
    @SerializedName("deliveryTime")
    private String deliveryTime;
    @SerializedName("getInPlace")
    private boolean getInPlace;
    @SerializedName("id")
    private long id;
    @SerializedName("logo")
    private String logo;
    @SerializedName("minimumOrder")
    private int minimumOrder;
    @SerializedName("name")
    private String name;
    @SerializedName("packingCost")
    private int packingCost;
    @SerializedName("shippingCostInCloseRegions")
    private int shippingCostInCloseRegions;
    @SerializedName("shippingCostInServiceArea")
    private int shippingCostInServiceArea;
    @SerializedName("taxAndService")
    private int taxAndService;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("region")
    private String region;
    @SerializedName("tagList")
    private List<String> tagList;
    @SerializedName("address")
    private String address;
    @SerializedName("serviceAreaCoordinates")
    private String serviceAreaCoordinates;
    @SerializedName("closeRegionsCoordinates")
    private String closeRegionsCoordinates;
    @SerializedName("lat")
    private Double latitude;
    @SerializedName("lon")
    private Double longitude;

    public int getShippingCostInCloseRegions() {
        return shippingCostInCloseRegions;
    }

    public void setShippingCostInCloseRegions(int shippingCostInCloseRegions) {
        this.shippingCostInCloseRegions = shippingCostInCloseRegions;
    }

    public boolean isWorking() {
        return working;
    }

    public int getShippingCostInServiceArea() {
        return shippingCostInServiceArea;
    }

    public void setShippingCostInServiceArea(int shippingCostInServiceArea) {
        this.shippingCostInServiceArea = shippingCostInServiceArea;
    }

    public String getServiceAreaCoordinates() {
        return serviceAreaCoordinates;
    }

    public void setServiceAreaCoordinates(String serviceAreaCoordinates) {
        this.serviceAreaCoordinates = serviceAreaCoordinates;
    }

    public String getCloseRegionsCoordinates() {
        return closeRegionsCoordinates;
    }

    public void setCloseRegionsCoordinates(String closeRegionsCoordinates) {
        this.closeRegionsCoordinates = closeRegionsCoordinates;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getTaxAndService() {
        return taxAndService;
    }

    public void setTaxAndService(int taxAndService) {
        this.taxAndService = taxAndService;
    }

    public Float getAverageScore() {
        return averageScore;
    }

    public String getBranch() {
        return branch;
    }

    public String getCover() {
        return webServerMediaRoute+cover;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public boolean isGetInPlace() {
        return getInPlace;
    }

    public long getId() {
        return id;
    }

    public String getLogo() {
        return webServerMediaRoute+logo;
    }

    public String getMinimumOrder() {

        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(minimumOrder);
        return " تومان "+formattedNumber;
    }

    public String getName() {
        return name;
    }

    public String getPackingCost() {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(packingCost);
        return " تومان "+formattedNumber;

    }
    public int getPackingCostInt() {

        return packingCost;

    }

    public String getShippingCostInRegion() {

        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(shippingCostInCloseRegions);
        return "داخل محدوده "+formattedNumber+" تومان";

    }

    public String getShippingCostOutRegion() {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(shippingCostInServiceArea);
        return "خارج از محدوده "+formattedNumber+" تومان";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRegion() {
        return region+" ";
    }

    public String getTagList() {
        StringBuilder tagListString= new StringBuilder();
        tagListString.append(" ");
        if(tagList != null)
            for(String t : tagList){
                tagListString.append("(").append(t).append(")").append(" ");
            }
        return tagListString.toString();
    }

    public String getAddress() {
        return address;
    }
}
