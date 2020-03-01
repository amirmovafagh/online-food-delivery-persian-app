package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

public interface OrdersFragmentInterface {
    void onStarted();
    void onSuccess();
    void onFailure(String error);
}
