package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;



import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFragmentInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    public RestaurantFragmentInterface restaurantInterface;
    private RestaurantRepository restaurantRepository;
    private Context context;
    public MutableLiveData<LastRestaurantResponse> responseMutableLiveData ;

    public RestaurantViewModel(Context context, RestaurantRepository restaurantRepository) {
        responseMutableLiveData = new MutableLiveData<>();
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public void getAllRestaurant(String authToken){
        Observable<LastRestaurantResponse> observable = restaurantRepository.getLastRestaurant(authToken);
        if(observable != null){
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LastRestaurantResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if(e instanceof NoNetworkConnectionException)
                        restaurantInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            restaurantInterface.onFailure(jsonObject.getString("message"));


                        }  catch (Exception d) {
                            restaurantInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(LastRestaurantResponse lastRestaurantResponse) {
                    restaurantInterface.onStarted();
                    responseMutableLiveData.setValue(lastRestaurantResponse);
                    restaurantInterface.onSuccess(responseMutableLiveData);
                }
            });
        }
    }
}
