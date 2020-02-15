package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.DiscountCodeResponse;
import ir.boojanco.onlinefoodorder.ui.activities.cart.PaymentInterface;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Subscriber;


public class PaymentViewModel extends ViewModel {
    private String TAG = this.getClass().getSimpleName();
    private  Context context;

    private RestaurantRepository restaurantRepository;

    public PaymentInterface paymentInterface;
    public PaymentViewModel(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public void checkDiscountCode(String authKey, String code){

        rx.Observable<DiscountCodeResponse> observable = restaurantRepository.getDiscountCodeResponse(authKey, code);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<DiscountCodeResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        paymentInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            paymentInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            paymentInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(DiscountCodeResponse getDiscountCode) {

                    Log.i(TAG, ""+getDiscountCode.getDiscountPercent());
                }
            });
        }
    }
}
