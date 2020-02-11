package ir.boojanco.onlinefoodorder.ui.activities.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityCartBinding;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.RestaurantPackageItem;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.ui.fragments.MapDialogFragment;
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

    private CoordinatorLayout coordinatorLayoutMainContent;
    private CardView cardExpandClick;
    private LinearLayout expandableLayout;
    private AutoTransition transition;


    private RecyclerView recyclerViewCart, recyclerViewUserAddress;
    private CartAdapter cartAdapter;
    private AddressAdapter addressAdapter;

    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private DialogFragment dialogFragment;

    private final String selectedPackageExtraName = "selectedPackage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);
        cartViewModel = new ViewModelProvider(this, factory).get(CartViewModel.class);
        cartViewModel.cartInterface = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        binding.setViewModel(cartViewModel);
        binding.setLifecycleOwner(this);

        bottom_sheet = binding.bottomSheet.bottomSheetAddNewAddress;
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        transition = new AutoTransition();
        coordinatorLayoutMainContent = binding.mainContent;
        cardExpandClick = binding.cvCartDetailsPropertyIcon;
        expandableLayout = binding.linearLayoutCartDetailsView;

        cardExpandClick.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(coordinatorLayoutMainContent, transition);
                expandableLayout.setVisibility(View.VISIBLE);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                TransitionManager.beginDelayedTransition(coordinatorLayoutMainContent, transition);
                expandableLayout.setVisibility(View.GONE);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                //arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.addToBackStack(null);
        dialogFragment = new MapDialogFragment();
        //dialogFragment.show(fragmentTransaction, "dialog");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long extraRestauranId = extras.getLong("RESTAURANT_ID", 0);
            RestaurantPackageItem packageItem = (RestaurantPackageItem) extras.getSerializable(selectedPackageExtraName);

            if (packageItem != null)
                cartViewModel.setPackageItem(packageItem);
            /*String extraRestauranLogo = extras.getString("RESTAURANT_LOGO"," ");
            String extraRestauranName = extras.getString("RESTAURANT_NAME"," ");
            String extraRestauranTagList = extras.getString("RESTAURANT_TAG_LIST"," ");
            Float extraRestauranAverageScore = extras.getFloat("RESTAURANT_AVERAGE_SCORE",0);
            restaurantDetailsViewModel.restaurantCover.setValue(extraRestauranCover);
            restaurantDetailsViewModel.restaurantLogo.setValue(extraRestauranLogo);
            restaurantDetailsViewModel.restaurantAverageScore.setValue(extraRestauranAverageScore);
            restaurantDetailsViewModel.restaurantName.setValue(extraRestauranName);*/
            recyclerViewUserAddress = binding.recyclerViewUserAddressHorizontalItems;
            recyclerViewUserAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewUserAddress.setHasFixedSize(true);
            addressAdapter = new AddressAdapter(this);
            recyclerViewUserAddress.setAdapter(addressAdapter);


            recyclerViewCart = binding.recyclerViewRestaurantCartItems;
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCart.setHasFixedSize(true);
            cartAdapter = new CartAdapter(this, cartDataSource);
            recyclerViewCart.setAdapter(cartAdapter);
            cartViewModel.getUserAddress(sharedPreferences.getUserAuthTokenKey());
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
    public void onSuccessGetAddress(List<UserAddressList> addressLists) {
        addressAdapter.setAddressLists(addressLists);
    }

    @Override
    public void onSuccessGetReverseAddress(ReverseFindAddressResponse reverseFindAddressResponses) {

    }

    @Override
    public void onFailure(String Error) {

    }

    @Override
    public void onRecyclerViewItemClick(View v, CartItem cartItem) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cartViewModel != null)
            cartViewModel.onStop();
    }
}
