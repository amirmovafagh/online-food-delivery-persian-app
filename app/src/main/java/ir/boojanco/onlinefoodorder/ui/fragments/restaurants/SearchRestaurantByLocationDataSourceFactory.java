package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;

public class SearchRestaurantByLocationDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, RestaurantList>> searchRestaurantByLocationLiveDataSource = new MutableLiveData<>();
    private RestaurantRepository restaurantRepository;
    private RestaurantDataSourceInterface dataSourceInterface;
    private double latitude;
    private double longitude;

    public SearchRestaurantByLocationDataSourceFactory(RestaurantRepository restaurantRepository, RestaurantDataSourceInterface dataSourceInterface, double latitude, double longitude) {
        this.restaurantRepository = restaurantRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public DataSource create() {
        SearchRestaurantByLocationDataSource searchRestaurantByLocationDataSource = new SearchRestaurantByLocationDataSource(restaurantRepository, dataSourceInterface,latitude, longitude);
        searchRestaurantByLocationLiveDataSource.postValue(searchRestaurantByLocationDataSource);
        return searchRestaurantByLocationDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, RestaurantList>> getSearchRestaurantByLocationLiveDataSource() {
        return searchRestaurantByLocationLiveDataSource;
    }
}
