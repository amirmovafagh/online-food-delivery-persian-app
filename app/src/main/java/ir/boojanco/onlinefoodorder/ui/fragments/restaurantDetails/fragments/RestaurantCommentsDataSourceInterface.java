package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

public interface RestaurantCommentsDataSourceInterface {
    void onStartedRestaurantCommentsData();
    void onSuccessRestaurantCommentsData();
    void onFailureRestaurantCommentsData(String error);
}
