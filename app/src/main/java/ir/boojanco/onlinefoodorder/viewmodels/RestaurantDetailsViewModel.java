package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantDetailsInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RestaurantDetailsViewModel extends ViewModel {
    private static final String TAG = RestaurantDetailsViewModel.class.getSimpleName();
    public RestaurantDetailsInterface detailsInterface;

    public MutableLiveData<String> restaurantCover;
    public MutableLiveData<String> restaurantLogo;
    public MutableLiveData<Float> restaurantAverageScore;
    public MutableLiveData<String> restaurantName;
    public MutableLiveData<String> restaurantAddress;
    public MutableLiveData<String> restaurantBranch;
    public MutableLiveData<Boolean> restaurantDelivery;
    public MutableLiveData<String> restaurantDeliveryTime;
    public MutableLiveData<Boolean> restaurantGetInPlace;
    public MutableLiveData<String> restaurantMinimumOrder;
    public MutableLiveData<String> restaurantPackingCost;
    public MutableLiveData<String> restaurantShippingCostInRegion;
    public MutableLiveData<String> restaurantShippingCostOutRegion;
    public MutableLiveData<String> restaurantPhoneNumber;
    public MutableLiveData<String> restaurantRegion;
    public MutableLiveData<String> restaurantTagList;
    public MutableLiveData<Integer> cartItemCount;

    private Context context;
    private long restaurantId = 0;
    private String userAuthToken;
    private RestaurantRepository restaurantRepository;
    private CartDataSource cartDataSource;

    public RestaurantDetailsViewModel(Context context, RestaurantRepository restaurantRepository, CartDataSource cartDataSource) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        this.cartDataSource = cartDataSource;

        cartItemCount = new MutableLiveData<>();
        restaurantCover = new MutableLiveData<>();
        restaurantLogo = new MutableLiveData<>();
        restaurantAverageScore = new MutableLiveData<>();
        restaurantName = new MutableLiveData<>();
        restaurantTagList = new MutableLiveData<>();
        restaurantAddress = new MutableLiveData<>();
        restaurantBranch = new MutableLiveData<>();
        restaurantDelivery = new MutableLiveData<>();
        restaurantDeliveryTime = new MutableLiveData<>();
        restaurantGetInPlace = new MutableLiveData<>();
        restaurantMinimumOrder = new MutableLiveData<>();
        restaurantPackingCost = new MutableLiveData<>();
        restaurantShippingCostInRegion = new MutableLiveData<>();
        restaurantShippingCostOutRegion = new MutableLiveData<>();
        restaurantPhoneNumber = new MutableLiveData<>();
        restaurantRegion = new MutableLiveData<>();
    }


    public void getRestaurantInfo(String authToken, long restaurantId) {
        userAuthToken = authToken;
        Observable<RestaurantInfoResponse> observable = restaurantRepository.getRestaurantInfo(authToken, restaurantId);
        Log.e(TAG, "" + observable);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RestaurantInfoResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "" + e.toString());
                    if (e instanceof NoNetworkConnectionException)
                        detailsInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            detailsInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            detailsInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(RestaurantInfoResponse restaurantInfo) {
                    detailsInterface.onStarted();


                    restaurantCover.setValue(restaurantInfo.getCover());
                    restaurantLogo.setValue(restaurantInfo.getLogo());
                    restaurantAverageScore.setValue(restaurantInfo.getAverageScore());
                    restaurantName.setValue(restaurantInfo.getName());
                    restaurantAddress.setValue(restaurantInfo.getAddress());
                    restaurantBranch.setValue(restaurantInfo.getBranch());
                    restaurantDelivery.setValue(restaurantInfo.isDelivery());
                    restaurantDeliveryTime.setValue(restaurantInfo.getDeliveryTime());
                    restaurantGetInPlace.setValue(restaurantInfo.isGetInPlace());
                    restaurantMinimumOrder.setValue(restaurantInfo.getMinimumOrder());
                    restaurantPackingCost.setValue(restaurantInfo.getPackingCost());
                    restaurantShippingCostInRegion.setValue(restaurantInfo.getShippingCostInRegion());
                    restaurantShippingCostOutRegion.setValue(restaurantInfo.getShippingCostOutRegion());
                    restaurantPhoneNumber.setValue(restaurantInfo.getPhoneNumber());
                    restaurantRegion.setValue(restaurantInfo.getRegion());
                    restaurantTagList.setValue(restaurantInfo.getTagList());

                    detailsInterface.onSuccess(restaurantInfo);
                }
            });
        }
    }

    private void addToFavoriteList() {
        Observable<Response<Void>> observable = restaurantRepository.addRestaurantToFavoriteList(userAuthToken, restaurantId);
        Log.e(TAG, "" + observable);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                @Override
                public void onCompleted() {
                    detailsInterface.onFailure("به پرمزه ها افزوده شد");

                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        detailsInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            detailsInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            detailsInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(Response<Void> voidResponse) {


                }
            });
        }
    }

    private void removeFromFavoriteList() {
        Observable<Response<Void>> observable = restaurantRepository.removeRestaurantFromFavoriteList(userAuthToken, restaurantId);
        Log.e(TAG, "" + observable);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                @Override
                public void onCompleted() {
                    detailsInterface.onFailure("از پرمزه ها حذف شد");
                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        detailsInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            detailsInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            detailsInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(Response<Void> voidResponse) {

                }
            });
        }
    }


    public void onRestaurantFavoriteCheckedChanged(boolean checked) {

        if (checked){
            addToFavoriteList();
        }else {
            removeFromFavoriteList();
        }
    }

}
