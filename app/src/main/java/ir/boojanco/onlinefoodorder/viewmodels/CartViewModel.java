package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.RestaurantPackageItem;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllCitiesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.models.user.GetUserAddressResponse;
import ir.boojanco.onlinefoodorder.ui.activities.cart.FinalPaymentPrice;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.CartInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.MapDialogInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Subscriber;

public class CartViewModel extends ViewModel {
    private String TAG = this.getClass().getSimpleName();

    public CartInterface cartInterface;
    public MapDialogInterface mapDialogInterface;
    private Context context;
    private CartDataSource cartDataSource;
    private CompositeDisposable compositeDisposableGetAllItems;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    private long cityId;
    private long stateId;
    private RestaurantPackageItem packageItem;
    private RestaurantInfoResponse restaurantInfo;
    private List<CartItem> cartItems;
    private List<AllStatesList> statesLists;
    private List<AllCitiesList> citiesLists;
    private ArrayList<FinalPaymentPrice> finalPaymentPrices;


    public MutableLiveData<Long> totalRawPriceLiveData;
    public MutableLiveData<String> cartStateLiveData;
    public MutableLiveData<String> city;
    public MutableLiveData<String> state;
    public MutableLiveData<String> region;
    public MutableLiveData<String> exactAddress;
    public MutableLiveData<String> totalAllPriceLiveData;
    public MutableLiveData<String> totalDiscountLiveData;
    public MutableLiveData<Integer> packingCost;
    public MutableLiveData<Integer> taxAndService;
    public MutableLiveData<String> deliveryTypeTextLiveData;
    public MutableLiveData<String> restaurantAddressLiveData;
    public MutableLiveData<Integer> deliveryTypeSelectLiveData;
    public MutableLiveData<Integer> deliveryTypeViewLiveData;

    private int totalDiscountedPrice = 0;
    private int tempTotalRawPrice = 0;


    public CartViewModel(Context context, CartDataSource cartDataSource, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.cartDataSource = cartDataSource;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;

        city = new MutableLiveData<>();
        state = new MutableLiveData<>();
        region = new MutableLiveData<>();
        exactAddress = new MutableLiveData<>();
        cartStateLiveData = new MutableLiveData<>();
        totalRawPriceLiveData = new MutableLiveData<>();
        totalAllPriceLiveData = new MutableLiveData<>();
        totalDiscountLiveData = new MutableLiveData<>();
        packingCost = new MutableLiveData<>();
        taxAndService = new MutableLiveData<>();
        deliveryTypeTextLiveData = new MutableLiveData<>();
        deliveryTypeSelectLiveData = new MutableLiveData<>();
        deliveryTypeViewLiveData = new MutableLiveData<>();
        restaurantAddressLiveData = new MutableLiveData<>();

        compositeDisposableGetAllItems = new CompositeDisposable();
    }

    public void setPackageItem(RestaurantPackageItem packageItem) {
        this.packageItem = packageItem;
    }

    public void setRestaurantInfo(RestaurantInfoResponse restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
        packingCost.setValue(restaurantInfo.getPackingCostInt());
        taxAndService.setValue(restaurantInfo.getTaxAndService());
        restaurantAddressLiveData.setValue(restaurantInfo.getRegion() + restaurantInfo.getAddress());

    }

    public void onCheckedChanged(boolean checked) {
        if (!checked) {
            deliveryTypeTextLiveData.setValue("ارسال به آدرس شما");
            deliveryTypeViewLiveData.setValue(1);
        } else {
            deliveryTypeTextLiveData.setValue("دریافت در رستوران");
            deliveryTypeViewLiveData.setValue(2);
        }
    }

