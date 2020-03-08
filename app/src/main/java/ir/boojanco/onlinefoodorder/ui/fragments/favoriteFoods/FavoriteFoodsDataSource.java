package ir.boojanco.onlinefoodorder.ui.fragments.favoriteFoods;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.food.FavoriteFoods;
import ir.boojanco.onlinefoodorder.models.food.FavoriteFoodsResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FavoriteFoodsDataSource extends PageKeyedDataSource<Integer, FavoriteFoods> {
    private String TAG = FavoriteFoodsDataSource.class.getSimpleName();
    private String authToken;
    public static final int PAGE_SIZE = 5;
    public static final int FIRST_PAGE = 1;
    private FavoriteFoodsDataSourceInterface dataSourceInterface;

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    private UserRepository userRepository;


    public FavoriteFoodsDataSource(UserRepository userRepository, FavoriteFoodsDataSourceInterface dataSourceInterface, String authToken) {
        this.userRepository = userRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.authToken = authToken;
    }

    /*
     * Step 2: This method is responsible to load the data initially
     * when app screen is launched for the first time.
     * We are fetching the first page data from the api
     * and passing it via the callback method to the UI.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, FavoriteFoods> callback) {
        dataSourceInterface.onStartedFaveFoodsData();

        Observable<FavoriteFoodsResponse> observable = userRepository.getFavoriteFoodsResponseObservable(authToken, FIRST_PAGE, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<FavoriteFoodsResponse>() {
                @Override
                public void onCompleted() {
                    dataSourceInterface.onSuccessFaveFoodsData();
                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureFaveFoodsData(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.i(TAG, " " + jsonObject.getString("message"));
                        } catch (Exception d) {
                            Log.i(TAG, " " + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(FavoriteFoodsResponse favoriteFoodsResponse) {
                    callback.onResult(favoriteFoodsResponse.getFavoriteFoodsList(), null, FIRST_PAGE + 1);
                }
            });
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, FavoriteFoods> callback) {

        Observable<FavoriteFoodsResponse> observable = userRepository.getFavoriteFoodsResponseObservable(authToken, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<FavoriteFoodsResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureFaveFoodsData(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.i(TAG, " " + jsonObject.getString("message"));
                        } catch (Exception d) {
                            Log.i(TAG, " " + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(FavoriteFoodsResponse favoriteFoodsResponse) {
                    int key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(favoriteFoodsResponse.getFavoriteFoodsList(), key);
                }
            });
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, FavoriteFoods> callback) {

        Observable<FavoriteFoodsResponse> observable = userRepository.getFavoriteFoodsResponseObservable(authToken, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<FavoriteFoodsResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureFaveFoodsData(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.i(TAG," "+jsonObject.getString("message"));
                        } catch (Exception d) {
                            Log.i(TAG, " " + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(FavoriteFoodsResponse favoriteFoodsResponse) {
                    int key = favoriteFoodsResponse != null ? params.key + 1 : null;
                    callback.onResult(favoriteFoodsResponse.getFavoriteFoodsList(), key);
                }
            });
        }
    }
}
