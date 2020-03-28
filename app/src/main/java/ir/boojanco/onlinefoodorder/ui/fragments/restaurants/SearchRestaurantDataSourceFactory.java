package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;

public class SearchRestaurantDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, RestaurantList>> searchRestaurantLiveDataSource = new MutableLiveData<>();
    private RestaurantRepository restaurantRepository;
    private RestaurantDataSourceInterface dataSourceInterface;
    private String query;
    private String city;
    private String region;

    public SearchRestaurantDataSourceFactory(RestaurantRepository restaurantRepository, RestaurantDataSourceInterface dataSourceInterface, String query, String city, String region) {
        this.restaurantRepository = restaurantRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.query = query;
        this.city = city;
        this.region = region;
    }


    @Override
    public DataSource create() {
        SearchRestaurantDataSource searchRestaurantDataSource = new SearchRestaurantDataSource(restaurantRepository, dataSourceInterface,query, city, region);
        searchRestaurantLiveDataSource.postValue(searchRestaurantDataSource);
        return searchRestaurantDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, RestaurantList>> getSearchRestaurantLiveDataSource() {
        return searchRestaurantLiveDataSource;
    }
}
