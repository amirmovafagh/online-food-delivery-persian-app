package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;

public class SearchRestaurantByCategoryDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, RestaurantList>> searchRestaurantByCategoryLiveDataSource = new MutableLiveData<>();
    private RestaurantRepository restaurantRepository;
    private RestaurantDataSourceInterface dataSourceInterface;
    private String query;
    private String city;

    public SearchRestaurantByCategoryDataSourceFactory(RestaurantRepository restaurantRepository, RestaurantDataSourceInterface dataSourceInterface, String query, String city) {
        this.restaurantRepository = restaurantRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.query = query;
        this.city = city;
    }


    @Override
    public DataSource create() {
        SearchRestaurantByCategoryDataSource searchRestaurantByCategoryDataSource = new SearchRestaurantByCategoryDataSource(restaurantRepository, dataSourceInterface,query, city);
        searchRestaurantByCategoryLiveDataSource.postValue(searchRestaurantByCategoryDataSource);
        return searchRestaurantByCategoryDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, RestaurantList>> getSearchRestaurantByCategoryLiveDataSource() {
        return searchRestaurantByCategoryLiveDataSource;
    }
}
