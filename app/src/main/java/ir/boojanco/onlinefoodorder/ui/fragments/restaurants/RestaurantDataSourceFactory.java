package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantList;

public class RestaurantDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, LastRestaurantList>> restaurantLiveDataSource = new MutableLiveData<>();
    private RestaurantRepository restaurantRepository;
    private RestaurantDataSourceInterface dataSourceInterface;

    public RestaurantDataSourceFactory(RestaurantRepository restaurantRepository, RestaurantDataSourceInterface dataSourceInterface) {
        this.restaurantRepository = restaurantRepository;
        this.dataSourceInterface = dataSourceInterface;
    }


    @Override
    public DataSource create() {
        RestaurantDataSource restaurantDataSource = new RestaurantDataSource(restaurantRepository, dataSourceInterface);
        restaurantLiveDataSource.postValue(restaurantDataSource);
        return restaurantDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, LastRestaurantList>> getRestaurantLiveDataSource() {
        return restaurantLiveDataSource;
    }
}
