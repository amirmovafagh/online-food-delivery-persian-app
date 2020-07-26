package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.food.FoodCategoriesResponse;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllCitiesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.ui.fragments.home.HomeFragment;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.HomeFragmentInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.MapDialogInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private String TAG = HomeFragment.class.getSimpleName();
    private Context context;
    private RestaurantRepository restaurantRepository;
    private HomeFragmentInterface fragmentInterface;
    private String userAuthToken;
    public MutableLiveData<String> cityLiveData;
    public MutableLiveData<String> stateLiveData;
    public MutableLiveData<Boolean> stateWatingOrNoConnection;
    public MutableLiveData<Boolean> stateProgressBar;
    private MapDialogInterface mapDialogInterface;
    private Double lat,lon;

    public void setUserAuthToken(String userAuthToken) {
        this.userAuthToken = userAuthToken;
    }

    public void setFragmentInterface(HomeFragmentInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }

    public HomeViewModel(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        stateLiveData = new MutableLiveData<>();
        stateLiveData.setValue("انتخاب شهر");
        cityLiveData = new MutableLiveData<>();
        stateProgressBar = new MutableLiveData<>();
        stateProgressBar.setValue(false); //do not show progress bar
        stateWatingOrNoConnection = new MutableLiveData<>();
        stateWatingOrNoConnection.setValue(false); // dont show try again btn textView

    }

    public void setMapDialogInterface(MapDialogInterface mapDialogInterface) {
        this.mapDialogInterface = mapDialogInterface;
    }

    public void searchRestaurantOnClick() {
        fragmentInterface.searchRestaurantOnClick();
    }

    public void selectCityOnClick() {
        rx.Observable<GetAllStatesResponse> observable = restaurantRepository.getAllStatesResponseObservable();
        if (observable != null) {
            stateProgressBar.setValue(true); //show progress bar
            observable.retry(1).subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllStatesResponse>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    stateProgressBar.setValue(false); //do not show progress bar

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
                public void onNext(GetAllStatesResponse getAllStatesResponse) {
                    stateProgressBar.setValue(false); //do not show progress bar
                    fragmentInterface.showStateCityCustomDialog(getAllStatesResponse.getAllStatesLists());
                }
            });
        }
    }

    public void getCities(long stateId) {
        rx.Observable<GetAllCitiesResponse> observable = restaurantRepository.getAllCitiesResponseObservable(stateId);
        if (observable != null) {
            observable.retry(1).subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllCitiesResponse>() {
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
                            fragmentInterface.onFailure(d.getMessage());
                            Log.i(TAG, "" + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetAllCitiesResponse getAllCitiesResponse) {
                    cityLiveData.setValue(getAllCitiesResponse.getAllCitiesLists().get(0).getName());//set the main province city if dont select any city
                    fragmentInterface.onSuccessGetCities(getAllCitiesResponse.getAllCitiesLists());
                }
            });
        }
    }

    public void tryAgainOnClick() {
        getCategories();
    }

    public void searchRestaurantsByScoreOnClick() {
        fragmentInterface.searchRestaurantsByScore();
    }

    public void searchRestaurantsByNewestDateOnClick() {
        fragmentInterface.searchRestaurantsByNewestDate();
    }

    public void getCategories() {
        rx.Observable<FoodCategoriesResponse> observable = restaurantRepository.getCategoriesResponseObservable();
        if (observable != null) {
            fragmentInterface.onStarted();
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<FoodCategoriesResponse>() {
                @Override
                public void onCompleted() {
                    stateWatingOrNoConnection.setValue(false); //do not show tryAgain button view
                }

                @Override
                public void onError(Throwable e) {
                    fragmentInterface.tryAgain();
                    stateWatingOrNoConnection.setValue(true); //show tryAgain view
                    if (e instanceof NoNetworkConnectionException)
                        fragmentInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            fragmentInterface.onFailure(jsonObject.getString("message"));

                        } catch (Exception d) {
//                            fragmentInterface.onFailure(d.getMessage());
                            Log.e(TAG, "" + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(FoodCategoriesResponse categoriesResponse) {

                    fragmentInterface.onSuccess(categoriesResponse.getCategories());
                }
            });
        }
    }

    public void getReverseAddressParsimap(Double latitude, Double longitude) {
        Observable<ReverseFindAddressResponse> observable = restaurantRepository.getReverseFindAddressResponse(latitude, longitude);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ReverseFindAddressResponse>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    fragmentInterface.onFailure(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        fragmentInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.e(TAG, "" + jsonObject.getString("message"));

                        } catch (Exception d) {
                            Log.e(TAG, "" + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(ReverseFindAddressResponse reverseFindAddressResponse) {
                    fragmentInterface.setCityAndState(reverseFindAddressResponse.getState(), reverseFindAddressResponse.getCity());

                }
            });
        }
    }

    public void openMapDialog(){
        fragmentInterface.openMapDialog();
    }

    public void selectMapPositionBtnClick() {
        fragmentInterface.searchRestaurantsBySelectedLocationDate(lat,lon);
    }

    public void setLocationForSearch(double latitude, double longitude) {
        lat = latitude;
        lon= longitude;
        mapDialogInterface.onSuccess();
    }
}
