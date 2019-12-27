package ir.boojanco.onlinefoodorder.models.food.getAllFood;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;

import static ir.boojanco.onlinefoodorder.dagger.App.webServerMediaRoute;

public class AllFoodList {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("details")
    private String details;
    @SerializedName("logo")
    private String logo;
    @SerializedName("point")
    private int point;
    @SerializedName("discount")
    private int discount;
    @SerializedName("cost")
    private int cost;
    @SerializedName("foodTypeList")
    private List<String> foodTypeList;

    public int getId() {
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

    public int getDiscount() {
        return discount;
    }

    public int getCost() {
        return cost;
    }

    public List<String> getFoodTypeList() {
        return foodTypeList;
    }

}
