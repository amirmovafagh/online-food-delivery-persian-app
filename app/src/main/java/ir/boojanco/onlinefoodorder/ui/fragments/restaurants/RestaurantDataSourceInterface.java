package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

public interface RestaurantDataSourceInterface {
    void onStarted();
    void onSuccess( );
    void onFailure(String error);
}
