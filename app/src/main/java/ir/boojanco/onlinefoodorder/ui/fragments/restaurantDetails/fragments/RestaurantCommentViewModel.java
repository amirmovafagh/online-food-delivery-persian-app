package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantComments;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantCommentFragmentInterface;

public class RestaurantCommentViewModel extends ViewModel implements RestaurantCommentsDataSourceInterface {
    private Context context;
    private RestaurantRepository restaurantRepository;
    private RestaurantCommentFragmentInterface fragmentInterface;

    public void setFragmentInterface(RestaurantCommentFragmentInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }
    public MutableLiveData<Boolean> waitingResponseAnimateLivedata;
    public LiveData<PagedList<RestaurantComments>> restaurantCommentsPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, RestaurantComments>> restaurantCommentsPageKeyedDataSourceLiveData;

    public RestaurantCommentViewModel(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        waitingResponseAnimateLivedata = new MutableLiveData<>();
        waitingResponseAnimateLivedata.setValue(true);
    }

    public void getRestaurantComments(String authToken, long restaurantId) {

        RestaurantCommentsDataSourceFactory restaurantCommentsDataSourceFactory = new RestaurantCommentsDataSourceFactory(restaurantRepository, this, authToken, restaurantId);
        restaurantCommentsPageKeyedDataSourceLiveData = restaurantCommentsDataSourceFactory.getRestaurantCommentsLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(RestaurantCommentsDataSource.PAGE_SIZE)
                        .build();
        restaurantCommentsPagedListLiveData = (new LivePagedListBuilder(restaurantCommentsDataSourceFactory, config)).build();
    }

    @Override
    public void onStartedRestaurantCommentsData() {
        fragmentInterface.onStarted();
    }

    @Override
    public void onSuccessRestaurantCommentsData() {
        fragmentInterface.onSuccess();
    }

    @Override
    public void onFailureRestaurantCommentsData(String error) {
        fragmentInterface.onFailure(error);
    }
}
