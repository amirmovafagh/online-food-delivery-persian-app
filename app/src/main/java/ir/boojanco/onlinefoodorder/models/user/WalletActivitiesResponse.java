package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletActivitiesResponse {
    @SerializedName("hits")
    private List<WalletActivity> walletItemList;
    @SerializedName("total")
    private int total;

    public List<WalletActivity> getWalletItemList() {
        return walletItemList;
    }

    public int getTotal() {
        return total;
    }
}
