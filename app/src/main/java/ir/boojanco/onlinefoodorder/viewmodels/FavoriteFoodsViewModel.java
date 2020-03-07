package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.food.FavoriteFoods;
import ir.boojanco.onlinefoodorder.ui.fragments.favoriteFoods.FavoriteFoodsDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.favoriteFoods.FavoriteFoodsDataSourceFactory;
import ir.boojanco.onlinefoodorder.ui.fragments.favoriteFoods.FavoriteFoodsDataSourceInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteFoodsFragmentInterface;

public class FavoriteFoodsViewModel extends ViewModel implements FavoriteFoodsDataSourceInterface {
    private Context context;
    private UserRepository userRepository;
    private FavoriteFoodsFragmentInterface fragmentInterface;

    public LiveData<PagedList<FavoriteFoods>> userFavoriteFoodsPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, FavoriteFoods>> userFavoriteFoodsPageKeyedDataSourceLiveData;

    public void setFragmentInterface(FavoriteFoodsFragmentInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }

    public FavoriteFoodsViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public void getFavoriteFoods(String authToken) {
        FavoriteFoodsDataSourceFactory favoriteFoodsDataSourceFactory = new FavoriteFoodsDataSourceFactory(userRepository, this, authToken);
        userFavoriteFoodsPageKeyedDataSourceLiveData = favoriteFoodsDataSourceFactory.getFavoriteFoodsLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(FavoriteFoodsDataSource.PAGE_SIZE)
                        .build();
        userFavoriteFoodsPagedListLiveData = (new LivePagedListBuilder(favoriteFoodsDataSourceFactory, config)).build();
    }

    @Override
    public void onStartedFaveFoodsData() {
        fragmentInterface.onStarted();
    }

    @Override
    public void onSuccessFaveFoodsData() {
        fragmentInterface.onSuccess();
    }

    @Override
    public void onFailureFaveFoodsData(String error) {
        fragmentInterface.onFailure(error);
    }
}
