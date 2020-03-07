package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static ir.boojanco.onlinefoodorder.dagger.App.webServerMediaRoute;

public class FavoriteRestaurants {
    @SerializedName("address")
    private String address;
    @SerializedName("branch")
    private String branch;
    @SerializedName("averageScore")
    private float averageScore;
    @SerializedName("commentCount")
    private int commentCount;
    @SerializedName("id")
    private long id;
    @SerializedName("cover")
    private String cover;
    @SerializedName("logo")
    private String logo;
    @SerializedName("deliveryTime")
    private String deliveryTime;
    @SerializedName("name")
    private String name;
    @SerializedName("tagList")
    private List<String> tagList;
    @SerializedName("working")
    private boolean working;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCover() {
        return webServerMediaRoute+cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLogo() {
        return webServerMediaRoute+logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }
}
