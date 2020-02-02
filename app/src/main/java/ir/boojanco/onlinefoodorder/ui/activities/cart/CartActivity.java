package ir.boojanco.onlinefoodorder.ui.activities.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityCartBinding;
import ir.boojanco.onlinefoodorder.viewmodels.CartViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.CartViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.CartInterface;

public class CartActivity extends AppCompatActivity implements CartInterface, RecyclerViewCartClickListener {
    CartViewModel cartViewModel;
    ActivityCartBinding binding;

    @Inject
    Application application;
    @Inject
    CartViewModelFactory factory;
    @Inject
    CartDataSource cartDataSource;
    @Inject
    MySharedPreferences sharedPreferences;

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);
        cartViewModel = new ViewModelProvider(this,factory).get(CartViewModel.class);
        cartViewModel.cartInterface = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        binding.setViewModel(cartViewModel);
        binding.setLifecycleOwner(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            long extraRestauranId = extras.getLong("RESTAURANT_ID",0);
            /*String extraRestauranLogo = extras.getString("RESTAURANT_LOGO"," ");
            String extraRestauranName = extras.getString("RESTAURANT_NAME"," ");
            String extraRestauranTagList = extras.getString("RESTAURANT_TAG_LIST"," ");
            Float extraRestauranAverageScore = extras.getFloat("RESTAURANT_AVERAGE_SCORE",0);
            restaurantDetailsViewModel.restaurantCover.setValue(extraRestauranCover);
            restaurantDetailsViewModel.restaurantLogo.setValue(extraRestauranLogo);
            restaurantDetailsViewModel.restaurantAverageScore.setValue(extraRestauranAverageScore);
            restaurantDetailsViewModel.restaurantName.setValue(extraRestauranName);*/

            recyclerView = binding.recyclerViewRestaurantCartItems;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            cartAdapter = new CartAdapter(this, cartDataSource);
            recyclerView.setAdapter(cartAdapter);

            cartViewModel.getAllItemInCart(extraRestauranId);
        }
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess(List<CartItem> cartItems) {
        cartAdapter.setCartItems(cartItems);
    }

    @Override
    public void onFailure(String Error) {

    }

    @Override
    public void onRecyclerViewItemClick(View v, CartItem cartItem) {

    }
}
