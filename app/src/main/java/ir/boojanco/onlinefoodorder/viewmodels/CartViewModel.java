package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButtonToggleGroup;
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
import ir.boojanco.onlinefoodorder.models.user.AddUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.AddressDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.AddressDataSourceFactory;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.AddressDataSourceInterface;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.FinalPaymentPrice;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.util.OrderType;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.CartInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.MapDialogInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Subscriber;

public class CartViewModel extends ViewModel implements AddressDataSourceInterface {
    private String TAG = this.getClass().getSimpleName();

    private CartInterface fragmentInterface;
    public MapDialogInterface mapDialogCartInterface;
    private Context context;
    private CartDataSource cartDataSource;
    private CompositeDisposable compositeDisposableGetAllItems;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    public long cityId, stateId;
    private double userLatitude;
    private double userLongitude;
    public MutableLiveData<Boolean> defaultAddress;
    private RestaurantPackageItem packageItem;
    private long packageId = 0;
    private RestaurantInfoResponse restaurantInfo;
    private List<CartItem> cartItems;
    private List<AllStatesList> statesLists;
    private List<AllCitiesList> citiesLists;
    private ArrayList<FinalPaymentPrice> finalPaymentPrices;
    public String userAuthToken;
    private long shippingUserAddressId = 0;
    private String addressTag = "OTHER";

    private boolean emptyCart = false;
    private OrderType orderType = OrderType.DONT_CHOOSE;
    public MutableLiveData<Boolean> changeViewLiveData;
    public MutableLiveData<String> totalRawPriceLiveData;
    private int totalRawPrice = 0;
    public MutableLiveData<String> cartStateLiveData;
    public MutableLiveData<String> city;
    public MutableLiveData<String> state;
    public MutableLiveData<String> region;
    public MutableLiveData<String> exactAddress;
    public MutableLiveData<String> totalAllPriceLiveData;
    private int totalAllPrice = 0;
    public MutableLiveData<String> totalDiscountLiveData;
    private int totalDiscount = 0;
    public MutableLiveData<String> packingCostLiveData;
    private int packingCost = 0;
    private double taxAndServicePercent = 0;
    public MutableLiveData<String> restaurantShippingCostLiveData;
    private int shippingCost = 0;
    public MutableLiveData<String> taxAndServiceLivedata;
    private int taxAndService = 0;
    public MutableLiveData<String> deliveryTypeTextLiveData;
    public MutableLiveData<String> restaurantAddressLiveData;
    public MutableLiveData<Integer> deliveryTypeButtonVisibilityLiveData;
    public MutableLiveData<Integer> deliveryTypeViewLiveData;
    public LiveData<PagedList<UserAddressList>> userAddressPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, UserAddressList>> userAddressPageKeyedDataSourceLiveData;
    private MaterialButtonToggleGroup materialDeliveryBtnGroup;

    public void setMaterialDeliveryBtnGroup(MaterialButtonToggleGroup materialDeliveryBtnGroup) {
        this.materialDeliveryBtnGroup = materialDeliveryBtnGroup;
    }

    private int totalDiscountedPrice = 0;
    private int tempTotalRawPrice = 0;

    public void setFragmentInterface(CartInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }

    public CartViewModel(Context context, CartDataSource cartDataSource, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.cartDataSource = cartDataSource;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;

        changeViewLiveData = new MutableLiveData<>();
        changeViewLiveData.setValue(false);//false: show restaurants carts //true: show only one restaurant cart
        city = new MutableLiveData<>();
        state = new MutableLiveData<>();
        region = new MutableLiveData<>();
        exactAddress = new MutableLiveData<>();
        cartStateLiveData = new MutableLiveData<>();
        totalRawPriceLiveData = new MutableLiveData<>();
        totalAllPriceLiveData = new MutableLiveData<>();
        totalDiscountLiveData = new MutableLiveData<>();
        packingCostLiveData = new MutableLiveData<>();
        packingCostLiveData.setValue(moneyFormat(0));
        taxAndServiceLivedata = new MutableLiveData<>();
        deliveryTypeTextLiveData = new MutableLiveData<>();
        deliveryTypeButtonVisibilityLiveData = new MutableLiveData<>();
        deliveryTypeViewLiveData = new MutableLiveData<>();
        restaurantAddressLiveData = new MutableLiveData<>();
        restaurantShippingCostLiveData = new MutableLiveData<>();
        restaurantShippingCostLiveData.setValue(moneyFormat(0));
        defaultAddress = new MutableLiveData<>();
        defaultAddress.setValue(false);
        compositeDisposableGetAllItems = new CompositeDisposable();
    }

