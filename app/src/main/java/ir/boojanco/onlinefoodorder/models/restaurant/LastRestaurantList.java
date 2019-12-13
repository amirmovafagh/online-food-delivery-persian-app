package ir.boojanco.onlinefoodorder.models.restaurant;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.SerializedName;

import ir.boojanco.onlinefoodorder.R;

public class LastRestaurantList {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("logo")
    private String logo;
    @SerializedName("averageScore")
    private int averageScore;

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
        return logo;
    }

    public int getAverageScore() {
        return averageScore;
    }

    // important code for loading image here
    /*@BindingAdapter({ "logo" })
    public static void loadImage(ImageView imageView, String imageURL) {
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(new RequestOptions()
                        .circleCrop())
                .load(imageURL)
                .placeholder(R.drawable.arrow_next_48dp)
                .into(imageView);
    }*/
}