    private void initDeliveryType() {
        boolean deliverInPlace = restaurantInfo.isGetInPlace();
        boolean deliverInDestination = restaurantInfo.isDelivery();
        if (deliverInDestination && deliverInPlace) {
            deliveryTypeTextLiveData.setValue("نحوه دریافت سفارش");
            deliveryTypeSelectLiveData.setValue(1); //show switch button
            deliveryTypeViewLiveData.setValue(1);
        } else if (deliverInDestination) {
            deliveryTypeTextLiveData.setValue("فقط ارسال به آدرس شما");
            deliveryTypeSelectLiveData.setValue(0); //hide switch button
            deliveryTypeViewLiveData.setValue(1);   //just show deliver in address destination

        } else {
            deliveryTypeTextLiveData.setValue("فقط دریافت در رستوران");
            deliveryTypeSelectLiveData.setValue(0); //hide switch button
            deliveryTypeViewLiveData.setValue(2);   //just show deliver in place
        }
    }

    public void checkUserAddressForService(Double lat, Double lng) {
        LatLng city = new LatLng(lat, lng);
        String closeRegionsCoordinates = restaurantInfo.getCloseRegionsCoordinates().substring(2); //substring for remove P:
        String[] closeRegionsCoordinatesSeperated = closeRegionsCoordinates.split(" ");
        String serviceAreaCoordinates = restaurantInfo.getServiceAreaCoordinates().substring(2);
        String[] serviceAreaCoordinatesSeperated = serviceAreaCoordinates.split(" ");

        List<LatLng> closePoints = new ArrayList<>();
        for (int i = 0; i < closeRegionsCoordinatesSeperated.length; i++) {
            String[] latLngSeprated = closeRegionsCoordinatesSeperated[i].split(",");
            LatLng latLng = new LatLng(Double.parseDouble(latLngSeprated[1]), Double.parseDouble(latLngSeprated[0]));
            closePoints.add(latLng);
        }

        if (PolyUtil.containsLocation(city.latitude, city.longitude, closePoints, true)) {
            Log.i(TAG, "1");
        } else {
            List<LatLng> serviceAreaPoints = new ArrayList<>();
            for (int i = 0; i < serviceAreaCoordinatesSeperated.length; i++) {
                String[] latLngSeprated = serviceAreaCoordinatesSeperated[i].split(",");
                LatLng latLng = new LatLng(Double.parseDouble(latLngSeprated[1]), Double.parseDouble(latLngSeprated[0]));
                serviceAreaPoints.add(latLng);
            }
            Log.i(TAG, "" + PolyUtil.containsLocation(city.latitude, city.longitude, serviceAreaPoints, true));
        }
    }