    public void setPackageItem(RestaurantPackageItem packageItem) {
        if (packageItem != null)
            packageId = packageItem.getId();
        this.packageItem = packageItem;
    }

    public void setRestaurantInfo(RestaurantInfoResponse restaurantInfo) {
        this.restaurantInfo = restaurantInfo;
        packingCost = restaurantInfo.getPackingCostInt();
        packingCostLiveData.setValue(moneyFormat(packingCost));
        taxAndServicePercent = restaurantInfo.getTaxAndService();
        //taxAndService.setValue(restaurantInfo.getTaxAndService());
        restaurantAddressLiveData.setValue(restaurantInfo.getRegion() + restaurantInfo.getAddress());

    }
    public void closeFilterBottomSheetOnClick(){
        fragmentInterface.closeBottomSheet();
    }

    public void OnButtonDeliveryChecked(View view) {
        if (view.getId() == R.id.btn_your_address) {
            deliveryTypeViewLiveData.setValue(1);
            orderType = OrderType.DONT_CHOOSE;
        } else {
            deliveryTypeViewLiveData.setValue(2);
            orderType = OrderType.GET_BY_CUSTOMER;
            shippingUserAddressId = 0;
            restaurantShippingCostLiveData.setValue(moneyFormat(0));
            shippingCost = 0;
            calculateFinalCartTotalPrice();
            fragmentInterface.resetAddressRecyclerView();

        }
    }

    private void initDeliveryType() {
        boolean deliverInPlace = restaurantInfo.isGetInPlace();
        boolean deliverInDestination = restaurantInfo.isDelivery();
        if (deliverInDestination && deliverInPlace) {
            deliveryTypeTextLiveData.setValue("???????? ???????????? ??????????");
            deliveryTypeButtonVisibilityLiveData.setValue(0); //show switch button
            deliveryTypeViewLiveData.setValue(1);
            materialDeliveryBtnGroup.check(R.id.btn_your_address);
        } else if (deliverInDestination) {
            deliveryTypeTextLiveData.setValue("?????? ?????????? ???? ???????? ??????");
            deliveryTypeButtonVisibilityLiveData.setValue(1); //hide switch button
            deliveryTypeViewLiveData.setValue(1);   //just show deliver in address destination
            materialDeliveryBtnGroup.check(R.id.btn_your_address);
        } else {
            deliveryTypeTextLiveData.setValue("?????? ???????????? ???? ??????????????");
            deliveryTypeButtonVisibilityLiveData.setValue(2); //hide switch button
            deliveryTypeViewLiveData.setValue(2);   //just show deliver in place
            orderType = OrderType.GET_BY_CUSTOMER;
            materialDeliveryBtnGroup.check(R.id.btn_at_restaurant);
        }
    }

    public void removeUserAddressInfo() {
        orderType = OrderType.DONT_CHOOSE;
        restaurantShippingCostLiveData.setValue(moneyFormat(0));
        shippingUserAddressId = 0;
        shippingCost = 0;
        calculateFinalCartTotalPrice();
    }

