package ir.boojanco.onlinefoodorder.ui.activities.payment;

public interface PaymentInterface {
    void onStarted();
    void onSuccess();
    void onFailure(String Error);
}
