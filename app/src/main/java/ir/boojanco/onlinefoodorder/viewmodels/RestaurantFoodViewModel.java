package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.food.getAllFood.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFoodInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestaurantFoodViewModel extends ViewModel {
    private static final String TAG = RestaurantFoodViewModel.class.getSimpleName();
    public RestaurantFoodInterface foodInterface;
    private Context context;
    private RestaurantRepository restaurantRepository;
    public MutableLiveData<GetAllFoodResponse> allFoodMutableLiveData;

    public RestaurantFoodViewModel(Context context, RestaurantRepository restaurantRepository) {
        allFoodMutableLiveData = new MutableLiveData<>();
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public void getAllFood(String authToken, int restaurantId){
        Observable<GetAllFoodResponse> observable = restaurantRepository.getAllFood(authToken, restaurantId);
        if(observable != null){
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetAllFoodResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        foodInterface.onFailure(e.getMessage());
                    if(e instanceof HttpException){
                        Response response = ((HttpException) e).response();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            foodInterface.onFailure(jsonObject.getString("message"));
                        }catch (Exception d){
                            foodInterface.onFailure(d.getMessage());
                        }
                    }

                }

                @Override
                public void onNext(GetAllFoodResponse getAllFoodResponse) {
                    foodInterface.onStarted();
                    allFoodMutableLiveData.setValue(getAllFoodResponse);
                    foodInterface.onSuccess(allFoodMutableLiveData);
                }
            });
        }
    }
}
