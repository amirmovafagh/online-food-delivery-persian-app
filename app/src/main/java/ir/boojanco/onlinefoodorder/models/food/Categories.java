package ir.boojanco.onlinefoodorder.models.food;

import com.google.gson.annotations.SerializedName;

class Categories {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}
