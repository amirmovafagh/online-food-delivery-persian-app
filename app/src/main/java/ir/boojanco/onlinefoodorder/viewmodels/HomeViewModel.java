package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.food.FoodCategoriesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllCitiesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.ui.fragments.home.HomeFragment;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.HomeFragmentInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Subscriber;

public class HomeViewModel extends ViewModel {
    private String TAG = HomeFragment.class.getSimpleName();
    private Context context;
    private RestaurantRepository restaurantRepository;
    private HomeFragmentInterface fragmentInterface;
    private String userAuthToken;
    public MutableLiveData<String> cityLiveData;
    public MutableLiveData<String> stateLiveData;

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
        cityLiveData = new MutableLiveData<>();
        stateLiveData.setValue("انتخاب شهر");

    }

    public void selectCityOnClick() {
        rx.Observable<GetAllStatesResponse> observable = restaurantRepository.getAllStatesResponseObservable(userAuthToken);
        if (observable != null) {
            observable.retry(3).subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllStatesResponse>() {
                @Override
                public void onCompleted(){
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
                            Log.i(TAG,""+d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetAllStatesResponse getAllStatesResponse) {
                    fragmentInterface.showStateCityCustomDialog(getAllStatesResponse.getAllStatesLists());
                }
            });
        }
    }

    public void getCities(String userAuthTokenKey, long stateId) {
        rx.Observable<GetAllCitiesResponse> observable = restaurantRepository.getAllCitiesResponseObservable(userAuthToken, stateId);
        if (observable != null) {
            observable.retry(3).subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllCitiesResponse>() {
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
                            Log.i(TAG,""+d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetAllCitiesResponse getAllCitiesResponse) {

                    fragmentInterface.onSuccessGetCities(getAllCitiesResponse.getAllCitiesLists());
                }
            });
        }
    }

    public void getCategories() {
        rx.Observable<FoodCategoriesResponse> observable = restaurantRepository.getCategoriesResponseObservable();
        if (observable != null) {
            fragmentInterface.onStarted();
            observable.retry(3).subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<FoodCategoriesResponse>() {
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
                public void onNext(FoodCategoriesResponse categoriesResponse) {

                    fragmentInterface.onSuccess(categoriesResponse.getCategories());
                }
            });
        }
    }
}
