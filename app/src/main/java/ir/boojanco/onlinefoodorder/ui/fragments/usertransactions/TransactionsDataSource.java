package ir.boojanco.onlinefoodorder.ui.fragments.usertransactions;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.WalletActivitiesResponse;
import ir.boojanco.onlinefoodorder.models.user.WalletActivity;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransactionsDataSource extends PageKeyedDataSource<Integer, WalletActivity> {
    private String TAG = TransactionsDataSource.class.getSimpleName();
    private String authToken;
    public static final int PAGE_SIZE = 5;
    public static final int FIRST_PAGE = 1;
    private TransactionsDataSourceInterface dataSourceInterface;

    /*
     * Step 1: Initialize the restApiFactory.
     * The networkState and initialLoading variables
     * are for updating the UI when data is being fetched
     * by displaying a progress bar
     */
    private UserRepository userRepository;


    public TransactionsDataSource(UserRepository userRepository, TransactionsDataSourceInterface dataSourceInterface, String authToken) {
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
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, WalletActivity> callback) {
        dataSourceInterface.onStartedTransactionsData();

        Observable<WalletActivitiesResponse> observable = userRepository.getWalletActivities(authToken, FIRST_PAGE, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<WalletActivitiesResponse>() {
                @Override
                public void onCompleted() {
                    dataSourceInterface.onSuccessTransactionsData();
                }

                @Override
                public void onError(Throwable e) {
                    dataSourceInterface.onFailureTransactionsData(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureTransactionsData(e.getMessage());
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
                public void onNext(WalletActivitiesResponse response) {
                    callback.onResult(response.getWalletItemList(), null, FIRST_PAGE + 1);
                }
            });
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, WalletActivity> callback) {

        Observable<WalletActivitiesResponse> observable = userRepository.getWalletActivities(authToken, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<WalletActivitiesResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                    dataSourceInterface.onFailureTransactionsData(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureTransactionsData(e.getMessage());
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
                public void onNext(WalletActivitiesResponse response) {
                    int key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(response.getWalletItemList(), key);
                }
            });
        }
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, WalletActivity> callback) {

        Observable<WalletActivitiesResponse> observable = userRepository.getWalletActivities(authToken, params.key, PAGE_SIZE);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<WalletActivitiesResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        dataSourceInterface.onFailureTransactionsData(e.getMessage());
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
                public void onNext(WalletActivitiesResponse response) {
                    int key = response != null ? params.key + 1 : null;
                    callback.onResult(response.getWalletItemList(), key);
                }
            });
        }
    }
}
