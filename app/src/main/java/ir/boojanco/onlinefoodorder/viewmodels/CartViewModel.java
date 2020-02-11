package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.RestaurantPackageItem;
import ir.boojanco.onlinefoodorder.models.user.GetUserAddressResponse;
import ir.boojanco.onlinefoodorder.ui.activities.cart.FinalPaymentPrice;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.CartInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Subscriber;

public class CartViewModel extends ViewModel {
    private String TAG = this.getClass().getSimpleName();

    public CartInterface cartInterface;
    private Context context;
    private CartDataSource cartDataSource;
    private CompositeDisposable compositeDisposableGetAllItems;
    private UserRepository userRepository;
    private RestaurantPackageItem packageItem;
    private List<CartItem> cartItems;
    private ArrayList<FinalPaymentPrice> finalPaymentPrices;

    public MutableLiveData<Long> totalItemsPriceLiveData;
    public MutableLiveData<String> cartStateLiveData;
    public MutableLiveData<String> city;
    public MutableLiveData<String> region;
    public MutableLiveData<String> exactAddress;
    public MutableLiveData<String> totalAllPriceLiveData;
    public MutableLiveData<String> totalDiscountLiveData;

    private int tempTotalAllPrice = 0;

    public CartViewModel(Context context, CartDataSource cartDataSource, UserRepository userRepository) {
        this.context = context;
        this.cartDataSource = cartDataSource;
        this.userRepository = userRepository;

        city = new MutableLiveData<>();
        region = new MutableLiveData<>();
        exactAddress = new MutableLiveData<>();
        cartStateLiveData = new MutableLiveData<>();
        totalItemsPriceLiveData = new MutableLiveData<>();
        totalAllPriceLiveData = new MutableLiveData<>();
        totalDiscountLiveData = new MutableLiveData<>();

        compositeDisposableGetAllItems = new CompositeDisposable();
    }

    public void setPackageItem(RestaurantPackageItem packageItem) {
        this.packageItem = packageItem;
    }

    public void getReverseAddressParsiMap(Double latitude, Double longitude) {
        rx.Observable<ReverseFindAddressResponse> observable = userRepository.getReverseFindAddressResponse(latitude, longitude);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<ReverseFindAddressResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        cartInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            cartInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            cartInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(ReverseFindAddressResponse reverseFindAddressResponse) {

                    city.setValue(reverseFindAddressResponse.getPrefix());
                    region.setValue(reverseFindAddressResponse.getLocalAddress());
                    Toast.makeText(context, "" + reverseFindAddressResponse.getLocalAddress(), Toast.LENGTH_SHORT).show();
                    //cartInterface.onSuccessGetReverseAddress(reverseFindAddressResponse);
                }
            });
        }
    }

    public void getUserAddress(String authToken) {
        rx.Observable<GetUserAddressResponse> observable = userRepository.getUserAddressResponseObservable(authToken);
        if (observable != null) {
            observable.subscribeOn(rx.schedulers.Schedulers.io()).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetUserAddressResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        cartInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());

                            cartInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            cartInterface.onFailure(d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(GetUserAddressResponse getUserAddressResponse) {


                    cartInterface.onSuccessGetAddress(getUserAddressResponse.getUserAddressLists());
                }
            });
        }
    }

    public void getAllItemInCart(long restaurantId) {
        compositeDisposableGetAllItems.add(cartDataSource.getAllCart(restaurantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    if (cartItems.isEmpty()) {
                        cartStateLiveData.setValue(context.getString(R.string.empty_cart));

                    } else {
                        cartInterface.onSuccess(cartItems);
                        this.cartItems = cartItems;


                    }
                    calculateCartTotalPrice(restaurantId);
                }, throwable -> {
                    Toast.makeText(context, "{GET ALL Cart}" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }));
    }

    public void calculateFinalCartTotalPrice() {
        finalPaymentPrices = new ArrayList<>();
        int totalPrice = 0;
        if (cartItems != null)
            for (int i = 0; i < cartItems.size(); i++) {
                int count = cartItems.get(i).getFoodQuantity();
                long id = cartItems.get(i).getFoodId();
                double price = count * cartItems.get(i).getFoodPrice();

                if (packageItem == null) { //not selected any discountRestaurantPackage
                    double discount = 100 - cartItems.get(i).getFoodDiscount();
                    discount = discount / 100;
                    price = price * discount;
                    totalPrice += price;
                    tempTotalAllPrice = (int) totalPrice;
                    totalAllPriceLiveData.setValue(moneyFormat((int) totalPrice));
                    FinalPaymentPrice paymentPrice = new FinalPaymentPrice();
                    paymentPrice.setId(id);
                    paymentPrice.setDiscountedPrice((int) price);
                    paymentPrice.setName(cartItems.get(i).getFoodName());
                    finalPaymentPrices.add(paymentPrice);
                } else { //use discountRestaurantPackage
                    double pDiscount = 100- packageItem.getDiscountPercent();
                    pDiscount = pDiscount / 100;
                    if(packageItem.isDiscountForAllFoods()){ // discount effect on all items
                        price = price * pDiscount;
                        totalPrice += price;
                        if(totalItemsPriceLiveData != null &&  totalItemsPriceLiveData.getValue() - (long)totalPrice >= packageItem.getMaximumDiscountAmount()){
                            tempTotalAllPrice =(int)(totalItemsPriceLiveData.getValue() -(long)packageItem.getMaximumDiscountAmount());
                            totalAllPriceLiveData.setValue(moneyFormat((int)(totalItemsPriceLiveData.getValue() -(long)packageItem.getMaximumDiscountAmount())));
                        }else {
                        tempTotalAllPrice = (int) totalPrice;
                        totalAllPriceLiveData.setValue(moneyFormat((int) totalPrice));}

                    }else {

                    }
                }
            }
    }


    private void calculateCartTotalPrice(Long restaurantId) {
        cartDataSource.sumPrice(restaurantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Long aLong) {
                totalItemsPriceLiveData.setValue(aLong);
                calculateFinalCartTotalPrice();
                totalDiscountLiveData.setValue(String.valueOf(aLong - tempTotalAllPrice));
            }

            @Override
            public void onError(Throwable e) {
                totalItemsPriceLiveData.setValue((long) 0);
                totalAllPriceLiveData.setValue("سبد خرید خالی است");
                totalDiscountLiveData.setValue("");
                Log.e(TAG, "{UPDATE CART ITEM}-> " + e.getMessage());
            }
        });
    }

    public void onStop() {
        if (compositeDisposableGetAllItems != null)
            compositeDisposableGetAllItems.clear();
    }

    private String moneyFormat(int cost) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(cost);
        return formattedNumber + " تومان";
    }
}
