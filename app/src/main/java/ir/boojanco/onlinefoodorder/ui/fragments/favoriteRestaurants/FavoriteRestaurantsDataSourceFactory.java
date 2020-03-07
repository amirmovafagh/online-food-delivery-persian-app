package ir.boojanco.onlinefoodorder.ui.fragments.favoriteRestaurants;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.FavoriteRestaurants;


public class FavoriteRestaurantsDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, FavoriteRestaurants>> favoriteRestaurantsLiveDataSource = new MutableLiveData<>();
    private UserRepository userRepository;
    private FavoriteRestaurantsDataSourceInterface dataSourceInterface;
    private String authToken;

    public FavoriteRestaurantsDataSourceFactory(UserRepository userRepository, FavoriteRestaurantsDataSourceInterface dataSourceInterface, String authToken) {
        this.userRepository = userRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.authToken = authToken;
    }


    @Override
    public DataSource create() {
        FavoriteRestaurantsDataSource favoriteRestaurantsDataSource = new FavoriteRestaurantsDataSource(userRepository, dataSourceInterface, authToken);
        favoriteRestaurantsLiveDataSource.postValue(favoriteRestaurantsDataSource);
        return favoriteRestaurantsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, FavoriteRestaurants>> getFavoriteRestaurantsLiveDataSource() {
        return favoriteRestaurantsLiveDataSource;
    }
}
