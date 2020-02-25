package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantList;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantDataSourceFactory;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantDataSourceInterface;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFragmentInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel implements RestaurantDataSourceInterface {
    private static final String TAG = RestaurantViewModel.class.getSimpleName();

    public RestaurantFragmentInterface restaurantInterface;
    private RestaurantRepository restaurantRepository;
    private Context context;
    public MutableLiveData<LastRestaurantResponse> responseMutableLiveData ;

    public LiveData<PagedList<LastRestaurantList>> restaurantPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, LastRestaurantList>> liveDataSource;

    public RestaurantViewModel(Context context, RestaurantRepository restaurantRepository) {
        responseMutableLiveData = new MutableLiveData<>();
        this.context = context;
        this.restaurantRepository = restaurantRepository;

        RestaurantDataSourceFactory restaurantDataSourceFactory = new RestaurantDataSourceFactory(restaurantRepository, this);
        liveDataSource = restaurantDataSourceFactory.getRestaurantLiveDataSource();
        PagedList.Config config =
                (new  PagedList.Config.Builder()
                .setEnablePlaceholders(false)).setPageSize(RestaurantDataSource.PAGE_SIZE)
                .build();
        restaurantPagedListLiveData = (new LivePagedListBuilder(restaurantDataSourceFactory, config)).build();
    }

    public void getAllRestaurant(String authToken){

    }

    @Override
    public void onStarted() {
        restaurantInterface.onStarted();
    }

    @Override
    public void onSuccess() {
        restaurantInterface.onSuccess();
    }

    @Override
    public void onFailure(String error) {
        restaurantInterface.onFailure(error);
    }
}
