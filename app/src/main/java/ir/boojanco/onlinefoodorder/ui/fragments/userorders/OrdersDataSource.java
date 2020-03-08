package ir.boojanco.onlinefoodorder.ui.fragments.userorders;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;

import ir.boojanco.onlinefoodorder.models.user.GetUserOrdersResponse;
import ir.boojanco.onlinefoodorder.models.user.OrderItem;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrdersDataSource extends PageKeyedDataSource<Integer, OrderItem> {
    private String TAG = OrdersDataSource.class.getSimpleName();
    private String authToken;
    public static final int PAGE_SIZE = 5;
    public static final int FIRST_PAGE = 1;
    private OrdersDataSourceInterface dataSourceInterface;

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    private UserRepository userRepository;


    public OrdersDataSource(UserRepository userRepository, OrdersDataSourceInterface dataSourceInterface, String authToken) {
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
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, OrderItem> callback) {
        dataSourceInterface.onStartedOrdersData();

        Observable<GetUserOrdersResponse> observable = userRepository.getUserOrdersResponseObservable(authToken, FIRST_PAGE, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserOrdersResponse>() {
                @Override
                public void onCompleted() {
                    dataSourceInterface.onSuccessOrdersData();
                }

                @Override
                public void onError(Throwable e) {
                    dataSourceInterface.onFailureOrdersData(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureOrdersData(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.i(TAG," "+jsonObject.getString("message"));
                        } catch (Exception d) {
                            Log.i(TAG," "+d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetUserOrdersResponse getUserOrdersResponse) {
                    callback.onResult(getUserOrdersResponse.getOrderItemList(), null, FIRST_PAGE + 1);
                }
            });
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, OrderItem> callback) {

        Observable<GetUserOrdersResponse> observable = userRepository.getUserOrdersResponseObservable(authToken, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserOrdersResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.i(TAG," 2 "+e.getMessage());
                    dataSourceInterface.onFailureOrdersData(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureOrdersData(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.i(TAG," "+jsonObject.getString("message"));
                        } catch (Exception d) {
                            Log.i(TAG," "+d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetUserOrdersResponse getUserOrdersResponse) {
                    int key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(getUserOrdersResponse.getOrderItemList(), key);
                }
            });
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, OrderItem> callback) {

        Observable<GetUserOrdersResponse> observable = userRepository.getUserOrdersResponseObservable(authToken, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserOrdersResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    dataSourceInterface.onFailureOrdersData(e.getMessage());
                    Log.i(TAG," 3 "+e.getMessage());
                    dataSourceInterface.onFailureOrdersData(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureOrdersData(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.i(TAG," "+jsonObject.getString("message"));
                        } catch (Exception d) {
                            Log.i(TAG," "+d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetUserOrdersResponse getUserOrdersResponse) {
                    int key = getUserOrdersResponse != null ? params.key + 1 : null;
                    callback.onResult(getUserOrdersResponse.getOrderItemList(), key);
                }
            });
        }
    }
}
