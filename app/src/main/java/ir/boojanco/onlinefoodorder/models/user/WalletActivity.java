package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ir.boojanco.onlinefoodorder.util.persianDate.PersianDate;
import ir.boojanco.onlinefoodorder.util.persianDate.PersianDateFormat;

public class WalletActivity {
    @SerializedName("cost")
    int cost;

    @SerializedName("date")
    Long date;

    @SerializedName("type")
    String type;

    @SerializedName("description")
    String description;

    PersianDate pdate;
    PersianDateFormat pdformater;

    public WalletActivity() {
        pdformater = new PersianDateFormat("Y/m/d");
    }

    public int getCost() {
        return cost;
    }

    public String getCostMoneyFormat() {
        return moneyFormat(cost);
    }

    public Long getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getShamsiDate() {
        pdate = new PersianDate(date);
        return pdformater.format(pdate);
    }

    private String moneyFormat(int cost) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber + " تومان";
    }
}
