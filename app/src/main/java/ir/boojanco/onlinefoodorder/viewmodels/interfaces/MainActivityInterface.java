package ir.boojanco.onlinefoodorder.viewmodels.interfaces;


public interface MainActivityInterface {
    void onStarted();

    void onSuccess(String state, String city);

    void onFailure(String error);
}
