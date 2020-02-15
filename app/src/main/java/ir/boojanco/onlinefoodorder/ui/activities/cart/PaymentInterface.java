package ir.boojanco.onlinefoodorder.ui.activities.cart;

public interface PaymentInterface {
    void onStarted();
    void onSuccess();
    void onFailure(String Error);}
