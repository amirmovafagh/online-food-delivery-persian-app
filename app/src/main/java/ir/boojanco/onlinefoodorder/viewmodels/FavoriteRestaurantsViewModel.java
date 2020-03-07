package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.FavoriteRestaurants;
import ir.boojanco.onlinefoodorder.ui.fragments.favoriteRestaurants.FavoriteRestaurantsDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.favoriteRestaurants.FavoriteRestaurantsDataSourceFactory;
import ir.boojanco.onlinefoodorder.ui.fragments.favoriteRestaurants.FavoriteRestaurantsDataSourceInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteRestaurantsFragmentInterface;

public class FavoriteRestaurantsViewModel extends ViewModel implements FavoriteRestaurantsDataSourceInterface {
    private Context context;
    private UserRepository userRepository;
    private FavoriteRestaurantsFragmentInterface fragmentInterface;

    public LiveData<PagedList<FavoriteRestaurants>> userFavoriteRestaurantsPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, FavoriteRestaurants>> userFavoriteRestaurantsPageKeyedDataSourceLiveData;

    public void setFragmentInterface(FavoriteRestaurantsFragmentInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }

    public FavoriteRestaurantsViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public void getFavoriteRestaurants(String authToken) {
        FavoriteRestaurantsDataSourceFactory favoriteRestaurantsDataSourceFactory = new FavoriteRestaurantsDataSourceFactory(userRepository, this, authToken);
        userFavoriteRestaurantsPageKeyedDataSourceLiveData = favoriteRestaurantsDataSourceFactory.getFavoriteRestaurantsLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(FavoriteRestaurantsDataSource.PAGE_SIZE)
                        .build();
        userFavoriteRestaurantsPagedListLiveData = (new LivePagedListBuilder(favoriteRestaurantsDataSourceFactory, config)).build();
    }

    @Override
    public void onStartedFaveRestaurantsData() {
        fragmentInterface.onStarted();
    }

    @Override
    public void onSuccessFaveRestaurantsData() {
        fragmentInterface.onSuccess();
    }

    @Override
    public void onFailureFaveRestaurantsData(String error) {
        fragmentInterface.onFailure(error);
    }
}
