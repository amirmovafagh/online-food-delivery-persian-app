package ir.boojanco.onlinefoodorder.ui.fragments.favoriteFoods;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.food.FavoriteFoods;

public class FavoriteFoodsDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, FavoriteFoods>> favoriteFoodsLiveDataSource = new MutableLiveData<>();
    private UserRepository userRepository;
    private FavoriteFoodsDataSourceInterface dataSourceInterface;
    private String authToken;

    public FavoriteFoodsDataSourceFactory(UserRepository userRepository, FavoriteFoodsDataSourceInterface dataSourceInterface, String authToken) {
        this.userRepository = userRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.authToken = authToken;
    }


    @Override
    public DataSource create() {
        FavoriteFoodsDataSource favoriteFoodsDataSource = new FavoriteFoodsDataSource(userRepository, dataSourceInterface, authToken);
        favoriteFoodsLiveDataSource.postValue(favoriteFoodsDataSource);
        return favoriteFoodsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, FavoriteFoods>> getFavoriteFoodsLiveDataSource() {
        return favoriteFoodsLiveDataSource;
    }
}
