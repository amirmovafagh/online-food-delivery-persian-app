package ir.boojanco.onlinefoodorder.models.food;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static ir.boojanco.onlinefoodorder.dagger.App.webServerMediaRoute;

public class FavoriteFoods {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("details")
    private String details;
    @SerializedName("logo")
    private String logo;
    @SerializedName("point")
    private int point;
    @SerializedName("score")
    private float score;
    @SerializedName("discountPercent")
    private int discountPercent;
    @SerializedName("cost")
    private int cost;
    @SerializedName("commentCount")
    private int commentCount;
    @SerializedName("active")
    private boolean active;
    @SerializedName("foodTypeList")
    private List<String> foodTypeList;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getLogo() {
        return webServerMediaRoute+logo;
    }

    public int getPoint() {
        return point;
    }

    public float getScore() {
        return score;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public String getDiscountedPrice(){
        if(discountPercent > 0){
            double i= 100-discountPercent;
            double percent = (i/100);
            i = cost; //convert int to double
            i = i * percent;
            int disPrice = (int)i;
            return moneyFormat(disPrice);
        }
        return " ";
    }

    public int getCost() {
        return cost;
    }

    public String getCostMoneyFormat(){
        return moneyFormat(cost);
    }

    public int getCommentCount() {
        return commentCount;
    }

    public boolean isActive() {
        return active;
    }

    public List<String> getFoodTypeList() {
        return foodTypeList;
    }

    private String moneyFormat(int cost){
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber+" تومان";
    }
}
