package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantResponse;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantDataSourceFactory;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantDataSourceInterface;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.SearchRestaurantDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.SearchRestaurantDataSourceFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFragmentInterface;

public class RestaurantViewModel extends ViewModel implements RestaurantDataSourceInterface {
    private static final String TAG = RestaurantViewModel.class.getSimpleName();

    public RestaurantFragmentInterface restaurantInterface;
    private RestaurantRepository restaurantRepository;
    private Context context;
    public MutableLiveData<RestaurantResponse> responseMutableLiveData;
    public MutableLiveData<String> cityNameLiveData;

    public LiveData<PagedList<RestaurantList>> restaurantPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, RestaurantList>> liveDataSource;

    public RestaurantViewModel(Context context, RestaurantRepository restaurantRepository) {
        responseMutableLiveData = new MutableLiveData<>();
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        cityNameLiveData = new MutableLiveData<>();
    }

    public void getAllRestaurant(String authToken) {
        RestaurantDataSourceFactory restaurantDataSourceFactory = new RestaurantDataSourceFactory(restaurantRepository, this);
        liveDataSource = restaurantDataSourceFactory.getRestaurantLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(RestaurantDataSource.PAGE_SIZE)
                        .build();
        restaurantPagedListLiveData = (new LivePagedListBuilder(restaurantDataSourceFactory, config)).build();
    }

    public void getAllSearchedRestaurant(Object categoryList, Object city, Object restaurantName, Object deliveryFilter,
                                         Object discountFilter, Object servingFilter, Object getInPlaceFilter,
                                         Object latitude, Object longitude, Object sortBy) {
        SearchRestaurantDataSourceFactory searchRestaurantDataSourceFactory = new SearchRestaurantDataSourceFactory(
                restaurantRepository, this, categoryList, city, restaurantName, deliveryFilter, discountFilter,
                servingFilter, getInPlaceFilter, latitude, longitude, sortBy);
        liveDataSource = searchRestaurantDataSourceFactory.getSearchRestaurantLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(SearchRestaurantDataSource.PAGE_SIZE)
                        .build();
        restaurantPagedListLiveData = (new LivePagedListBuilder(searchRestaurantDataSourceFactory, config)).build();
    }

    public void openFilterBottomSheetOnClick(){
        restaurantInterface.openBottomSheet();
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
