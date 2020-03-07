package ir.boojanco.onlinefoodorder.ui.fragments.favoriteRestaurants;

public interface FavoriteRestaurantsDataSourceInterface {
    void onStartedFaveRestaurantsData();
    void onSuccessFaveRestaurantsData();
    void onFailureFaveRestaurantsData(String error);
}
