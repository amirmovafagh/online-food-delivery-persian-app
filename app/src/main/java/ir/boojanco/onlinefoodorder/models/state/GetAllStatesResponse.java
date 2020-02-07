package ir.boojanco.onlinefoodorder.models.state;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllStatesResponse {
    @SerializedName("hits")
    private List<AllStatesList> allStatesLists;

    @SerializedName("total")
    private int total;

    public List<AllStatesList> getAllStatesLists() {
        return allStatesLists;
    }

    public void setAllStatesLists(List<AllStatesList> allStatesLists) {
        this.allStatesLists = allStatesLists;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
