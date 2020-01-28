package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static ir.boojanco.onlinefoodorder.dagger.App.webServerMediaRoute;

public class RestaurantInfoResponse {
    @SerializedName("averageScore")
    private Float averageScore;
    @SerializedName("branch")
    private String branch;
    @SerializedName("cover")
    private String cover;
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
    @SerializedName("shippingCostInRegion")
    private int shippingCostInRegion;
    @SerializedName("shippingCostOutRegion")
    private int shippingCostOutRegion;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("region")
    private String region;
    @SerializedName("tagList")
    private List<String> tagList;
    @SerializedName("address")
    private String address;

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

    public String getShippingCostInRegion() {

        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(shippingCostInRegion);
        return "داخل محدوده "+formattedNumber+" تومان";

    }

    public String getShippingCostOutRegion() {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(shippingCostOutRegion);
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
