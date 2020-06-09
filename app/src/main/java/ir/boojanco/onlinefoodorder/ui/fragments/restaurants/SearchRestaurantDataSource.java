package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import org.json.JSONObject;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchRestaurantDataSource extends PageKeyedDataSource<Integer, RestaurantList> {
    private static final String TAG = SearchRestaurantDataSource.class.getSimpleName();
    public static final int PAGE_SIZE = 5;
    public static final int FIRST_PAGE = 1;
    private String restaurantName;
    private String city;
    private ArrayList<String> categoryList;
    private boolean discountFilter  ;
    private boolean deliveryFilter  ;
    private boolean servingFilter   ;
    private boolean getInPlaceFilter;
    private double latitude;
    private double longitude;
    private int sortBy = 0;
    private RestaurantDataSourceInterface dataSourceInterface;

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    private RestaurantRepository restaurantRepository;


    public SearchRestaurantDataSource(RestaurantRepository restaurantRepository, RestaurantDataSourceInterface dataSourceInterface, ArrayList<String> categoryList, String city, String restaurantName, boolean deliveryFilter,
                                      boolean discountFilter, boolean servingFilter, boolean getInPlaceFilter,
                                      double latitude, double longitude, int sortBy) {
        this.restaurantRepository = restaurantRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.restaurantName = restaurantName;
        this.city = city;
        this.discountFilter = discountFilter;
        this.deliveryFilter = deliveryFilter;
        this.servingFilter = servingFilter;
        this.getInPlaceFilter = getInPlaceFilter;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sortBy = sortBy;
        this.categoryList = categoryList;
    }

    /*
     * Step 2: This method is responsible to load the data initially
     * when app screen is launched for the first time.
     * We are fetching the first page data from the api
     * and passing it via the callback method to the UI.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, RestaurantList> callback) {
        dataSourceInterface.onStarted();

        Observable<RestaurantResponse> observable = restaurantRepository.searchRestaurantObservable(categoryList, city, restaurantName, deliveryFilter, discountFilter,
                servingFilter, getInPlaceFilter, latitude, longitude, FIRST_PAGE, PAGE_SIZE, sortBy);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RestaurantResponse>() {
                @Override
                public void onCompleted() {
                    dataSourceInterface.onSuccess();
                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailure(e.getMessage());
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
                public void onNext(RestaurantResponse restaurantResponse) {
                    callback.onResult(restaurantResponse.getRestaurantsList(), null, FIRST_PAGE + 1);
                }
            });
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, RestaurantList> callback) {

        Observable<RestaurantResponse> observable = restaurantRepository.searchRestaurantObservable(categoryList, city, restaurantName, deliveryFilter, discountFilter,
                servingFilter, getInPlaceFilter, latitude, longitude, params.key, PAGE_SIZE, sortBy);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RestaurantResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailure(e.getMessage());
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
                public void onNext(RestaurantResponse restaurantResponse) {
                    int key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(restaurantResponse.getRestaurantsList(), key);
                }
            });
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, RestaurantList> callback) {

        Observable<RestaurantResponse> observable = restaurantRepository.searchRestaurantObservable(categoryList, city, restaurantName, deliveryFilter, discountFilter,
                servingFilter, getInPlaceFilter, latitude, longitude, params.key, PAGE_SIZE, sortBy);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RestaurantResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailure(e.getMessage());
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
                public void onNext(RestaurantResponse restaurantResponse) {
                    int key = restaurantResponse != null ? params.key + 1 : null;
                    callback.onResult(restaurantResponse.getRestaurantsList(), key);
                }
            });
        }
    }
}