    public void getReverseAddressParsiMap(Double latitude, Double longitude, String authToken) {
        rx.Observable<ReverseFindAddressResponse> observable = userRepository.getReverseFindAddressResponse(latitude, longitude);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<ReverseFindAddressResponse>() {
                @Override
                public void onCompleted() {
                    if (state != null){
                        checkAddressAndGetStateId(authToken);
                        mapDialogInterface.onSuccess();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    mapDialogInterface.onFailure(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        mapDialogInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            mapDialogInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            mapDialogInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(ReverseFindAddressResponse reverseFindAddressResponse) {
                    state.setValue(reverseFindAddressResponse.getResult().get(0).getTitle());
                    city.setValue(reverseFindAddressResponse.getResult().get(3).getTitle());
                    region.setValue(reverseFindAddressResponse.getShortAddress());
                    Toast.makeText(context, "" + reverseFindAddressResponse.getShortAddress() + "  " + state.getValue() + "  " + city.getValue(), Toast.LENGTH_LONG).show();

                    //cartInterface.onSuccessGetReverseAddress(reverseFindAddressResponse);
                }
            });
        }
    }

    public void addMapPositionBtnClick(){
        Toast.makeText(context, ""+state.getValue()+" "+stateId+" "+city.getValue()+" "+cityId, Toast.LENGTH_SHORT).show();
        cartInterface.showAddressBottomSheet();
    }

    private void checkAddressAndGetStateId(String authToken) {
        if (statesLists != null) {// dont let extra request
            for (AllStatesList item : statesLists) {
                if (item.getName().contains(state.getValue())) {
                    stateId = item.getId();
                    getCities(authToken, stateId);

                }
            }

        } else getStates(authToken);
    }

    private void getCityId() {

        if(city.getValue() != null)
            for (AllCitiesList item : citiesLists) {
                if (item.getName().contains(city.getValue())) {
                    cityId = item.getId();

                }
            }
    }
    private void getStateId() {

        if(state.getValue() != null)
            for (AllStatesList item : statesLists) {
                if (item.getName().contains(state.getValue())) {
                    stateId = item.getId();
                }
            }
    }

    public void getUserAddress(String authToken) {
        rx.Observable<GetUserAddressResponse> observable = userRepository.getUserAddressResponseObservable(authToken);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserAddressResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        cartInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            cartInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            cartInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetUserAddressResponse getUserAddressResponse) {


                    cartInterface.onSuccessGetAddress(getUserAddressResponse.getUserAddressLists());
                    initDeliveryType();
                }
            });
        }
    }

    public void getAllItemInCart(long restaurantId) {
        compositeDisposableGetAllItems.add(cartDataSource.getAllCart(restaurantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    if (cartItems.isEmpty()) {
                        cartStateLiveData.setValue(context.getString(R.string.empty_cart));

                    } else {
                        cartInterface.onSuccess(cartItems);
                        this.cartItems = cartItems;


                    }
                    calculateCartTotalPrice(restaurantId);
                }, throwable -> {
                    Toast.makeText(context, "{GET ALL Cart}" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }));
    }

    public void calculateFinalCartTotalPrice() {
        finalPaymentPrices = new ArrayList<>();
        totalDiscountedPrice = 0;
        tempTotalRawPrice = 0;
        if (cartItems != null)
            for (int i = 0; i < cartItems.size(); i++) {
                int count = cartItems.get(i).getFoodQuantity();
                long id = cartItems.get(i).getFoodId();
                double price = count * cartItems.get(i).getFoodPrice();
                tempTotalRawPrice += price;
                Log.i(TAG + " " + cartItems.get(i).getFoodName() + " ", "count: " + count);

                if (packageItem == null) { //not selected any discountRestaurantPackage
                    double discount = 100 - cartItems.get(i).getFoodDiscount();
                    discount = discount / 100;
                    price = price * discount;
                    totalDiscountedPrice += price;

                    totalAllPriceLiveData.setValue(moneyFormat((int) totalDiscountedPrice + packingCost.getValue() + taxAndService.getValue()));
                    FinalPaymentPrice paymentPrice = new FinalPaymentPrice();
                    paymentPrice.setId(id);
                    paymentPrice.setDiscountedPrice((int) price);
                    paymentPrice.setName(cartItems.get(i).getFoodName());
                    finalPaymentPrices.add(paymentPrice);
                } else { //use discountRestaurantPackage
                    double pDiscount = 100 - packageItem.getDiscountPercent();
                    pDiscount = pDiscount / 100;
                    Log.i(TAG + " pDiscount: ", "" + totalDiscountedPrice);
                    if (packageItem.isDiscountForAllFoods()) { // discount effect on all items
                        price = price * pDiscount;
                        totalDiscountedPrice += price;
                        if (tempTotalRawPrice - totalDiscountedPrice >= packageItem.getMaximumDiscountAmount()) {
                            totalDiscountedPrice = tempTotalRawPrice - packageItem.getMaximumDiscountAmount();
                            totalAllPriceLiveData.setValue(moneyFormat((tempTotalRawPrice - packageItem.getMaximumDiscountAmount()) + packingCost.getValue() + taxAndService.getValue()));
                        } else {

                            totalAllPriceLiveData.setValue(moneyFormat((int) totalDiscountedPrice + packingCost.getValue() + taxAndService.getValue()));
                        }

                    } else { // discount effect on Specific items
                        if (packageItem.getFoodList() != null) {
                            Map<Long, String> specificItems = packageItem.getFoodList();
                            for (Map.Entry<Long, String> entry : specificItems.entrySet()) {
                                long foodId = entry.getKey();
                                String foodName = entry.getValue();
                                Log.i(TAG, foodId + " : " + foodName);
                                if (foodId == id) {

                                    id = 0; // dont let calculate one food with two tag type for twice
                                    price = price * pDiscount;
                                    totalDiscountedPrice += price;
                                    Log.i(TAG + " have foodId : ", "" + totalDiscountedPrice);
                                }
                            }
                            if (id != 0) {
                                totalDiscountedPrice += price;
                                Log.i(TAG + " dontHave foodId : ", "" + totalDiscountedPrice);
                            }
                            Log.i(TAG + " totalRawPriceLiveDat: ", "" + tempTotalRawPrice);
                            Log.i(TAG + " DiscountedPrice: ", "" + totalDiscountedPrice);
                            Log.i(TAG + " getMaximumDiscount: ", "" + packageItem.getMaximumDiscountAmount());
                            Log.i(TAG + " 0 : ", "" + (totalRawPriceLiveData.getValue() - (long) totalDiscountedPrice >= packageItem.getMaximumDiscountAmount()));
                            if (tempTotalRawPrice - totalDiscountedPrice >= packageItem.getMaximumDiscountAmount()) {

                                totalDiscountedPrice = tempTotalRawPrice - packageItem.getMaximumDiscountAmount();
                                totalAllPriceLiveData.setValue(moneyFormat((tempTotalRawPrice - packageItem.getMaximumDiscountAmount()) + packingCost.getValue() + taxAndService.getValue()));
                                Log.i(TAG + " 1 : ", "" + totalDiscountedPrice);
                            } else {

                                totalAllPriceLiveData.setValue(moneyFormat((int) totalDiscountedPrice + packingCost.getValue() + taxAndService.getValue()));
                                Log.i(TAG + " 2 : ", "" + totalDiscountedPrice);
                            }

                        }

                    }
                }
            }

    }


    private void calculateCartTotalPrice(Long restaurantId) {
        cartDataSource.sumPrice(restaurantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Long aLong) {

                totalRawPriceLiveData.setValue(aLong);
                calculateFinalCartTotalPrice();
                totalDiscountLiveData.setValue(String.valueOf(aLong - totalDiscountedPrice));

            }

            @Override
            public void onError(Throwable e) {
                totalRawPriceLiveData.setValue((long) 0);
                totalAllPriceLiveData.setValue("سبد خرید خالی است");
                totalDiscountLiveData.setValue("");
                Log.e(TAG, "{UPDATE CART ITEM}-> " + e.getMessage());
            }
        });
    }

    public void getStates(String authToken) {
        rx.Observable<GetAllStatesResponse> observable = userRepository.getAllStatesResponseObservable(authToken);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllStatesResponse>() {
                @Override
                public void onCompleted() {
                    getStateId();
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        mapDialogInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            mapDialogInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            mapDialogInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetAllStatesResponse getAllStatesResponse) {
                    statesLists = getAllStatesResponse.getAllStatesLists();
                    cartInterface.onSuccessGetStates(getAllStatesResponse.getAllStatesLists());


                }
            });
        }
    }

    public void getCities(String authToken, long stateId) {
        rx.Observable<GetAllCitiesResponse> observable = userRepository.getAllCitiesResponseObservable(authToken, stateId);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllCitiesResponse>() {
                @Override
                public void onCompleted() {
                    getCityId();
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        cartInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            cartInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            cartInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetAllCitiesResponse getAllCitiesResponse) {
                    citiesLists = getAllCitiesResponse.getAllCitiesLists();
                    cartInterface.onSuccessGetcities(getAllCitiesResponse.getAllCitiesLists());
                }
            });
        }
    }


    public void onStop() {
        if (compositeDisposableGetAllItems != null)
            compositeDisposableGetAllItems.clear();
    }

    private String moneyFormat(int cost) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber + " تومان";
    }
}
