package ir.boojanco.onlinefoodorder.models.food;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static ir.boojanco.onlinefoodorder.dagger.App.webServerMediaRoute;

public class AllFoodList {
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

    public int getCommentCount() {
        return commentCount;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

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

    public int getDiscountPercent() {
        return discountPercent;
    }

    public boolean isActive() {
        return active;
    }

    public int getCost() {
        return cost;
    }

    public List<String> getFoodTypeList() {
        return foodTypeList;
    }

}
