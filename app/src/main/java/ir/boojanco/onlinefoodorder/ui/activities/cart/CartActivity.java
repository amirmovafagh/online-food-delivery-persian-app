package ir.boojanco.onlinefoodorder.ui.activities.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityCartBinding;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.RestaurantPackageItem;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.ui.activities.payment.PaymentActivity;
import ir.boojanco.onlinefoodorder.ui.fragments.MapDialogCartFragment;
import ir.boojanco.onlinefoodorder.viewmodels.CartViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.CartViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.AddressRecyclerViewInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.CartInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogInterface;


public class CartActivity extends AppCompatActivity implements CartInterface, RecyclerViewCartClickListener, StateCityDialogInterface, AddressRecyclerViewInterface {
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
    private ImageButton arrowBtn;
    private LinearLayout expandableLayout;
    private AutoTransition transition;


    private RecyclerView recyclerViewCart, recyclerViewUserAddress;
    private CartAdapter cartAdapter;
    private AddressAdapter addressAdapter;

    private BottomSheetBehavior sheetBehavior;
    private NestedScrollView bottom_sheet;
    private LinearLayout acceptOrder;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private DialogFragment mapFragment;
    private CityAdapter cityAdapter;
    private CustomStateCityDialog stateCityDialog;
    private Intent goToPaymentActivity;

    private final String selectedPackageExtraName = "selectedPackage";
    private final String restaurantInfoResponseExtraName = "restaurantInfoResponse";
    private final String cartItemExtraName = "cartItem";
    private final String finalPaymentPricesExtraName = "finalPaymentPrices";
    private final String totalAllPriceExtraName = "totalAllPrice";
    private final String totalRawPriceExtraName = "totalRaw";
    private final String totalDiscountExtraName = "totalDiscount";
    private final String packingCostLiveDataExtraName = "packingCost";
    private final String restaurantShippingCostExtraName = "restaurantShipping";
    private final String taxAndServiceExtraName = "taxAndService";
    private final String shipingCostExtraName = "shipingCost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);
        cartViewModel = new ViewModelProvider(this, factory).get(CartViewModel.class);
        cartViewModel.cartInterface = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        binding.setViewModel(cartViewModel);
        binding.setLifecycleOwner(this);
        bottom_sheet = binding.bottomSheet;
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        transition = new AutoTransition();
        coordinatorLayoutMainContent = binding.mainContent;
        arrowBtn = binding.imgBtnExpandArrow;
        expandableLayout = binding.linearLayoutCartDetailsView;
        acceptOrder = binding.linearLayoutAcceptOrder;

        arrowBtn.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(coordinatorLayoutMainContent, transition);
                expandableLayout.setVisibility(View.VISIBLE);
                //sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                TransitionManager.beginDelayedTransition(coordinatorLayoutMainContent, transition);
                expandableLayout.setVisibility(View.GONE);
                //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });

        binding.bottomSheetInclude.switcherDefaultAddress.setOnCheckedChangeListener(aBoolean -> {
            Toast.makeText(application, ""+aBoolean, Toast.LENGTH_SHORT).show();
            cartViewModel.defaultAddress = aBoolean;
            return null;
        });

