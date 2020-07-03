package ir.boojanco.onlinefoodorder.ui.fragments.payment;

public interface PaymentInterface {
    void onStarted();

    void onSuccess();

    void onFailure(String Error);

    void tryAgain();
}
