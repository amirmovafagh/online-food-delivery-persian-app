package ir.boojanco.onlinefoodorder.ui.fragments.cart;

public interface AddressDataSourceInterface {
    void onStarted();

    void onSuccess();

    void onFailure(String error);

}
