package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.databinding.RestaurantFoodMenuFragmentBinding;
import ir.boojanco.onlinefoodorder.models.food.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.RestaurantPackageItem;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.ListItemType;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.RecyclerViewRestaurantFoodMenuClickListener;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.RecyclerViewRestaurantFoodTypeClickListener;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.RestaurantFoodMenuAdapter;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.RestaurantFoodTypeAdapter;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantInfoSharedViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantFoodMenuViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFoodMenuInterface;

public class RestaurantFoodMenuFragment extends Fragment implements RestaurantFoodMenuInterface, RecyclerViewRestaurantFoodMenuClickListener, RecyclerViewRestaurantFoodTypeClickListener, RestaurantPackageInterface {
    private final static String TAG = RestaurantFoodMenuFragment.class.getSimpleName();
    @Inject
    RestaurantFoodMenuViewModelFactory factory;
    @Inject
    Application application;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    CartDataSource cartDataSource;


    private ArrayList<String> foodTypeIndex;

    private RestaurantFoodMenuViewModel restaurantFoodMenuViewModel;
    private RestaurantFoodMenuFragmentBinding binding;
    private RecyclerView recyclerViewFoodMenu;
    private RestaurantFoodMenuAdapter adapterMenu;
    private RestaurantFoodTypeAdapter adapterFoodType;
    private RestaurantPackageAdapter adapterPackage;
    private LinearLayoutManager linearLayoutManagerFoodMenu;
    private AutoTransition transition;
    private Bundle bundleCartFragment;

    private long extraRestauranId = 0;

    private ConstraintLayout expandableView;
    private ImageButton arrowBtn;
    private LinearLayout mainLayout;

    private final String selectedPackageExtraName = "selectedPackage";
    private final String restaurantInfoResponseExtraName = "restaurantInfoResponse";


