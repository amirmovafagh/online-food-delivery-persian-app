package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import org.json.JSONObject;

import java.util.List;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllCitiesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.models.user.AddUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.EditUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.models.user.UserProfileResponse;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.AddressDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.AddressDataSourceFactory;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.AddressDataSourceInterface;
import ir.boojanco.onlinefoodorder.util.EmailValidator;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.MapDialogInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.UserProfileInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserProfileViewModel extends ViewModel implements AddressDataSourceInterface {
    private static final String TAG = UserProfileViewModel.class.getSimpleName();
    private Context context;
    private static EmailValidator emailValidator;
    private String userAuthToken;
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;
    public UserProfileInterface userProfileInterface;
    public MapDialogInterface mapDialogInterface;
    public MutableLiveData<String> city;
    private long cityId;
    public MutableLiveData<String> state;
    private long stateId;
    public MutableLiveData<String> region;
    public MutableLiveData<String> exactAddress;
    public MutableLiveData<String> addressBottomSheetTitle;
    public MutableLiveData<Boolean> defaultAddress;
    public MutableLiveData<Boolean> bottomSheetChangeVisibility; //true show addres //false show info
    public MutableLiveData<String> emailLiveData;
    public MutableLiveData<String> emailErrorLiveData;
    public MutableLiveData<String> lastNameLiveData;
    public MutableLiveData<String> lastNameErrorLiveData;
    public MutableLiveData<String> firstNameLiveData;
    public MutableLiveData<String> firstNameErrorLiveData;
    public MutableLiveData<String> birthDateLiveData;
    public MutableLiveData<String> birthDateErrorLiveData;

    public long birthDateTimeMill;
    private double userLatitude;
    private double userLongitude;
    private int addressRecyclerViewPosition;
    private long addressId;
    private String addressTag;
    private int addressFunctionFlag;
    private List<AllStatesList> statesLists;
    private List<AllCitiesList> citiesLists;
    private boolean completeUserProfile = false;

    public LiveData<PagedList<UserAddressList>> userAddressPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, UserAddressList>> userAddressPageKeyedDataSourceLiveData;

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public void setBirthDateTimeMill(long birthDateTimeMill) {
        this.birthDateTimeMill = birthDateTimeMill;
    }

    public UserProfileViewModel(Context context, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        city = new MutableLiveData<>();
        state = new MutableLiveData<>();
        region = new MutableLiveData<>();
        exactAddress = new MutableLiveData<>();
        addressBottomSheetTitle = new MutableLiveData<>();
        defaultAddress = new MutableLiveData<>();
        defaultAddress.setValue(false);
        emailLiveData = new MutableLiveData<>();
        emailErrorLiveData = new MutableLiveData<>();
        lastNameLiveData = new MutableLiveData<>();
        lastNameErrorLiveData = new MutableLiveData<>();
        firstNameLiveData = new MutableLiveData<>();
        firstNameErrorLiveData = new MutableLiveData<>();
        birthDateLiveData = new MutableLiveData<>();
        birthDateErrorLiveData = new MutableLiveData<>();
        bottomSheetChangeVisibility = new MutableLiveData<>();
        bottomSheetChangeVisibility.setValue(true);//default onView Address

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

    public void acceptAddNewUserAddressOrEditAddressOnClick() {
        if (addressFunctionFlag == 1) {//add new address
            if (userLatitude == 0 || userLongitude == 0) {
                userProfileInterface.onFailure("لطفا موقعیت دقیق خودرا در نقشه مشخص کنید");
                return;
            }

            AddUserAddressResponse address = new AddUserAddressResponse(cityId, defaultAddress.getValue(), exactAddress.getValue(), userLatitude, userLongitude, region.getValue(), "WORK");
            Observable<Response<Void>> observable = userRepository.addUserAddressResponseObservable(userAuthToken, address);
            if (observable != null) {
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NoNetworkConnectionException)
                            userProfileInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();

                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                userProfileInterface.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                userProfileInterface.onFailure(d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {

                        Toast.makeText(context, "save", Toast.LENGTH_SHORT).show();
                        getUserAddress(userAuthToken);
                        userProfileInterface.updateAddressRecyclerView(addressRecyclerViewPosition);
                        //cartInterface.onSuccessGetAddress(addUserAddressResponse);
                        //initDeliveryType();
                    }
                });
            }

        } else if (addressFunctionFlag == 2) {
            EditUserAddressResponse editedAddress = new EditUserAddressResponse(exactAddress.getValue(), "WORK", cityId, defaultAddress.getValue(), userLatitude, userLongitude, region.getValue());
            Observable<Response<Void>> observable = userRepository.getEditUserAddressResponseObservable(userAuthToken, addressId, editedAddress);
            if (observable != null) {
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        userProfileInterface.onFailure(e.getMessage());
                        if (e instanceof NoNetworkConnectionException)
                            userProfileInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();

                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                userProfileInterface.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                userProfileInterface.onFailure(d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {
                        getUserAddress(userAuthToken);
                        userProfileInterface.updateAddressRecyclerView(addressRecyclerViewPosition);
                        //cartInterface.onSuccessGetAddress(addUserAddressResponse);
                        //initDeliveryType();
                    }
                });
            }
        }
    }

    public void addAddressOnClick() {
        addressFunctionFlag = 1; // add function
        addressBottomSheetTitle.setValue("افزودن آدرس");

        addressId = 0;
        city.setValue(null);
        cityId = 0;
        stateId = 0;
        userLatitude = 0;
        userLongitude = 0;
        exactAddress.setValue(null);
        addressTag = null;
        region.setValue(null);
        defaultAddress.setValue(false);

        userProfileInterface.showMapDialogFragment();
    }

    public void getReverseAddressParsimap(Double latitude, Double longitude, String authToken) {
        Observable<ReverseFindAddressResponse> observable = userRepository.getReverseFindAddressResponse(latitude, longitude);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ReverseFindAddressResponse>() {
                @Override
                public void onCompleted() {
                    userLatitude = latitude;
                    userLongitude = longitude;
                    if (state.getValue() != null) {
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


                }
            });
        }
    }

    public void showStateCityDialog() {
        userProfileInterface.showStateCityCustomDialog();
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
                }
            }
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
                    userProfileInterface.onSuccessGetStates(getAllStatesResponse.getAllStatesLists());
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
                        userProfileInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            userProfileInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            userProfileInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetAllCitiesResponse getAllCitiesResponse) {
                    citiesLists = getAllCitiesResponse.getAllCitiesLists();
                    userProfileInterface.onSuccessGetcities(getAllCitiesResponse.getAllCitiesLists());
                }
            });
        }
    }

    public void addMapPositionBtnClick() {
        Toast.makeText(context, "" + state.getValue() + " " + stateId + " " + city.getValue() + " " + cityId, Toast.LENGTH_SHORT).show();
        bottomSheetChangeVisibility.setValue(true);
        userProfileInterface.showAddressBottomSheet();
    }

    public void getUserProfileInfo(String authToken) {
        rx.Observable<UserProfileResponse> observable = userRepository.getUserProfileResponseObservable(authToken);
        if (observable != null) {
            userProfileInterface.onStarted();
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserProfileResponse>() {
                @Override
                public void onCompleted() {
                    userProfileInterface.onSuccessGetUserProfileInfo();
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        userProfileInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            userProfileInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            userProfileInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(UserProfileResponse profileResponse) {

                    if (profileResponse.getBirthDate() == 0 && profileResponse.getEmail().equals("") &&
                            profileResponse.getFirstName().equals("") && profileResponse.getLastName().equals("")) {
                        completeUserProfile = false;
                        birthDateTimeMill = profileResponse.getBirthDate();
                        emailLiveData.setValue(profileResponse.getEmail());
                        lastNameLiveData.setValue(profileResponse.getLastName());
                        firstNameLiveData.setValue(profileResponse.getFirstName());
                        birthDateLiveData.setValue(profileResponse.getShamsiDate());
                    } else {
                        completeUserProfile = true;
                        birthDateTimeMill = profileResponse.getBirthDate();
                        emailLiveData.setValue(profileResponse.getEmail());
                        lastNameLiveData.setValue(profileResponse.getLastName());
                        firstNameLiveData.setValue(profileResponse.getFirstName() + " ");
                        birthDateLiveData.setValue(profileResponse.getShamsiDate());
                    }
                    userProfileInterface.onSuccessGetUserProfileInfo();
                }
            });
        }
    }

    public void logoutUser() {
        userProfileInterface.onLogoutUser();
    }

    public void editUserProfile() {
        emailValidator = new EmailValidator();
        bottomSheetChangeVisibility.setValue(false);
        userProfileInterface.onEditUserProfile();
    }

    public void acceptEditUserProfileOnClick() {
        if (!isValidEnteredUserInfo()) {
            return;
        }
        UserProfileResponse userProfileBody;
        if (completeUserProfile) {
            userProfileBody = new UserProfileResponse(firstNameLiveData.getValue(), lastNameLiveData.getValue(), emailLiveData.getValue(), birthDateTimeMill);
            rx.Observable<Response<Void>> observable = userRepository.editUserProfileObservable(userAuthToken, userProfileBody);
            if (observable != null) {
                userProfileInterface.onStarted();
                observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                        userProfileInterface.onSuccessGetUserProfileInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NoNetworkConnectionException)
                            userProfileInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();

                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());

                                userProfileInterface.onFailure(jsonObject.getString("message"));


                            } catch (Exception d) {
                                userProfileInterface.onFailure(d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {
                        Log.i(TAG, voidResponse.isSuccessful() + "");
                        userProfileInterface.hideBottomSheet();
                    }
                });
            }
        } else {
            userProfileBody = new UserProfileResponse(firstNameLiveData.getValue(), lastNameLiveData.getValue(), emailLiveData.getValue(), birthDateTimeMill);
            rx.Observable<Response<Void>> observable = userRepository.completeUserProfileObservable(userAuthToken, userProfileBody);
            if (observable != null) {
                userProfileInterface.onStarted();
                observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                        userProfileInterface.onSuccessGetUserProfileInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NoNetworkConnectionException)
                            userProfileInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();

                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());

                                userProfileInterface.onFailure(jsonObject.getString("message"));


                            } catch (Exception d) {
                                userProfileInterface.onFailure(d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {
                        Log.i(TAG, voidResponse.isSuccessful() + "");
                        userProfileInterface.hideBottomSheet();
                    }
                });
            }
        }

    }

    private boolean isValidEnteredUserInfo() {
        if (firstNameLiveData.getValue() == null || firstNameLiveData.getValue().equals("")) {
            firstNameErrorLiveData.setValue("*");
            return false;
        } else {
            firstNameErrorLiveData.setValue(null);
        }
        if (lastNameLiveData.getValue() == null || lastNameLiveData.getValue().equals("")) {
            lastNameErrorLiveData.setValue("*");
            return false;
        } else {
            lastNameErrorLiveData.setValue(null);
        }
        if (emailLiveData.getValue() == null || emailLiveData.getValue().equals("")) {
            emailErrorLiveData.setValue("*");
            return false;
        } else if (emailValidator.validateEmail(emailLiveData.getValue())) {// email is valid
            emailErrorLiveData.setValue(null);
        } else {// email is not valid
            emailErrorLiveData.setValue("فرمت ایمیل وارد شده نادرست می باشد");
            return false;
        }
        if (birthDateLiveData.getValue() == null || birthDateLiveData.getValue().equals("")) {
            birthDateErrorLiveData.setValue("*");
            return false;
        } else {
            birthDateErrorLiveData.setValue(null);
        }
        return true;
    }


    public void birthDatePickerOnClick() {
        userProfileInterface.showDatePickerDialog(birthDateTimeMill);
    }

    @Override
    public void onStarted() {
        userProfileInterface.onStarted();
    }

    @Override
    public void onSuccess() {
        userProfileInterface.onSuccessGetAddress();
    }

    @Override
    public void onFailure(String error) {
        userProfileInterface.onFailure(error);
    }

    public void editUserAddress(UserAddressList userAddress, int position) {
        bottomSheetChangeVisibility.setValue(true);
        addressRecyclerViewPosition = position;
        addressFunctionFlag = 2; // edit function
        addressBottomSheetTitle.setValue("تغییر آدرس");
        addressId = userAddress.getId();
        city.setValue(userAddress.getCity());
        cityId = userAddress.getCityId();
        stateId = userAddress.getStateId();
        userLatitude = userAddress.getLatitude();
        userLongitude = userAddress.getLongitude();
        exactAddress.setValue(userAddress.getExactAddress());
        addressTag = userAddress.getTag();
        region.setValue(userAddress.getRegion());
        defaultAddress.setValue(userAddress.isDefaultAddress());
    }
}
