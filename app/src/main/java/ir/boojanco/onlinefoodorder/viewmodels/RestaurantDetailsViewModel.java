package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFoodInterface;


public class RestaurantDetailsViewModel extends ViewModel {
    private static final String TAG = RestaurantDetailsViewModel.class.getSimpleName();
    public RestaurantFoodInterface foodInterface;
    public MutableLiveData<String> restaurantCover;
    public MutableLiveData<String> restaurantLogo;
    public MutableLiveData<Float> restaurantAverageScore;
    public MutableLiveData<String> restaurantName;
    public MutableLiveData<String> restaurantTagList;
    private Context context;
    private RestaurantRepository restaurantRepository;

    public RestaurantDetailsViewModel(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        restaurantCover = new MutableLiveData<>();
        restaurantLogo = new MutableLiveData<>();
        restaurantAverageScore = new MutableLiveData<>();
        restaurantName = new MutableLiveData<>();
        restaurantTagList = new MutableLiveData<>();
    }

}
