package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantFragmentBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.CityAdapter;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.CustomStateCityDialog;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.StateAdapter;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFragmentInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogInterface;

public class RestaurantFragment extends Fragment implements RestaurantFragmentInterface, RecyclerViewRestaurantClickListener, StateCityDialogInterface {
    private static final String TAG = RestaurantFragment.class.getSimpleName();
    @Inject
    RestaurantViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;
    private LottieAnimationView lottie;

    private CoordinatorLayout mainLayout;
    private AutoTransition transition;
    private ChipGroup sortChipGroup;
    private ExpandableListView expandableListViewCategory;
    private List<String> listGroupCategory;
    private HashMap<String, List<String>> listItemCategory;
    private ExpandableCategoryListAadpter categoryListAadpter;
    private HorizontalScrollView scrollViewFilterChipGroup;
    private RestaurantViewModel viewModel;
    private RestaurantFragmentBinding binding;
    private RestaurantAdapter restaurantAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private CityAdapter cityAdapter;
    private CustomStateCityDialog stateCityDialog;
    private ImageButton btnSearch;
    private EditText searchEditText;
    private Button cityNameBtn;
    private NavController navController;
    private ArrayList<String> categoryList;
    private NestedScrollView nestedScrollView;
    private LiveData<PagedList<RestaurantList>> restaurantPagedListLiveData;
    private BottomSheetBehavior sheetBehavior;
    private int sortBy = 0; //default 0 - 1 == bestRestaurantScore _ 2= newest


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(RestaurantViewModel.class);
        viewModel.fragmentInterface = this;
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        toolbar = binding.toolbar;
        mainLayout = binding.mainContent;
        transition = new AutoTransition();
        sortChipGroup = binding.chipGroupSort;
        lottie = binding.animationView;
        scrollViewFilterChipGroup = binding.horizontalScrollChipsGroupFilter;
        expandableListViewCategory = binding.bottomSheetRestaurantInclude.expandableListviewFoodCategory;
        listGroupCategory = new ArrayList<>();
        listItemCategory = new HashMap<>();
        categoryListAadpter = new ExpandableCategoryListAadpter(getContext(), listGroupCategory, listItemCategory);
        expandableListViewCategory.setAdapter(categoryListAadpter);
        initCategoryData();
        btnSearch = binding.btnSearch;
        searchEditText = binding.editTextSearch;
        cityNameBtn = binding.btnCityName;

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        nestedScrollView = binding.bottomSheetRestaurantNestedScrollview;
        sheetBehavior = BottomSheetBehavior.from(nestedScrollView);
        sheetBehavior.setGestureInsetBottomIgnored(true);

        recyclerView = binding.recyclerViewAllRestaurant;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        restaurantAdapter = new RestaurantAdapter(this);
        recyclerView.setAdapter(restaurantAdapter);


