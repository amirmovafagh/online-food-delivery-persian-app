package ir.boojanco.onlinefoodorder.models.stateCity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllCitiesResponse {
    @SerializedName("hits")
    private List<AllCitiesList> allCitiesLists;

    @SerializedName("total")
    private int total;

    public List<AllCitiesList> getAllCitiesLists() {
        return allCitiesLists;
    }

    public void setAllCitiesLists(List<AllCitiesList> allCitiesLists) {
        this.allCitiesLists = allCitiesLists;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
