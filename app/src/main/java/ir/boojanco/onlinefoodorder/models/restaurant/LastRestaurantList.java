package ir.boojanco.onlinefoodorder.models.restaurant;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;

import static ir.boojanco.onlinefoodorder.dagger.App.webServerMediaRoute;

public class LastRestaurantList {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLogo() {
        return webServerMediaRoute+logo;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public String getCover() {
        return webServerMediaRoute+cover;
    }

    public String getDeliveryTime() {
        return deliveryTime;
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

    // important code for loading image here
    @BindingAdapter({ "image" })
    public static void loadImage(ImageView imageView, String imageURL) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(imageView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(new RequestOptions())
                .load(imageURL)
                .placeholder(circularProgressDrawable)
                .into(imageView);
    }
}
