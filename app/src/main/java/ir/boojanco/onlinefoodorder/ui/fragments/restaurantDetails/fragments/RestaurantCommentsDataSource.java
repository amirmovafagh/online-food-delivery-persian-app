package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.GetRestaurantCommentResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantComments;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestaurantCommentsDataSource extends PageKeyedDataSource<Integer, RestaurantComments> {
    private String TAG = RestaurantCommentsDataSource.class.getSimpleName();
    private String authToken;
    private long restarantId;
    public static final int PAGE_SIZE = 10;
    public static final int FIRST_PAGE = 1;
    private RestaurantCommentsDataSourceInterface dataSourceInterface;

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    private RestaurantRepository restaurantRepository;


    public RestaurantCommentsDataSource(RestaurantRepository restaurantRepository, RestaurantCommentsDataSourceInterface dataSourceInterface, String authToken, long restarantId) {
        this.restaurantRepository = restaurantRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.authToken = authToken;
        this.restarantId = restarantId;
    }

    /*
     * Step 2: This method is responsible to load the data initially
     * when app screen is launched for the first time.
     * We are fetching the first page data from the api
     * and passing it via the callback method to the UI.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, RestaurantComments> callback) {
        dataSourceInterface.onStartedRestaurantCommentsData();

        Observable<GetRestaurantCommentResponse> observable = restaurantRepository.getRestaurantCommentResponseObservable(authToken, restarantId, FIRST_PAGE, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetRestaurantCommentResponse>() {
                @Override
                public void onCompleted() {
                    dataSourceInterface.onSuccessRestaurantCommentsData();
                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureRestaurantCommentsData(e.getMessage());
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
                public void onNext(GetRestaurantCommentResponse getRestaurantCommentResponse) {
                    callback.onResult(getRestaurantCommentResponse.getRestaurantComments(), null, FIRST_PAGE + 1);
                }
            });
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, RestaurantComments> callback) {

        Observable<GetRestaurantCommentResponse> observable = restaurantRepository.getRestaurantCommentResponseObservable(authToken, restarantId, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetRestaurantCommentResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureRestaurantCommentsData(e.getMessage());
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
                public void onNext(GetRestaurantCommentResponse getRestaurantCommentResponse) {
                    int key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(getRestaurantCommentResponse.getRestaurantComments(), key);
                }
            });
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, RestaurantComments> callback) {

        Observable<GetRestaurantCommentResponse> observable = restaurantRepository.getRestaurantCommentResponseObservable(authToken, restarantId, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetRestaurantCommentResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureRestaurantCommentsData(e.getMessage());
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
                public void onNext(GetRestaurantCommentResponse getRestaurantCommentResponse) {
                    int key = getRestaurantCommentResponse != null ? params.key + 1 : null;
                    callback.onResult(getRestaurantCommentResponse.getRestaurantComments(), key);
                }
            });
        }
    }
}