    public void checkUserAddressForService(long userAddressId, Double lat, Double lng) {
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

        if (PolyUtil.containsLocation(city.latitude, city.longitude, closePoints, true)) { //check user address is in close area
            shippingCost = restaurantInfo.getShippingCostInCloseRegions();
            restaurantShippingCostLiveData.setValue(moneyFormat(shippingCost));
            orderType = OrderType.GET_BY_DELIVERY;
            shippingUserAddressId = userAddressId;
        } else {
            List<LatLng> serviceAreaPoints = new ArrayList<>();
            for (int i = 0; i < serviceAreaCoordinatesSeperated.length; i++) {          //check user address is in service area
                String[] latLngSeprated = serviceAreaCoordinatesSeperated[i].split(",");
                LatLng latLng = new LatLng(Double.parseDouble(latLngSeprated[1]), Double.parseDouble(latLngSeprated[0]));
                serviceAreaPoints.add(latLng);
            }
            if (PolyUtil.containsLocation(city.latitude, city.longitude, serviceAreaPoints, true)) {
                shippingCost = restaurantInfo.getShippingCostInServiceArea();
                restaurantShippingCostLiveData.setValue(moneyFormat(shippingCost));
                orderType = OrderType.GET_BY_DELIVERY;
                shippingUserAddressId = userAddressId;
            } else {
                restaurantShippingCostLiveData.setValue("?????? ?????????? ??????");
                fragmentInterface.onFailure("?????? ?????????? ?????? ???? ???????????? ??????");
                orderType = OrderType.DONT_CHOOSE;
            }
        }
        calculateFinalCartTotalPrice();
    }


    public void getReverseAddressParsiMap(Double latitude, Double longitude) {
        rx.Observable<ReverseFindAddressResponse> observable = userRepository.getReverseFindAddressResponse(latitude, longitude);
        if (observable != null) {

            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<ReverseFindAddressResponse>() {
                @Override
                public void onCompleted() {
                    userLatitude = latitude;
                    userLongitude = longitude;
                    if (state.getValue() != null) {
                        checkAddressAndGetStateId();
                        mapDialogCartInterface.onSuccess();
                    }
                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof NoNetworkConnectionException)
                        mapDialogCartInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            mapDialogCartInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            Log.i(TAG, "" + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(ReverseFindAddressResponse reverseFindAddressResponse) {

                    state.setValue(reverseFindAddressResponse.getState());
                    city.setValue(reverseFindAddressResponse.getCity());
                    region.setValue(reverseFindAddressResponse.getShortAddress());
                    Log.i(TAG, "shortAddress: " + reverseFindAddressResponse.getShortAddress() + " state: " + state.getValue() + " city: " + city.getValue());
                }
            });
        }
    }

    public void addMapPositionBtnClick() {
        Log.i(TAG, "" + state.getValue() + " " + stateId + " " + city.getValue() + " " + cityId);
        fragmentInterface.showAddressBottomSheet();
    }

    public void showMap() {
        fragmentInterface.showMapDialogFragment();
    }

    public void showStateCityDialog() {
        fragmentInterface.showStateCityCustomDialog();
    }

    private void checkAddressAndGetStateId() {
        if (statesLists != null) {// dont let extra request
            for (AllStatesList item : statesLists) {
                if (item.getName().contains(state.getValue())) {
                    stateId = item.getId();
                    getCities(stateId);

                }
            }

        } else getStates();
    }

    private void getCityId() {

        if (city.getValue() != null)
            for (AllCitiesList item : citiesLists) {
                if (item.getName().contains(city.getValue())) {
                    cityId = item.getId();

                }
            }
    }

    private void getStateId() {

        if (state.getValue() != null)
            for (AllStatesList item : statesLists) {
                if (item.getName().contains(state.getValue())) {
                    stateId = item.getId();
                    getCities(stateId);
                }
            }
    }

    public void getUserAddress(String authToken) {
        userAuthToken = authToken;
        AddressDataSourceFactory addressDataSourceFactory = new AddressDataSourceFactory(userRepository, this, authToken);
        userAddressPageKeyedDataSourceLiveData = addressDataSourceFactory.getAddresstLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(AddressDataSource.PAGE_SIZE)
                        .build();
        userAddressPagedListLiveData = (new LivePagedListBuilder(addressDataSourceFactory, config)).build();
    }

