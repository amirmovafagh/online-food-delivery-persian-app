package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.ListItemType;

public class FoodItem implements ListItemType {
    private long id;
    private String name;
    private String details;
    private String logo;
    private int point;
    private int discount;
    private int cost;
    private int count;
    private boolean active;
    private List<String> foodTypeList;

    public FoodItem( ) {

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getCost() {
        return cost;
    }
    public String getStringCost() {
        return moneyFormat(cost);
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDiscountedPrice(){
        if(discount > 0){
            double i= 100-discount;
            double percent = (i/100);
            i = cost; //convert int to double
            i = i * percent;
            int disPrice = (int)i;
            return moneyFormat(disPrice);
        }
        return " ";
    }

    public List<String> getFoodTypeList() {
        return foodTypeList;
    }

    public void setFoodTypeList(List<String> foodTypeList) {
        this.foodTypeList = foodTypeList;
    }

    @Override
    public int getItemType() {
        return ListItemType.TYPE_ITEM;
    }

    private String moneyFormat(int cost){
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber+" تومان";
    }
}
