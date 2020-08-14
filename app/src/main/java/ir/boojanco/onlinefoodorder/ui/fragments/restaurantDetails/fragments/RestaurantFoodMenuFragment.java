package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.databinding.RestaurantFoodMenuFragmentBinding;
import ir.boojanco.onlinefoodorder.models.food.GetAllFoodResponse;
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
    private ArrayList<String> pureFoodTypeIndex;

    private RestaurantFoodMenuViewModel viewModel;
    private RestaurantFoodMenuFragmentBinding binding;
    private RecyclerView recyclerViewFoodMenu;
    private RestaurantFoodMenuAdapter adapterMenu;
    private RestaurantFoodTypeAdapter adapterFoodType;
    private RestaurantPackageAdapter adapterPackage;
    private LinearLayoutManager linearLayoutManagerFoodMenu, linearLayoutManagerFoodType;
    private AutoTransition transition;
    private Bundle bundleCartFragment;
    private NavController navigation;

    private long extraRestauranId = 0;
    private boolean isLogin = false;

    private ConstraintLayout expandableView;
    private CardView packageBtn;
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
        viewModel = new ViewModelProvider(this, factory).get(RestaurantFoodMenuViewModel.class);
        RestaurantInfoSharedViewModel sharedViewModel = new ViewModelProvider(getParentFragment()).get(RestaurantInfoSharedViewModel.class);
        viewModel.foodInterface = this;
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        navigation = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        transition = new AutoTransition();
        expandableView = binding.expandableView;
        packageBtn = binding.cvPackageBtn;
        mainLayout = binding.linearLayoutMainFoodMenu;
        Bundle extras = getArguments();
        if (extras != null) {
            if (sharedPreferences.getUserAuthTokenKey() != null)
                isLogin = true;
            extraRestauranId = extras.getLong("restaurantID", 0);
            viewModel.setExtraRestaurantId(extraRestauranId);
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

            linearLayoutManagerFoodType = new LinearLayoutManager(getActivity().getApplication(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewFoodType.setLayoutManager(linearLayoutManagerFoodType);
            recyclerViewFoodType.canScrollHorizontally(0);
            recyclerViewFoodType.setHasFixedSize(true);
            adapterFoodType = new RestaurantFoodTypeAdapter(this, application);
            snapHelper.attachToRecyclerView(recyclerViewFoodType);

            recyclerViewFoodType.setAdapter(adapterFoodType);
            recyclerViewFoodType.setItemViewCacheSize(20);


            linearLayoutManagerFoodMenu = new LinearLayoutManager(application);
            recyclerViewFoodMenu = binding.recyclerViewRestauranFood;
            recyclerViewFoodMenu.setLayoutManager(linearLayoutManagerFoodMenu);
            recyclerViewFoodMenu.setHasFixedSize(true);
            recyclerViewFoodMenu.setItemViewCacheSize(20);
            adapterMenu = new RestaurantFoodMenuAdapter(this, cartDataSource, extraRestauranId);
            recyclerViewFoodMenu.setAdapter(adapterMenu);
            viewModel.getAllFood(sharedPreferences.getUserAuthTokenKey(), extraRestauranId);
            viewModel.getRestaurantPackages(sharedPreferences.getUserAuthTokenKey(), extraRestauranId);

            sharedViewModel.infoResponseMutableLiveData.observe(getViewLifecycleOwner(), restaurantInfoResponse -> {
                if (restaurantInfoResponse != null) {
                    viewModel.setRestaurantInfoResponse(restaurantInfoResponse);
                    bundleCartFragment.putSerializable(restaurantInfoResponseExtraName, restaurantInfoResponse);
                }
            });
            sharedViewModel.userRestaurantClubPointLivedata.observe(getViewLifecycleOwner(), userClubPoint -> {
                if (userClubPoint != null) {
                    viewModel.setUserRestaurantClubPoint(userClubPoint);
                    adapterPackage.setUserClubPoint(userClubPoint);
                    String point = "امتیاز شما" + System.lineSeparator() + userClubPoint;
                    binding.textViewClubScore.setText(point);
                }

            });
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        packageBtn.setOnClickListener(v -> {
            if (!isLogin) {
                loginMessage("لطفا وارد شوید");
                return;
            }
            if (expandableView.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(mainLayout, transition);
                expandableView.setVisibility(View.VISIBLE);
                binding.textViewClubScore.setVisibility(View.VISIBLE);

            } else {
                TransitionManager.beginDelayedTransition(mainLayout, transition);
                expandableView.setVisibility(View.GONE);
                binding.textViewClubScore.setVisibility(View.GONE);
            }
        });

        recyclerViewFoodMenu.getViewTreeObserver().addOnScrollChangedListener(() -> { // managing on Scroll foodMenu And select foodType recyclerView
            for (int childCount = recyclerViewFoodMenu.getChildCount(), i = 0; i < childCount; ++i) {

                if (recyclerViewFoodMenu.getChildViewHolder(recyclerViewFoodMenu.getChildAt(i)).getItemViewType() == 1) { // if is as header type condition is true
                    Log.e(TAG, "" + foodTypeIndex.get(recyclerViewFoodMenu.getChildViewHolder(recyclerViewFoodMenu.getChildAt(i)).getLayoutPosition()));
                    for (int j = 0; j < pureFoodTypeIndex.size(); j++) {
                        if (pureFoodTypeIndex.get(j).equals(foodTypeIndex.get(recyclerViewFoodMenu.getChildViewHolder(recyclerViewFoodMenu.getChildAt(i)).getLayoutPosition()))) {
                            linearLayoutManagerFoodType.scrollToPositionWithOffset(j, linearLayoutManagerFoodType.getPaddingRight());
                            adapterFoodType.changeOnScrollFoodMenu(j);
                        }
                    }
                }

            }
        });
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess(ArrayList<ListItemType> items, MutableLiveData<GetAllFoodResponse> foodTypeMutableLiveData, ArrayList<String> foodTypeIndex, List<Long> faveFoods) {
        foodTypeMutableLiveData.observe(this, getAllFoodResponse -> {
            //recyclerViewFoodType.scheduleLayoutAnimation();
            adapterFoodType.setFoodTypeLists(getAllFoodResponse.getFoodTypeList().getTypeList());
        });
        this.foodTypeIndex = foodTypeIndex;
        recyclerViewFoodMenu.scheduleLayoutAnimation();
        adapterMenu.setFoodLists(items, faveFoods);

        pureFoodTypeIndex = new ArrayList<>(); //use this variable for checking on scroll food menu and change food category
        for (String str : foodTypeIndex) {
            if (!str.equals(" ")) {
                pureFoodTypeIndex.add(str);
            }
        }
    }

    @Override
    public void onSuccessRestaurantPackages(MutableLiveData<AllPackagesResponse> allPackagesMutableLiveData) {
        allPackagesMutableLiveData.observe(this, allPackagesResponse -> {
            adapterPackage.setPackageItems(allPackagesResponse.getPackageItem());

        });
    }

    @Override
    public void onFailure(String error) {
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + error, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.materialGray900))
                .setBackgroundTint(getResources().getColor(R.color.colorLowGold));
        snackbar.show();
    }

    private void loginMessage(String msg) {
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + msg, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                .setTextColor(getResources().getColor(R.color.materialGray900))
                .setBackgroundTint(getResources().getColor(R.color.colorLowGold))
                .setAction(R.string.login, v -> {
                    if (navigation.getCurrentDestination().getId() == R.id.restaurantDetailsFragment) {
                        if (getActivity() != null) getActivity().finish();
                        navigation.navigate(R.id.action_restaurantFoodMenuFragment_to_enterActivity);
                    }
                });
        /*TextView tv = (TextView) (snackbar.getView()).findViewById(R.id.snackbar_text);
        Typeface font = Typeface.createFromAsset(getResources().getAssets(), "fonts/iran_sans_mobile_fa_num_bold.ttf");
        tv.setTypeface(font);*/
        snackbar.show();
    }

    @Override
    public void goToCartFragment() {
        if (!isLogin) {
            loginMessage("لطفا وارد شوید");
            return;
        }
        bundleCartFragment.putLong("restaurantID", extraRestauranId);
        navigation.navigate(R.id.action_restaurantFoodMenuFragment_to_cartFragment, bundleCartFragment);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewModel != null) {
            viewModel.onStop();

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapterFoodType != null)
            viewModel.onStop();
    }

    @Override
    public void onRecyclerViewItemClick(View v, FoodItem items) {

        switch (v.getId()) {
            case R.id.img_btn_increase:
                viewModel.addToCartItemDB(items, extraRestauranId);
                break;
            case R.id.ivLogo:
                break;
        }
    }

    @Override
    public void onRecyclerViewFaveToggleClick(FoodItem items, boolean isChecked) {
        if (!isLogin) {
            loginMessage("لطفا وارد شوید");
            return;
        }
        viewModel.onFoodFavoriteCheckedChanged(items.getId(), isChecked);
    }

    @Override
    public void onCartItemCountUpdate() {
        viewModel.getCartCountItem(extraRestauranId);
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
        viewModel.getCartCountItem(extraRestauranId);
        adapterMenu.notifyDataSetChanged();
    }

    @Override
    public void onPackageItemClick(View v, RestaurantPackageItem packageItem) {
        if (!isLogin) {
            loginMessage("لطفا وارد شوید");
            return;
        }
        switch (v.getId()) {
            case R.id.cv_main_package_layout:
                bundleCartFragment.putSerializable(selectedPackageExtraName, packageItem);
                return;
            case R.id.cv_package_name:
                if (bundleCartFragment.getSerializable(selectedPackageExtraName) != null)
                    bundleCartFragment.remove(selectedPackageExtraName);
                return;
            case R.id.btn_show_discount_items:
                packageFoodsAlertDialog(packageItem.getFoodList());
                return;
        }
    }

    private void packageFoodsAlertDialog(Map<Long, String> map) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogCustomRTL);
        builder.setTitle("غذا های دارای تخفیف");

        // set the custom layout
        //final View customLayout = getLayoutInflater().inflate(R.layout.custom_rounded_alert_dialog, null);
        //builder.setView(customLayout);

        String[] foodList = map.values().toArray(new String[0]);
        // add a list
        builder.setItems(foodList, null);
        builder.setNegativeButton("بستن", (dialog, which) -> dialog.cancel());
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    @Override
    public void onPackageMessage(String msg) {
        onFailure(msg);
    }
}
