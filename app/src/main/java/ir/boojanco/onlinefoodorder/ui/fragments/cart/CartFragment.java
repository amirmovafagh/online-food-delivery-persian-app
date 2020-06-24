package ir.boojanco.onlinefoodorder.ui.fragments.cart;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.data.database.RestaurantItem;
import ir.boojanco.onlinefoodorder.databinding.CartFragmentBinding;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.RestaurantPackageItem;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.ui.fragments.MapDialogCartFragment;
import ir.boojanco.onlinefoodorder.util.OrderType;
import ir.boojanco.onlinefoodorder.viewmodels.CartViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.CartViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.AddressRecyclerViewInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.CartInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogInterface;

public class CartFragment extends Fragment implements CartInterface, RecyclerViewCartClickListener, StateCityDialogInterface, AddressRecyclerViewInterface {
    private final static String TAG = CartFragment.class.getSimpleName();
    @Inject
    Application application;
    @Inject
    CartViewModelFactory factory;
    @Inject
    CartDataSource cartDataSource;
    @Inject
    MySharedPreferences sharedPreferences;

    private CartViewModel viewModel;
    private CartFragmentBinding binding;

    private MaterialButtonToggleGroup materialDeliveryBtnGroup;
    private CoordinatorLayout coordinatorLayoutMainContent;
    private ImageButton arrowBtn;
    private LinearLayout expandableLayout;
    private AutoTransition transition;

    private RecyclerView recyclerViewCart, recyclerViewUserAddress, recyclerViewRestaurantsCart;
    private CartAdapter cartAdapter;
    private RestaurantsCartAdapter restaurantsCartAdapter;
    private AddressAdapter addressAdapter;

    private BottomSheetBehavior sheetBehavior;
    private NestedScrollView bottom_sheet;
    private LinearLayout acceptOrder;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private DialogFragment mapFragment;
    private CityAdapter cityAdapter;
    private CustomStateCityDialog stateCityDialog;
    private Bundle bundlePaymentFragment;

