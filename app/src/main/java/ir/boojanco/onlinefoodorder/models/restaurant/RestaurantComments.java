package ir.boojanco.onlinefoodorder.models.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.boojanco.onlinefoodorder.util.persianDate.PersianDate;
import ir.boojanco.onlinefoodorder.util.persianDate.PersianDateFormat;

public class RestaurantComments {

    @SerializedName("commentId")
    private long commentId;
    @SerializedName("date")
    private long date;
    @SerializedName("user")
    private String user;
    @SerializedName("context")
    private String userComment;
    @SerializedName("averageScore")
    private float averageScore;
    @SerializedName("foods")
    private List<String> foods;
    PersianDate pdate;
    PersianDateFormat pdformater;

    public RestaurantComments() {
        pdformater = new PersianDateFormat("Y/m/d");
    }
    public String getShamsiDate() {
        pdate = new PersianDate(date);
        return pdformater.format(pdate);
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public List<String> getFoods() {
        return foods;
    }

    public void setFoods(List<String> foods) {
        this.foods = foods;
    }

    public PersianDate getPdate() {
        return pdate;
    }

    public void setPdate(PersianDate pdate) {
        this.pdate = pdate;
    }

    public PersianDateFormat getPdformater() {
        return pdformater;
    }

    public void setPdformater(PersianDateFormat pdformater) {
        this.pdformater = pdformater;
    }

    public String getFoodList() {
        StringBuilder tagListString= new StringBuilder();
        tagListString.append(" ");
        if(foods != null)
            for(String t : foods){
                tagListString.append("(").append(t).append(")").append(" ");
            }
        return tagListString.toString();
    }
}