    public static RestaurantFoodMenuFragment newInstance() {
        return new RestaurantFoodMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        bundleCartFragment = new Bundle();
        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_food_menu_fragment, container, false);
        restaurantFoodMenuViewModel = new ViewModelProvider(this, factory).get(RestaurantFoodMenuViewModel.class);
        RestaurantInfoSharedViewModel sharedViewModel = new ViewModelProvider(getParentFragment()).get(RestaurantInfoSharedViewModel.class);
        restaurantFoodMenuViewModel.foodInterface = this;
        binding.setFoodMenu(restaurantFoodMenuViewModel);
        binding.setLifecycleOwner(this);
        transition = new AutoTransition();
        expandableView = binding.expandableView;
        arrowBtn = binding.arrowBtn;
        mainLayout = binding.linearLayoutMainFoodMenu;
        Bundle extras = getArguments();
        if (extras != null) {
            extraRestauranId = extras.getLong("restaurantID", 0);
            restaurantFoodMenuViewModel.setExtraRestaurantId(extraRestauranId);
            RecyclerView recyclerViewFoodType = binding.recyclerViewFoodType;
            RecyclerView recyclerViewRestaurantPackage = binding.recyclerViewRestaurantPackage;
            SnapHelper snapHelper = new PagerSnapHelper(); //for stay on center


            recyclerViewRestaurantPackage.setLayoutManager(new LinearLayoutManager(getActivity().getApplication(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewRestaurantPackage.canScrollHorizontally(0);
            recyclerViewRestaurantPackage.setHasFixedSize(true);
            adapterPackage = new RestaurantPackageAdapter(this, application);
            snapHelper.attachToRecyclerView(recyclerViewRestaurantPackage);
            recyclerViewRestaurantPackage.setAdapter(adapterPackage);
            recyclerViewRestaurantPackage.setItemViewCacheSize(20);


            recyclerViewFoodType.setLayoutManager(new LinearLayoutManager(getActivity().getApplication(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewFoodType.canScrollHorizontally(0);
            recyclerViewFoodType.setHasFixedSize(true);
            adapterFoodType = new RestaurantFoodTypeAdapter(this, application);
            snapHelper.attachToRecyclerView(recyclerViewFoodType);

            recyclerViewFoodType.setAdapter(adapterFoodType);
            recyclerViewFoodType.setItemViewCacheSize(20);


            linearLayoutManagerFoodMenu = new LinearLayoutManager(getActivity().getApplication());
            recyclerViewFoodMenu = binding.recyclerViewRestauranFood;
            recyclerViewFoodMenu.setLayoutManager(linearLayoutManagerFoodMenu);
            recyclerViewFoodMenu.setHasFixedSize(true);
            recyclerViewFoodMenu.setItemViewCacheSize(20);
            adapterMenu = new RestaurantFoodMenuAdapter(this, cartDataSource, extraRestauranId);
            recyclerViewFoodMenu.setAdapter(adapterMenu);
            restaurantFoodMenuViewModel.getAllFood(sharedPreferences.getUserAuthTokenKey(), extraRestauranId);
            restaurantFoodMenuViewModel.getRestaurantPackages(sharedPreferences.getUserAuthTokenKey(), extraRestauranId);

            sharedViewModel.infoResponseMutableLiveData.observe(getViewLifecycleOwner(), new Observer<RestaurantInfoResponse>() {
                @Override
                public void onChanged(RestaurantInfoResponse restaurantInfoResponse) {
                    if (restaurantInfoResponse != null) {
                        bundleCartFragment.putSerializable(restaurantInfoResponseExtraName, restaurantInfoResponse);
                    }
                }
            });
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.fab.setOnClickListener(v -> {
            bundleCartFragment.putLong("restaurantID", extraRestauranId);

            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_restaurantFoodMenuFragment_to_cartFragment, bundleCartFragment);
        });

        arrowBtn.setOnClickListener(v -> {
            if (expandableView.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(mainLayout, transition);
                expandableView.setVisibility(View.VISIBLE);
                arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            } else {
                TransitionManager.beginDelayedTransition(mainLayout, transition);
                expandableView.setVisibility(View.GONE);
                arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
        });
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess(ArrayList<ListItemType> items, MutableLiveData<GetAllFoodResponse> foodTypeMutableLiveData, ArrayList<String> foodTypeIndex) {
        foodTypeMutableLiveData.observe(this, getAllFoodResponse -> {
            //recyclerViewFoodType.scheduleLayoutAnimation();
            adapterFoodType.setFoodTypeLists(getAllFoodResponse.secondaryList());
        });
        this.foodTypeIndex = foodTypeIndex;
        recyclerViewFoodMenu.scheduleLayoutAnimation();
        adapterMenu.setFoodLists(items);
    }

    @Override
    public void onSuccessRestaurantPackages(MutableLiveData<AllPackagesResponse> allPackagesMutableLiveData) {
        allPackagesMutableLiveData.observe(this, allPackagesResponse -> {
            adapterPackage.setPackageItems(allPackagesResponse.getPackageItem());

        });
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(application, "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (restaurantFoodMenuViewModel != null) {
            restaurantFoodMenuViewModel.onStop();

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapterFoodType != null)
            restaurantFoodMenuViewModel.onStop();
    }

    @Override
    public void onRecyclerViewItemClick(View v, FoodItem items) {
        switch (v.getId()) {
            case R.id.img_btn_increase:
                Toast.makeText(application, "" + extraRestauranId, Toast.LENGTH_SHORT).show();
                restaurantFoodMenuViewModel.addToCartItemDB(items, extraRestauranId);

                break;
            case R.id.ivLogo:

                break;
        }
    }

    @Override
    public void onRecyclerViewFaveToggleClick(FoodItem items, boolean isChecked) {
        restaurantFoodMenuViewModel.onFoodFavoriteCheckedChanged(items.getId(), isChecked);
    }

    @Override
    public void onCartItemCountUpdate() {
        restaurantFoodMenuViewModel.getCartCountItem(extraRestauranId);
    }

    @Override
    public void onRecyclerViewTypeItemClick(View v, String foodTypeName) {
        switch (v.getId()) {
            case R.id.tvNameType:

                for (int i = 0; i < foodTypeIndex.size(); i++) {
                    if (foodTypeIndex.get(i).equals(foodTypeName)) {
                        linearLayoutManagerFoodMenu.scrollToPositionWithOffset(i, linearLayoutManagerFoodMenu.getPaddingTop());
                    }
                }
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        restaurantFoodMenuViewModel.getCartCountItem(extraRestauranId);
        adapterMenu.notifyDataSetChanged();
    }

    @Override
    public void onPackageItemClick(View v, RestaurantPackageItem packageItem) {
        switch (v.getId()) {
            case R.id.cv_main_package_layout:
                bundleCartFragment.putSerializable(selectedPackageExtraName, packageItem);
                return;
            case R.id.cv_package_name:
                if (bundleCartFragment.getSerializable(selectedPackageExtraName) != null)
                    bundleCartFragment.remove(selectedPackageExtraName);
                return;
        }
    }

    @Override
    public void removeSelectedPackage() {

    }
}
