package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantList;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestaurantDataSource extends PageKeyedDataSource<Integer, LastRestaurantList> {
    public static final int PAGE_SIZE = 10;
    public static final int FIRST_PAGE = 1;

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    private RestaurantRepository restaurantRepository;


    public RestaurantDataSource(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;


    }

    /*
     * Step 2: This method is responsible to load the data initially
     * when app screen is launched for the first time.
     * We are fetching the first page data from the api
     * and passing it via the callback method to the UI.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, LastRestaurantList> callback) {


        Observable<LastRestaurantResponse> observable = restaurantRepository.getLastRestaurant(FIRST_PAGE, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LastRestaurantResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
//                        restaurantInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();

                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());

//                            restaurantInterface.onFailure(jsonObject.getString("message"));


                            } catch (Exception d) {
//                            restaurantInterface.onFailure(d.getMessage());
                            }
                        }
                }

                @Override
                public void onNext(LastRestaurantResponse lastRestaurantResponse) {
                    callback.onResult(lastRestaurantResponse.getRestaurantsList(), null, FIRST_PAGE + 1);
                }
            });
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, LastRestaurantList> callback) {

        Observable<LastRestaurantResponse> observable = restaurantRepository.getLastRestaurant(params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LastRestaurantResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
//                        restaurantInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();

                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());

//                            restaurantInterface.onFailure(jsonObject.getString("message"));


                            } catch (Exception d) {
//                            restaurantInterface.onFailure(d.getMessage());
                            }
                        }
                }

                @Override
                public void onNext(LastRestaurantResponse lastRestaurantResponse) {
                    int key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(lastRestaurantResponse.getRestaurantsList(), key);
                }
            });
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, LastRestaurantList> callback) {

        Observable<LastRestaurantResponse> observable = restaurantRepository.getLastRestaurant(params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LastRestaurantResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
//                        restaurantInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();

                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());

//                            restaurantInterface.onFailure(jsonObject.getString("message"));


                            } catch (Exception d) {
//                            restaurantInterface.onFailure(d.getMessage());
                            }
                        }
                }

                @Override
                public void onNext(LastRestaurantResponse lastRestaurantResponse) {
                    int key = lastRestaurantResponse != null ? params.key + 1 : null;
                    callback.onResult(lastRestaurantResponse.getRestaurantsList(), key);
                }
            });
        }
    }
}
