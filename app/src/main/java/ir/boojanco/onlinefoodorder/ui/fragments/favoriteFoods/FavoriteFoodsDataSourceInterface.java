package ir.boojanco.onlinefoodorder.ui.fragments.favoriteFoods;

public interface FavoriteFoodsDataSourceInterface {
    void onStartedFaveFoodsData();
    void onSuccessFaveFoodsData();
    void onFailureFaveFoodsData(String error);
}
