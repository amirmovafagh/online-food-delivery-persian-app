package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.CartInterface;

public class CartViewModel extends ViewModel {
    private String TAG = this.getClass().getSimpleName();

    public CartInterface cartInterface;
    private Context context;
    private CartDataSource cartDataSource;
    private CompositeDisposable compositeDisposableGetAllItems;


    public MutableLiveData<Long> totalItemsPriceLiveData;
    public MutableLiveData<String> cartStateLiveData;
    public CartViewModel(Context context, CartDataSource cartDataSource) {
        this.context = context;
        this.cartDataSource = cartDataSource;

        cartStateLiveData = new MutableLiveData<>();
        totalItemsPriceLiveData = new MutableLiveData<>();
        compositeDisposableGetAllItems = new CompositeDisposable();
    }

    public void getAllItemInCart(long restaurantId){
        compositeDisposableGetAllItems.add(cartDataSource.getAllCart(restaurantId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(cartItems -> {
            if (cartItems.isEmpty()){
                cartStateLiveData.setValue(context.getString(R.string.empty_cart));
                Log.d(TAG,"am1");
            }

            else {
                cartInterface.onSuccess(cartItems);
                Log.d(TAG,"am2");
            }
            calculateCartTotalPrice(restaurantId);

        }, throwable -> {
            Toast.makeText(context, "{GET ALL Cart}"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }));
    }

    private void calculateCartTotalPrice(Long restaurantId){
        cartDataSource.sumPrice(restaurantId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Long aLong) {
                totalItemsPriceLiveData.setValue(aLong);

            }

            @Override
            public void onError(Throwable e) {
                totalItemsPriceLiveData.setValue((long) 0);
                Log.e(TAG,"{UPDATE CART ITEM}-> "+e.getMessage());
            }
        });
    }

    public void onStop(){
        if(compositeDisposableGetAllItems !=null)
            compositeDisposableGetAllItems.clear();
    }
}