        return binding.getRoot();

    }

    private void initCategoryData() {
        listGroupCategory.add(getString(R.string.food_category));

        String[] array;
        array = getResources().getStringArray(R.array.food_category);
        List<String> list = new ArrayList<>(Arrays.asList(array));
        listItemCategory.put(listGroupCategory.get(0), list);

        categoryListAadpter.notifyDataSetChanged();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                String query = searchEditText.getText().toString();
                if (query.length() > 0) {
                    hideKeyboard();
                    viewModel.setRestaurantName(query);
                    viewModel.searchBtnOnClick();
                } else {
                    hideKeyboard();
                }
            }
            return true;
        });

        btnSearch.setOnClickListener(v -> {
            if (searchEditText.getVisibility() == View.INVISIBLE) {
                TransitionManager.beginDelayedTransition(mainLayout, transition);
                btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_black_24dp));
                searchEditText.setVisibility(View.VISIBLE);
                searchEditText.requestFocus();
                showKeyboard();
                cityNameBtn.setVisibility(View.INVISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(mainLayout, transition);
                btnSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_restaurant));
                searchEditText.setVisibility(View.INVISIBLE);
                cityNameBtn.setVisibility(View.VISIBLE);
                hideKeyboard();
                if (searchEditText.getText().toString().length() > 0) {
                    viewModel.setRestaurantName(null);
                    viewModel.searchBtnOnClick();
                }
            }
        });

        assert getArguments() != null;
        boolean searchByLocation = getArguments().getBoolean("isSearchByLocation");
        boolean searchByCategory = getArguments().getBoolean("isSearchByCategory");
        boolean searchByRestaurantName = getArguments().getBoolean("isSearchByName");
        sortBy = getArguments().getInt("sortByType", 0);
        if (sortBy == 0) {
            sortChipGroup.check(R.id.chip_most_relevant);
            viewModel.sortByNameLiveData.postValue(getString(R.string.most_relevant));
        } else if (sortBy == 1) {
            sortChipGroup.check(R.id.chip_more_score);
            viewModel.sortByNameLiveData.postValue(getString(R.string.fave_resturants));
        } else {
            sortChipGroup.check(R.id.chip_newest);
            viewModel.sortByNameLiveData.postValue(getString(R.string.new_restaurants));
        }

        String cityName = sharedPreferences.getCity();
        viewModel.cityLiveData.postValue(cityName);
        viewModel.setCity(cityName);
        viewModel.setSortBy(sortBy);


        if (!searchByCategory && !searchByLocation && !searchByRestaurantName) //show all restaurants in the city
        {
            viewModel.getAllSearchedRestaurant(null, cityName, null, null,
                    null, null, null, null, null, sortBy);
        } else {

            if (searchByLocation)//search restaurants by location
            {
                viewModel.cityLiveData.postValue("جستجو براساس موقعیت");
                double lat = sharedPreferences.getLatitude();
                double lon = sharedPreferences.getLongitud();
                viewModel.getAllSearchedRestaurant(null, null, null, null,
                        null, null, null, lat, lon, 0);
                viewModel.setLatLon(lat, lon);
            } else if (searchByCategory) //search restaurants by category
            {
                categoryList = new ArrayList<>();
                categoryList.add(getArguments().getString("categoryName"));
                viewModel.setCategoryList(categoryList);
                viewModel.getAllSearchedRestaurant(categoryList, cityName, null, null,
                        null, null, null, null, null, 0);

            } else //search restaurants by restaurant name
            {
                String restaurantName = getArguments().getString("restaurantName");
                viewModel.getAllSearchedRestaurant(null, cityName, restaurantName, null,
                        null, null, null, null, null, 0);
                viewModel.setRestaurantName(restaurantName);
            }
        }
        restaurantPagedListLiveData = viewModel.restaurantPagedListLiveData;
        restaurantPagedListLiveData.observe(getViewLifecycleOwner(), restaurantLists -> restaurantAdapter.submitList(restaurantLists));
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputManager != null)
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) application.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onStarted() {


        Observable.fromCallable(() -> {
            binding.cvWaitingResponse.setVisibility(View.VISIBLE);
            lottie.setAnimation(R.raw.waiting_animate_burger);
            lottie.playAnimation();
            return false;
        });

    }

    @Override
    public void onSuccess() {
        recyclerView.scheduleLayoutAnimation();
        binding.cvWaitingResponse.setVisibility(View.GONE);
    }


    @Override
    public void onFailure(String error) {
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + error, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.materialGray900))
                .setBackgroundTint(getResources().getColor(R.color.colorLowGold));
        snackbar.show();
        binding.cvWaitingResponse.setVisibility(View.GONE);
    }

    @Override
    public void expandSortView() {
        if (sortChipGroup.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(mainLayout, transition);
            sortChipGroup.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(mainLayout, transition);
            sortChipGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void expandFilterView() {
        if (scrollViewFilterChipGroup.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(mainLayout, transition);
            scrollViewFilterChipGroup.setVisibility(View.VISIBLE);

        } else {
            TransitionManager.beginDelayedTransition(mainLayout, transition);
            scrollViewFilterChipGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void openBottomSheet() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void closeBottomSheet() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void showStateCityCustomDialog(List<AllStatesList> statesLists) {
        StateAdapter stateAdapter = new StateAdapter(this, application);
        cityAdapter = new CityAdapter(this, application);
        stateCityDialog = new CustomStateCityDialog(getActivity(), stateAdapter, statesLists, cityAdapter);
        stateCityDialog.setCanceledOnTouchOutside(false);
        if (stateCityDialog != null)
            stateCityDialog.show();
        else
            onFailure("خطا در دریافت اطلاعات استان ها");
    }

    @Override
    public void onSuccessGetCities(List<AllCitiesList> allCitiesLists) {
        cityAdapter.setCitiesLists(allCitiesLists);
    }

    @Override
    public void updateRestaurantsRecyclerView(Object categoryList, Object city, Object restaurantName, Object deliveryFilter, Object discountFilter, Object servingFilter, Object getInPlaceFilter, Object latitude, Object longitude, Object sortBy) {
        viewModel.getAllSearchedRestaurant(categoryList, city,
                restaurantName, deliveryFilter, discountFilter, servingFilter, getInPlaceFilter,
                latitude, longitude, sortBy);
        if (restaurantPagedListLiveData.hasObservers()) {
            restaurantPagedListLiveData.removeObservers(getActivity());
            restaurantPagedListLiveData = viewModel.restaurantPagedListLiveData;
            restaurantPagedListLiveData.observe(getViewLifecycleOwner(), restaurantLists -> restaurantAdapter.submitList(restaurantLists));
        }
        if (sheetBehavior != null) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void tryAgain() {
        lottie.setAnimation(R.raw.no_internet_connection_animate);
        lottie.playAnimation();
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRecyclerViewItemClick(View v, RestaurantList restaurantList) {
        switch (v.getId()) {
            case R.id.toggle_bookmark:

                break;
            case R.id.cons_layout:
                Bundle bundle = new Bundle();
                bundle.putLong("restaurantID", restaurantList.getId());
                navController.navigate(R.id.action_restaurantFragment_to_restaurantDetailsFragment, bundle);
                break;
        }
    }

    @Override
    public void onStateItemClick(AllStatesList state) {
        sharedPreferences.setState(state.getName());
        viewModel.stateLiveData.postValue(state.getName());
        viewModel.getCities(state.getId());
    }

    @Override
    public void onCityItemClick(AllCitiesList city) {
        sharedPreferences.setCity(city.getName());
        viewModel.cityLiveData.setValue(city.getName());
        viewModel.setCity(city.getName());
        stateCityDialog.dismiss();
        //because the city is changed so we must put null in location and restaurant
        viewModel.setLatLon(null, null);
        viewModel.setRestaurantName(null);
        viewModel.searchBtnOnClick();
    }
}
