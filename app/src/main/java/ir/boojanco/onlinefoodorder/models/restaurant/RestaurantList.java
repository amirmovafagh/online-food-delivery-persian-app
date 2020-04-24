package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static ir.boojanco.onlinefoodorder.dagger.App.webServerMediaRoute;

public class RestaurantList {

    @SerializedName("id")
    private long id;
    @SerializedName("commentCount")
    private int commentCount;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("branch")
    private String branch;
    @SerializedName("working")
    private Boolean working;
    @SerializedName("logo")
    private String logo;
    @SerializedName("cover")
    private String cover;
    @SerializedName("deliveryTime")
    private String deliveryTime;
    @SerializedName("averageScore")
    private float averageScore;
    @SerializedName("tagList")
    private List<String> tagList;

    public int getCommentCount() {
        return commentCount;
    }

    public Boolean getWorking() {
        return working;
    }

    public String getBranch() {
        return branch;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLogo() {
        return webServerMediaRoute + logo;
    }

    public double getAverageScore() {
        return Math.round(averageScore * 10) / 10.0;
    }

    public String getCover() {
        return webServerMediaRoute + cover;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public String getTagList() {
        StringBuilder tagListString = new StringBuilder();
        tagListString.append(" ");
        if (tagList != null)
            for (String t : tagList) {
                tagListString.append("(").append(t).append(")").append(" ");
            }
        return tagListString.toString();
    }

}
