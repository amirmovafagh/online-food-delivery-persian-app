package ir.boojanco.onlinefoodorder.ui.activities.cart;

public interface AddressDataSourceInterface {
    void onStarted();
    void onSuccess();
    void onFailure(String error);
}
