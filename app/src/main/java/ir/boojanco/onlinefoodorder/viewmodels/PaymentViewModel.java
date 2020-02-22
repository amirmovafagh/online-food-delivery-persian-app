package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.DiscountCodeResponse;
import ir.boojanco.onlinefoodorder.ui.activities.cart.FinalPaymentPrice;
import ir.boojanco.onlinefoodorder.ui.activities.payment.PaymentInterface;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Subscriber;


public class PaymentViewModel extends ViewModel {
    private String TAG = this.getClass().getSimpleName();
    private  Context context;

    private RestaurantRepository restaurantRepository;
    public ArrayList<FinalPaymentPrice> finalPaymentPrices;

    public MutableLiveData<String>  discountCodeLiveData;
    public MutableLiveData<String>  totalAllPriceLiveData;
    public MutableLiveData<String>  totalDiscountLiveData;
    public MutableLiveData<Integer> packingCostLiveData;
    public MutableLiveData<String>  restaurantShippingCostLiveData;
    public MutableLiveData<Integer> taxAndServiceLivedata;
    public MutableLiveData<Long>    totalRawPriceLiveData;
    public PaymentInterface paymentInterface;
    public PaymentViewModel(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;

        totalAllPriceLiveData = new MutableLiveData<>();
        totalDiscountLiveData = new MutableLiveData<>();
        packingCostLiveData = new MutableLiveData<>();
        restaurantShippingCostLiveData = new MutableLiveData<>();
        taxAndServiceLivedata = new MutableLiveData<>();
        totalRawPriceLiveData = new MutableLiveData<>();
        discountCodeLiveData = new MutableLiveData<>();
    }

    public void checkDiscountCode(String authKey){
        paymentInterface.onStarted();
        rx.Observable<DiscountCodeResponse> observable = restaurantRepository.getDiscountCodeResponse(authKey, "bj-F8GX0P",467,100000);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<DiscountCodeResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    paymentInterface.onFailure(e.getMessage());
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
