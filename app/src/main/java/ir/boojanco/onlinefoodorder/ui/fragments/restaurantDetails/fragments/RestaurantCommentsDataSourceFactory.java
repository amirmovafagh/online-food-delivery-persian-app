package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantComments;


public class RestaurantCommentsDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, RestaurantComments>> restaurantCommentsLiveDataSource = new MutableLiveData<>();
    private RestaurantRepository restaurantRepository;
    private RestaurantCommentsDataSourceInterface dataSourceInterface;
    private String authToken;
    private long restarantId;

    public RestaurantCommentsDataSourceFactory(RestaurantRepository restaurantRepository, RestaurantCommentsDataSourceInterface dataSourceInterface, String authToken, long restarantId) {
        this.restaurantRepository = restaurantRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.authToken = authToken;
        this.restarantId = restarantId;
    }

    @Override
    public DataSource create() {
        RestaurantCommentsDataSource restaurantCommentsDataSource = new RestaurantCommentsDataSource(restaurantRepository, dataSourceInterface, authToken, restarantId);
        restaurantCommentsLiveDataSource.postValue(restaurantCommentsDataSource);
        return restaurantCommentsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, RestaurantComments>> getRestaurantCommentsLiveDataSource() {
        return restaurantCommentsLiveDataSource;
    }
}
