package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantInfoFragmentInterface;

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
        infoFragmentInterface.onSuccess(restaurantInfo.getMenuTypesInfo().getMenuType());

    }


}