        acceptOrder.setOnClickListener(v -> {

        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long extraRestauranId = extras.getLong("RESTAURANT_ID", 0);
            RestaurantPackageItem packageItem = (RestaurantPackageItem) extras.getSerializable(selectedPackageExtraName);
            RestaurantInfoResponse restaurantInfo = (RestaurantInfoResponse) extras.getSerializable(restaurantInfoResponseExtraName);

            if (packageItem != null)
                cartViewModel.setPackageItem(packageItem);
            if (restaurantInfo != null)
                cartViewModel.setRestaurantInfo(restaurantInfo);
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
            addressAdapter = new AddressAdapter(this, application, true);
            recyclerViewUserAddress.setAdapter(addressAdapter);
            recyclerViewCart = binding.recyclerViewRestaurantCartItems;
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCart.setHasFixedSize(true);
            cartAdapter = new CartAdapter(this, cartDataSource);
            recyclerViewCart.setAdapter(cartAdapter);
            cartViewModel.getUserAddress(sharedPreferences.getUserAuthTokenKey());
            cartViewModel.getAllItemInCart(extraRestauranId);

            cartViewModel.userAddressPagedListLiveData.observe(this, userAddressLists -> addressAdapter.submitList(userAddressLists)); //set PagedList user address

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
    public void onSuccessGetAddress() {


    }

    @Override
    public void onSuccessGetStates(List<AllStatesList> allStatesLists) {

        StateAdapter stateAdapter = new StateAdapter(this, this);
        cityAdapter = new CityAdapter(this, this);
        stateCityDialog = new CustomStateCityDialog(CartActivity.this, stateAdapter, allStatesLists, cityAdapter);
        stateCityDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onSuccessGetcities(List<AllCitiesList> allCitiesLists) {
        cityAdapter.setCitiesLists(allCitiesLists);
    }

    @Override
    public void showAddressBottomSheet() {
        mapFragment.dismiss();
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void showStateCityCustomDialog() {
        if (stateCityDialog != null)
            stateCityDialog.show();
        else Toast.makeText(application, "خطا در دریافت اطلاعات استان ها", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMapDialogFragment() {
        cartViewModel.getStates(sharedPreferences.getUserAuthTokenKey()); //get States once

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.addToBackStack(null);
        mapFragment = new MapDialogCartFragment();
        mapFragment.show(fragmentTransaction, "dialog");
    }

    @Override
    public void acceptOrder(ArrayList<FinalPaymentPrice> finalPaymentPrices, List<CartItem> cartItems, int totalAllPrice, int totalRawPrice, int totalDiscount, int packingCost, int taxAndService, int shippingCost) {
        if(cartItems != null){
            goToPaymentActivity = new Intent(this, PaymentActivity.class);
            goToPaymentActivity.putExtra(cartItemExtraName, (Serializable) cartItems);
            goToPaymentActivity.putExtra(finalPaymentPricesExtraName, finalPaymentPrices);
            goToPaymentActivity.putExtra(totalAllPriceExtraName, totalAllPrice);
            goToPaymentActivity.putExtra(totalRawPriceExtraName,  totalRawPrice);
            goToPaymentActivity.putExtra(totalDiscountExtraName,  totalDiscount);
            goToPaymentActivity.putExtra(packingCostLiveDataExtraName,  packingCost);
            goToPaymentActivity.putExtra(taxAndServiceExtraName,  taxAndService);
            goToPaymentActivity.putExtra(shipingCostExtraName,  shippingCost);
            startActivity(goToPaymentActivity);
        }
    }


    @Override
    public void onSuccessGetReverseAddress(ReverseFindAddressResponse reverseFindAddressResponses) {

    }

    @Override
    public void onFailure(String Error) {
        Toast.makeText(application, ""+Error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecyclerViewItemClick(View v, CartItem cartItem) {

    }

    @Override
    public void onRecyclerViewAddressClick(View v, UserAddressList userAddress) {
        cartViewModel.checkUserAddressForService(userAddress.getLatitude(), userAddress.getLongitude());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cartViewModel != null)
            cartViewModel.onStop();
    }


    @Override
    public void onStateItemClick(AllStatesList state) {
        cartViewModel.stateId = state.getId();
        cartViewModel.state.setValue(state.getName());
        cartViewModel.city.setValue(null);
        cartViewModel.getCities(sharedPreferences.getUserAuthTokenKey(), state.getId());
    }

    @Override
    public void onCityItemClick(AllCitiesList city) {
        cartViewModel.cityId = city.getId();
        cartViewModel.city.setValue(city.getName());
    }


}
