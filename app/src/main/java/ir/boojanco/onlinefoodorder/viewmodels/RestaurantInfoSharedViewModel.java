package ir.boojanco.onlinefoodorder.viewmodels;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;


public class RestaurantInfoSharedViewModel extends ViewModel {
    public MutableLiveData<RestaurantInfoResponse> infoResponseMutableLiveData ;
    public RestaurantInfoSharedViewModel(){
        infoResponseMutableLiveData = new MutableLiveData<>();
    }


}
