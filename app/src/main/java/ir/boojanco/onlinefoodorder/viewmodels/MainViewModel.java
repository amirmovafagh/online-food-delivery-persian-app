package ir.boojanco.onlinefoodorder.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.MainActivityInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainViewModel extends ViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private RestaurantRepository restaurantRepository;
    private MainActivityInterface activityInterface;

    public void setActivityInterface(MainActivityInterface activityInterface) {
        this.activityInterface = activityInterface;
    }

    public MainViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void getReverseAddressParsimap(Double latitude, Double longitude) {
        Observable<ReverseFindAddressResponse> observable = restaurantRepository.getReverseFindAddressResponse(latitude, longitude);
        if (observable != null) {
            activityInterface.onStarted();
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ReverseFindAddressResponse>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    activityInterface.onFailure(e.getMessage());
                    if (e instanceof NoNetworkConnectionException)
                        //fragmentInterface.onFailure(e.getMessage());
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
                    activityInterface.onSuccess(reverseFindAddressResponse.getState(), reverseFindAddressResponse.getCity());

                }
            });
        }
    }

}
