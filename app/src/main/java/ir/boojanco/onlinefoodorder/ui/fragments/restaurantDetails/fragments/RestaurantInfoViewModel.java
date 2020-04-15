package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.MenuTypesInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantInfoFragmentInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestaurantInfoViewModel extends ViewModel {
    private static final String TAG = RestaurantInfoViewModel.class.getSimpleName();
    private Context context;
    private RestaurantRepository restaurantRepository;

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
    public MutableLiveData<String> restaurantMenuEndTimeLiveData;
    public MutableLiveData<String> restaurantMenuStartTimeLiveData;
    public MutableLiveData<String> restaurantMenuTypeNameLiveData;
    public MutableLiveData<String> restaurantMenuTypeDaysLiveData;
    public RestaurantInfoFragmentInterface infoFragmentInterface;

    public RestaurantInfoViewModel(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;

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
        restaurantMenuEndTimeLiveData = new MutableLiveData<>();
        restaurantMenuStartTimeLiveData = new MutableLiveData<>();
        restaurantMenuTypeNameLiveData = new MutableLiveData<>();
        restaurantMenuTypeDaysLiveData = new MutableLiveData<>();
    }

    public void setRestaurantInfo(RestaurantInfoResponse restaurantInfo) {
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
        getRestaurantMenuTypesInfo(restaurantInfo.getId());
    }

    private void getRestaurantMenuTypesInfo(long restaurantId) {
        Toast.makeText(context, ""+restaurantId, Toast.LENGTH_SHORT).show();
        infoFragmentInterface.onStarted();
        Observable<MenuTypesInfoResponse> observable = restaurantRepository.getMenuTypesInfo(restaurantId);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<MenuTypesInfoResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "" + e.toString());
                    if (e instanceof NoNetworkConnectionException)
                        infoFragmentInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            infoFragmentInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            Log.e(TAG, "" + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(MenuTypesInfoResponse typesInfoResponse) {
                    restaurantMenuStartTimeLiveData.setValue(typesInfoResponse.getMenuType().get(0).getStart());
                    restaurantMenuEndTimeLiveData.setValue(typesInfoResponse.getMenuType().get(0).getEnd());
                    restaurantMenuTypeNameLiveData.setValue(typesInfoResponse.getMenuType().get(0).getName());
                    restaurantMenuTypeDaysLiveData.setValue(typesInfoResponse.getMenuType().get(0).getDaysList());
                }
            });
        }
    }

}
