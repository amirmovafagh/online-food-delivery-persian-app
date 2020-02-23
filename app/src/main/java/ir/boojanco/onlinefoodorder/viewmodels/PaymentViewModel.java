package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ir.boojanco.onlinefoodorder.data.database.CartItem;
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
    private Context context;
    private int tempTotalDiscountedPrice = 0;
    private int finalPaymentPrice = 0;

    private RestaurantRepository restaurantRepository;
    private ArrayList<FinalPaymentPrice> finalPaymentPrices;
    private List<CartItem> cartItems;

    public MutableLiveData<String> discountCodeLiveData;
    public MutableLiveData<String> totalAllPriceLiveData;
    private int totalAllPrice = 0;
    public MutableLiveData<String> totalDiscountLiveData;
    private int totalDiscount = 0;
    public MutableLiveData<String> packingCostLiveData;
    private int packingCost = 0;
    public MutableLiveData<String> restaurantShippingCostLiveData;
    private int shippingCost = 0;
    public MutableLiveData<String> taxAndServiceLiveData;
    private int taxAndService = 0;
    public MutableLiveData<String> totalRawPriceLiveData;
    private int totalRawPrice = 0;
    public PaymentInterface paymentInterface;

    public PaymentViewModel(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;

        totalAllPriceLiveData = new MutableLiveData<>();
        totalDiscountLiveData = new MutableLiveData<>();
        packingCostLiveData = new MutableLiveData<>();
        restaurantShippingCostLiveData = new MutableLiveData<>();
        taxAndServiceLiveData = new MutableLiveData<>();
        totalRawPriceLiveData = new MutableLiveData<>();
        discountCodeLiveData = new MutableLiveData<>();
    }


    public void checkDiscountCode(String authKey) {
        paymentInterface.onStarted();
        rx.Observable<DiscountCodeResponse> observable = restaurantRepository.getDiscountCodeResponse(authKey, "bj-F8GX0P", 467, 100000);
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
                    calculateDiscountCode(getDiscountCode);
                    Log.i(TAG, "" + getDiscountCode.getDiscountPercent());
                }
            });
        }
    }

    private void calculateDiscountCode(DiscountCodeResponse discountCode) {
        tempTotalDiscountedPrice = 0;
        int allDiscountsCost = 0;
        double discount = 100 - discountCode.getDiscountPercent();
        discount = discount / 100;
        if (finalPaymentPrices != null && finalPaymentPrices.size() > 0) {// not null so dont use the packages

            if (discountCode.isForAllFoods()) {
                /*in this section we must check each price of foods  because the discount will apply on
                 specific food discount at Previous step*/
                for (int i = 0; i < finalPaymentPrices.size(); i++) {
                    tempTotalDiscountedPrice += finalPaymentPrices.get(i).getDiscountedPrice();
                }
                tempTotalDiscountedPrice = (int) (tempTotalDiscountedPrice * discount);
                Log.i(TAG, "" + (totalRawPrice - tempTotalDiscountedPrice));

                if (totalRawPrice - tempTotalDiscountedPrice >= discountCode.getMaximumDiscountAmount()) {
                    tempTotalDiscountedPrice = totalRawPrice - discountCode.getMaximumDiscountAmount();
                    allDiscountsCost = (totalRawPrice - tempTotalDiscountedPrice) + totalDiscount;
                    finalPaymentPrice = (totalRawPrice - allDiscountsCost) + packingCost + taxAndService + shippingCost;
                } else {
                    allDiscountsCost = (totalRawPrice - tempTotalDiscountedPrice) + totalDiscount;
                    finalPaymentPrice = (totalRawPrice -allDiscountsCost) + packingCost + taxAndService + shippingCost;
                }

            } else {

            }
        }else {//use the packages
            //in this section we must divide on total raw price the discount apply on raw price of food
            if (discountCode.isForAllFoods()){
                tempTotalDiscountedPrice = (int) (totalRawPrice * discount);
                if(totalRawPrice - tempTotalDiscountedPrice >= discountCode.getMaximumDiscountAmount()){
                    tempTotalDiscountedPrice = totalRawPrice - discountCode.getMaximumDiscountAmount();
                    allDiscountsCost = (totalRawPrice - tempTotalDiscountedPrice) + totalDiscount;
                    finalPaymentPrice = (totalRawPrice - allDiscountsCost) + packingCost + taxAndService + shippingCost;
                }else {
                    allDiscountsCost = (totalRawPrice - tempTotalDiscountedPrice) + totalDiscount;
                    finalPaymentPrice = (totalRawPrice -allDiscountsCost) + packingCost + taxAndService + shippingCost;
                }
            }

        }
        totalDiscountLiveData.setValue(moneyFormat(allDiscountsCost));
        totalAllPriceLiveData.setValue(moneyFormat(finalPaymentPrice));
    }

    public void setVariablesInTempVar(ArrayList<FinalPaymentPrice> finalPaymentPrices, List<CartItem> cartItems,
                                      int totalAllPrice, int totalRawPrice, int totalDiscount, int packingCost,
                                      int taxAndService, int shippingCost) {
        if (finalPaymentPrices != null)
            this.finalPaymentPrices = finalPaymentPrices;
        this.cartItems = cartItems;
        this.totalAllPrice = totalAllPrice;
        this.totalRawPrice = totalRawPrice;
        this.totalDiscount = totalDiscount;
        this.packingCost = packingCost;
        this.taxAndService = taxAndService;
        this.shippingCost = shippingCost;

        totalAllPriceLiveData.setValue(moneyFormat(totalAllPrice));
        totalRawPriceLiveData.setValue(moneyFormat(totalRawPrice));
        totalDiscountLiveData.setValue(moneyFormat(totalDiscount));
        packingCostLiveData.setValue(moneyFormat(packingCost));
        taxAndServiceLiveData.setValue(moneyFormat(taxAndService));
        restaurantShippingCostLiveData.setValue(moneyFormat(shippingCost));

    }

    private String moneyFormat(int cost) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber + " تومان";
    }


}
