package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.CartInterface;

public class CartViewModel extends ViewModel {

    public CartInterface cartInterface;
    private Context context;
    private CartDataSource cartDataSource;
    private CompositeDisposable compositeDisposableGetAllItems;

    public MutableLiveData<String> cartStateLiveData;
    public CartViewModel(Context context, CartDataSource cartDataSource) {
        this.context = context;
        this.cartDataSource = cartDataSource;

        cartStateLiveData = new MutableLiveData<>();
        compositeDisposableGetAllItems = new CompositeDisposable();
    }

    public void getAllItemInCart(long restaurantId){
        compositeDisposableGetAllItems.add((Disposable) cartDataSource.getAllCart(restaurantId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(cartItems -> {
            if (cartItems.isEmpty())
                cartStateLiveData.setValue(context.getString(R.string.empty_cart));
            else {
                cartInterface.onSuccess(cartItems);
            }
        }, throwable -> {
            Toast.makeText(context, "{GET ALL Cart}"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }));
    }
}
