package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

public interface RestaurantCommentsDataSourceInterface {
    void onStartedRestaurantCommentsData();
    void onSuccessRestaurantCommentsData();
    void onFailureRestaurantCommentsData(String error);
}
