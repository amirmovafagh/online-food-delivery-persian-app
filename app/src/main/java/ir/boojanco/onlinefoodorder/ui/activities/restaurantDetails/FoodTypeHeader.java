package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

public class FoodTypeHeader implements ListItemType {
    String foodTypeName;

    public FoodTypeHeader(String foodTypeName) {
        this.foodTypeName = foodTypeName;
    }

    public String getFoodTypeName() {
        return foodTypeName;
    }

    public void setFoodTypeName(String foodTypeName) {
        this.foodTypeName = foodTypeName;
    }

    @Override
    public int getItemType() {
        return ListItemType.TYPE_HEADER;
    }
}
