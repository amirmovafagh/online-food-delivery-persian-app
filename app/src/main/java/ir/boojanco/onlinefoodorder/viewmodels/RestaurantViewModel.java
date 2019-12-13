package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFragmentInterface;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    public RestaurantFragmentInterface restaurantInterface;
    private RestaurantRepository restaurantRepository;
    private Context context;
    private MutableLiveData<LastRestaurantResponse> responseMutableLiveData ;

    public RestaurantViewModel(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public void getAllRestaurant(){
        Observable<LastRestaurantResponse> observable = restaurantRepository.getLastRestaurant();
        if(observable != null){
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LastRestaurantResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(LastRestaurantResponse lastRestaurantResponse) {

                    restaurantInterface.onSuccess();
                }
            });
        }
    }
}
