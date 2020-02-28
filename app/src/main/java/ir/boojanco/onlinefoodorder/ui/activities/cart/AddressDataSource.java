package ir.boojanco.onlinefoodorder.ui.activities.cart;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.GetUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddressDataSource extends PageKeyedDataSource<Integer, UserAddressList> {
    private String TAG = /*AddressDataSource.class.getSimpleName()*/"AMIR";
    private String authToken;
    public static final int PAGE_SIZE = 5;
    public static final int FIRST_PAGE = 1;
    private AddressDataSourceInterface dataSourceInterface;

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    private UserRepository userRepository;


    public AddressDataSource(UserRepository userRepository, AddressDataSourceInterface dataSourceInterface, String authToken) {
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
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, UserAddressList> callback) {
        dataSourceInterface.onStarted();

        Observable<GetUserAddressResponse> observable = userRepository.getUserAddressResponseObservable(authToken, FIRST_PAGE, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserAddressResponse>() {
                @Override
                public void onCompleted() {
                    dataSourceInterface.onSuccess();
                }

                @Override
                public void onError(Throwable e) {
                    dataSourceInterface.onFailure(e.getMessage());
                    Log.i(TAG," 1 "+e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            dataSourceInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            dataSourceInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetUserAddressResponse getUserAddressResponse) {
                    Log.i(TAG," 1 "+getUserAddressResponse.getTotal());
                    callback.onResult(getUserAddressResponse.getUserAddressLists(), null, FIRST_PAGE + 1);
                }
            });
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, UserAddressList> callback) {

        Observable<GetUserAddressResponse> observable = userRepository.getUserAddressResponseObservable(authToken, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserAddressResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.i(TAG," 2 "+e.getMessage());
                    dataSourceInterface.onFailure(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            dataSourceInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            dataSourceInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetUserAddressResponse getUserAddressResponse) {
                    Log.i(TAG," 2 "+getUserAddressResponse.getTotal());
                    int key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(getUserAddressResponse.getUserAddressLists(), key);
                }
            });
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, UserAddressList> callback) {

        Observable<GetUserAddressResponse> observable = userRepository.getUserAddressResponseObservable(authToken, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserAddressResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    dataSourceInterface.onFailure(e.getMessage());
                    Log.i(TAG," 3 "+e.getMessage());
                    dataSourceInterface.onFailure(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            dataSourceInterface.onFailure(jsonObject.getString("message"));

                        } catch (Exception d) {
                            dataSourceInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetUserAddressResponse getUserAddressResponse) {
                    Log.i(TAG," 3 "+getUserAddressResponse.getTotal());
                    int key = getUserAddressResponse != null ? params.key + 1 : null;
                    callback.onResult(getUserAddressResponse.getUserAddressLists(), key);
                }
            });
        }
    }
}
