package ir.boojanco.onlinefoodorder.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;

public class RestaurantInfoSharedViewModel extends ViewModel {
    public MutableLiveData<RestaurantInfoResponse> infoResponseMutableLiveData ;
    public MutableLiveData<Integer> userRestaurantClubPointLivedata;
    public RestaurantInfoSharedViewModel(){
        infoResponseMutableLiveData = new MutableLiveData<>();
        userRestaurantClubPointLivedata = new MutableLiveData<>();
        userRestaurantClubPointLivedata.setValue(0);
    }
}
