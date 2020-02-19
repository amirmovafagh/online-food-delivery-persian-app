package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.List;

import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;

public interface MapDialogInterface {
    void onStarted();
    void onSuccess();
    void onFailure(String Error);
}