    public void setAddressTagOnClick(View v) {

        switch (v.getId()) {
            case R.id.chip_home_tag:
                addressTag = "HOME";
                break;
            case R.id.chip_work_tag:
                addressTag = "WORK";
                break;
            case R.id.chip_university_tag:
                addressTag = "UNIVERSITY";
                break;
            case R.id.chip_other_tag:
            default:
                addressTag = "OTHER";
                break;
        }
    }

    public void addUserAddress() {

        AddUserAddressResponse address = new AddUserAddressResponse(cityId, defaultAddress.getValue(), exactAddress.getValue(), userLatitude, userLongitude, region.getValue(), addressTag);
        rx.Observable<Response<Void>> observable = userRepository.addUserAddressResponseObservable(userAuthToken, address);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        fragmentInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            fragmentInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            Log.i(TAG, "" + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(Response<Void> voidResponse) {
                    if (voidResponse.isSuccessful())
                        fragmentInterface.hideAddressBottomSheet("???????? ?????? ????");
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
                        fragmentInterface.onSuccessCartItems(cartItems);
                        this.cartItems = cartItems;

                    }
                    calculateCartTotalPrice(restaurantId);
                }, throwable -> {
                    Log.e(TAG, "{GET ALL Cart}" + throwable.getMessage());
                }));
    }

    public void getAllRestaurantsCart() {
        compositeDisposableGetAllItems.add(cartDataSource.getAllRestaurantsCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurantItems -> {
                    fragmentInterface.onSuccessRestaurantsCarts(restaurantItems);
                }, throwable -> {
                    Log.e(TAG, "{GET Restaurants Cart}" + throwable.getMessage());
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

                    double taxPercent = taxAndServicePercent / 100;
                    taxAndService = (int) (totalDiscountedPrice * taxPercent);
                    taxAndServiceLivedata.setValue(moneyFormat(taxAndService));
                    totalAllPrice = totalDiscountedPrice + packingCost + taxAndService + shippingCost;
                    totalAllPriceLiveData.setValue(moneyFormat(totalAllPrice));
                    FinalPaymentPrice paymentPrice = new FinalPaymentPrice();
                    paymentPrice.setId(id);
                    paymentPrice.setDiscountedPrice((int) price);
                    paymentPrice.setName(cartItems.get(i).getFoodName());
                    finalPaymentPrices.add(paymentPrice);
                } else { //use discountRestaurantPackage
                    double pDiscount = 100 - packageItem.getDiscountPercent();
                    pDiscount = pDiscount / 100;

                    double taxPercent = taxAndServicePercent / 100;
                    Log.i(TAG + " pDiscount: ", "" + totalDiscountedPrice);
                    if (packageItem.isDiscountForAllFoods()) { // discount effect on all items
                        price = price * pDiscount;
                        totalDiscountedPrice += price;
                        taxAndService = (int) (totalDiscountedPrice * taxPercent);
                        taxAndServiceLivedata.setValue(moneyFormat(taxAndService));
                        if (tempTotalRawPrice - totalDiscountedPrice >= packageItem.getMaximumDiscountAmount()) {
                            totalDiscountedPrice = tempTotalRawPrice - packageItem.getMaximumDiscountAmount();
                            taxAndService = (int) (totalDiscountedPrice * taxPercent);
                            taxAndServiceLivedata.setValue(moneyFormat(taxAndService));
                            totalAllPrice = (tempTotalRawPrice - packageItem.getMaximumDiscountAmount()) + packingCost + taxAndService + shippingCost;
                            totalAllPriceLiveData.setValue(moneyFormat(totalAllPrice));
                        } else {
                            totalAllPrice = totalDiscountedPrice + packingCost + taxAndService + shippingCost;
                            totalAllPriceLiveData.setValue(moneyFormat(totalAllPrice));
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
                            taxAndService = (int) (totalDiscountedPrice * taxPercent);
                            taxAndServiceLivedata.setValue(moneyFormat(taxAndService));
                            if (tempTotalRawPrice - totalDiscountedPrice >= packageItem.getMaximumDiscountAmount()) {

                                totalDiscountedPrice = tempTotalRawPrice - packageItem.getMaximumDiscountAmount();
                                taxAndService = (int) (totalDiscountedPrice * taxPercent);
                                taxAndServiceLivedata.setValue(moneyFormat(taxAndService));
                                totalAllPrice = (tempTotalRawPrice - packageItem.getMaximumDiscountAmount()) + packingCost + taxAndService + shippingCost;
                                totalAllPriceLiveData.setValue(moneyFormat(totalAllPrice));
                                Log.i(TAG + " 1 : ", "" + totalDiscountedPrice);
                            } else {
                                totalAllPrice = totalDiscountedPrice + packingCost + taxAndService + shippingCost;
                                totalAllPriceLiveData.setValue(moneyFormat(totalAllPrice));
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
                totalRawPrice = aLong.intValue();
                totalRawPriceLiveData.setValue(moneyFormat(aLong.intValue()));
                calculateFinalCartTotalPrice();
                totalDiscount = totalRawPrice - totalDiscountedPrice;
                totalDiscountLiveData.setValue(moneyFormat(totalDiscount));

            }

            @Override
            public void onError(Throwable e) {
                emptyCart = true;
                totalRawPriceLiveData.setValue(moneyFormat(0));
                totalAllPriceLiveData.setValue("?????? ???????? ???????? ??????");
                totalDiscountLiveData.setValue(moneyFormat(0));
                taxAndServiceLivedata.setValue(moneyFormat(0));
                packingCostLiveData.setValue(moneyFormat(0));
                Log.e(TAG, "{UPDATE CART ITEM}-> " + e.getMessage());
            }
        });
    }

    public void getStates() {
        rx.Observable<GetAllStatesResponse> observable = userRepository.getAllStatesResponseObservable();
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllStatesResponse>() {
                @Override
                public void onCompleted() {
                    getStateId();
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        mapDialogCartInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            mapDialogCartInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            mapDialogCartInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetAllStatesResponse getAllStatesResponse) {
                    statesLists = getAllStatesResponse.getAllStatesLists();
                    fragmentInterface.onSuccessGetStates(getAllStatesResponse.getAllStatesLists());


                }
            });
        }
    }

    public void getCities(long stateId) {
        rx.Observable<GetAllCitiesResponse> observable = userRepository.getAllCitiesResponseObservable(stateId);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllCitiesResponse>() {
                @Override
                public void onCompleted() {
                    getCityId();
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        fragmentInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            fragmentInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            Log.i(TAG, "" + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetAllCitiesResponse getAllCitiesResponse) {
                    citiesLists = getAllCitiesResponse.getAllCitiesLists();
                    fragmentInterface.onSuccessGetCities(getAllCitiesResponse.getAllCitiesLists());
                }
            });
        }
    }

    public void acceptOrder() {

        if (emptyCart) {
            fragmentInterface.onFailure("?????? ???????? ???????? ??????.");
            return;
        }
        switch (orderType) {
            case DONT_CHOOSE:
                fragmentInterface.onFailure("???????? ???????? ???????????? ?????????? ???? ???????? ????????");
                break;
            case GET_BY_CUSTOMER:
            case GET_BY_DELIVERY:
                fragmentInterface.acceptOrder(finalPaymentPrices, cartItems, totalAllPrice, totalRawPrice,
                        totalDiscount, packingCost, taxAndService, shippingCost, orderType, restaurantInfo.getId(), packageId, shippingUserAddressId);
                break;
            case SERVE_IN_PLACE:
                // TODO: SERVE_IN_PLACE implement later
                break;
        }


    }

    public void onStop() {
        if (compositeDisposableGetAllItems != null)
            compositeDisposableGetAllItems.clear();
    }

    private String moneyFormat(int cost) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber + " ??????????";
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess() {
        initDeliveryType();
        fragmentInterface.onSuccessGetAddress();

    }

    @Override
    public void onFailure(String error) {
        fragmentInterface.onFailure(error);
    }


}
