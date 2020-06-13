package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

public interface RestaurantFragmentInterface {
    void onStarted();
    void onSuccess();
    void onFailure(String error);
    void openBottomSheet();
}