    private Toolbar toolbar;
    private NavController navController;
    private final String selectedPackageExtraName = "selectedPackage";
    private final String restaurantInfoResponseExtraName = "restaurantInfoResponse";
    private final String cartItemExtraName = "cartItem";
    private final String finalPaymentPricesExtraName = "finalPaymentPrices";
    private final String totalAllPriceExtraName = "totalAllPrice";
    private final String totalRawPriceExtraName = "totalRaw";
    private final String totalDiscountExtraName = "totalDiscount";
    private final String packingCostLiveDataExtraName = "packingCost";
    private final String taxAndServiceExtraName = "taxAndService";
    private final String shippingCostExtraName = "shippingCost";
    private final String orderTypeExtraName = "orderType";
    private final String restaurantIdExtraName = "restaurantID";
    private final String restaurantPackageIdExtraName = "restaurantPackageId";
    private final String shippingAddressIdExtraName = "shippingAddressId";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.cart_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(CartViewModel.class);
        viewModel.setFragmentInterface(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        bottom_sheet = binding.bottomSheet;
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setGestureInsetBottomIgnored(true);
        transition = new AutoTransition();
        materialDeliveryBtnGroup = binding.materialDeliveryBtnGroup;
        coordinatorLayoutMainContent = binding.mainContent;
        arrowBtn = binding.imgBtnExpandArrow;
        expandableLayout = binding.linearLayoutCartDetailsView;
        acceptOrder = binding.linearLayoutAcceptOrder;
        toolbar = binding.toolbar;
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();

        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        viewModel.setMaterialDeliveryBtnGroup(materialDeliveryBtnGroup);
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


        /*binding.bottomSheetInclude.switcherDefaultAddress.setOnCheckedChangeListener(aBoolean -> {

            viewModel.defaultAddress = aBoolean;
            return null;
        });*/

        Bundle extras = getArguments();
        assert extras != null;
        if (extras.getLong("restaurantID", 0) != 0) {

            viewModel.changeViewLiveData.postValue(true);
            long extraRestauranId = extras.getLong("restaurantID", 0);
            RestaurantPackageItem packageItem = (RestaurantPackageItem) extras.getSerializable(selectedPackageExtraName);
            RestaurantInfoResponse restaurantInfo = (RestaurantInfoResponse) extras.getSerializable(restaurantInfoResponseExtraName);
            toolbar.setTitle(restaurantInfo.getName());
            if (packageItem != null)
                viewModel.setPackageItem(packageItem);
            if (restaurantInfo != null) {
                viewModel.setRestaurantInfo(restaurantInfo);
                toolbar.setTitle("سبد خرید " + restaurantInfo.getName());
            }


            recyclerViewUserAddress = binding.recyclerViewUserAddressHorizontalItems;
            recyclerViewUserAddress.setLayoutManager(new LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewUserAddress.setHasFixedSize(true);
            addressAdapter = new AddressAdapter(this, application);
            recyclerViewUserAddress.setAdapter(addressAdapter);
            recyclerViewCart = binding.recyclerViewRestaurantCartItems;
            recyclerViewCart.setLayoutManager(new LinearLayoutManager(application));
            recyclerViewCart.setHasFixedSize(true);
            cartAdapter = new CartAdapter(this, cartDataSource);
            recyclerViewCart.setAdapter(cartAdapter);
            viewModel.getUserAddress(sharedPreferences.getUserAuthTokenKey());
            viewModel.getAllItemInCart(extraRestauranId);

            viewModel.userAddressPagedListLiveData.observe(getActivity(), userAddressLists -> addressAdapter.submitList(userAddressLists)); //set PagedList user address

        } else {
            viewModel.changeViewLiveData.postValue(false);
            recyclerViewRestaurantsCart = binding.recyclerViewRestaurantsCartItems;
            recyclerViewRestaurantsCart.setLayoutManager(new LinearLayoutManager(application));
            recyclerViewRestaurantsCart.setHasFixedSize(true);
            restaurantsCartAdapter = new RestaurantsCartAdapter(this, cartDataSource);
            recyclerViewRestaurantsCart.setAdapter(restaurantsCartAdapter);

            viewModel.getAllRestaurantsCart();

        }

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onRecyclerViewItemClick(View v, CartItem cartItem) {

    }

    @Override
    public void onRecyclerViewRestaurantCartItemClick(View v, RestaurantItem restaurantItem) {
        Bundle bundle = new Bundle();
        bundle.putLong("restaurantID", restaurantItem.getRestaurantId());
        navController.navigate(R.id.action_cartFragment_to_restaurantDetailsFragment, bundle);
    }

    @Override
    public void onRecyclerViewAddressClick(View v, UserAddressList userAddress) {
        if (v.getId() == R.id.cv_address_name) { //remove  selecte daddress
            viewModel.removeUserAddressInfo();
            return;
        }
        viewModel.checkUserAddressForService(userAddress.getId(), userAddress.getLatitude(), userAddress.getLongitude());
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccessCartItems(List<CartItem> cartItems) {
        cartAdapter.setCartItems(cartItems);
    }

    @Override
    public void onSuccessRestaurantsCarts(List<RestaurantItem> restaurantItems) {
        if (restaurantItems.isEmpty()) {
            binding.imgEmptyCart.setVisibility(View.VISIBLE);
            toolbar.setTitle("سبد خرید شما خالی است!");
        }
        restaurantsCartAdapter.setRestaurantItems(restaurantItems);
        binding.cvWaitingResponse.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetAddress() {
        binding.cvWaitingResponse.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetStates(List<AllStatesList> allStatesLists) {
        StateAdapter stateAdapter = new StateAdapter(this, application);
        cityAdapter = new CityAdapter(this, application);
        stateCityDialog = new CustomStateCityDialog(getActivity(), stateAdapter, allStatesLists, cityAdapter);
        stateCityDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onSuccessGetCities(List<AllCitiesList> allCitiesLists) {
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
        else
            onFailure("خطا در دریافت اطلاعات استان ها");
    }

    @Override
    public void showMapDialogFragment() {
        viewModel.getStates(sharedPreferences.getUserAuthTokenKey()); //get States once

        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragment = getChildFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.addToBackStack(null);
        mapFragment = new MapDialogCartFragment(viewModel);
        mapFragment.show(fragmentTransaction, "dialog");
    }

    @Override
    public void acceptOrder(ArrayList<FinalPaymentPrice> finalPaymentPrices, List<CartItem> cartItems, int totalAllPrice, int totalRawPrice, int totalDiscount, int packingCost, int taxAndService, int shippingCost, OrderType orderType, long restaurantId, long restaurantPackageId, long shippingAddressId) {
        if (cartItems != null) {
            bundlePaymentFragment = new Bundle();
            bundlePaymentFragment.putSerializable(cartItemExtraName, (Serializable) cartItems);
            bundlePaymentFragment.putParcelableArrayList(finalPaymentPricesExtraName, finalPaymentPrices);
            bundlePaymentFragment.putInt(totalAllPriceExtraName, totalAllPrice);
            bundlePaymentFragment.putInt(totalRawPriceExtraName, totalRawPrice);
            bundlePaymentFragment.putInt(totalDiscountExtraName, totalDiscount);
            bundlePaymentFragment.putInt(packingCostLiveDataExtraName, packingCost);
            bundlePaymentFragment.putInt(taxAndServiceExtraName, taxAndService);
            bundlePaymentFragment.putInt(shippingCostExtraName, shippingCost);
            bundlePaymentFragment.putSerializable(orderTypeExtraName, orderType);
            bundlePaymentFragment.putLong(restaurantIdExtraName, restaurantId);
            bundlePaymentFragment.putLong(restaurantPackageIdExtraName, restaurantPackageId);
            bundlePaymentFragment.putLong(shippingAddressIdExtraName, shippingAddressId);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_cartFragment_to_paymentFragment, bundlePaymentFragment);

        }
    }

    @Override
    public void onSuccessGetReverseAddress(ReverseFindAddressResponse reverseFindAddressResponses) {

    }

    @Override
    public void onFailure(String Error) {
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + Error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void hideAddressBottomSheet(String msg) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        onFailure(msg);
    }

    @Override
    public void resetAddressRecyclerView() {
        addressAdapter.resetSelectedPosition();
        addressAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStateItemClick(AllStatesList state) {
        viewModel.stateId = state.getId();
        viewModel.state.setValue(state.getName());
        viewModel.city.setValue(null);
        viewModel.getCities(sharedPreferences.getUserAuthTokenKey(), state.getId());
    }

    @Override
    public void onCityItemClick(AllCitiesList city) {
        viewModel.cityId = city.getId();
        viewModel.city.setValue(city.getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewModel != null)
            viewModel.onStop();
    }
}
