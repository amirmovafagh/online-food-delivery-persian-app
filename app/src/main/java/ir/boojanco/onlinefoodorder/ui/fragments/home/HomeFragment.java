package ir.boojanco.onlinefoodorder.ui.fragments.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.HomeFragmentBinding;
import ir.boojanco.onlinefoodorder.models.food.FoodCategories;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.CityAdapter;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.CustomStateCityDialog;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.StateAdapter;
import ir.boojanco.onlinefoodorder.viewmodels.HomeViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.HomeViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.HomeFragmentInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogInterface;

public class HomeFragment extends Fragment implements HomeFragmentInterface, StateCityDialogInterface, FoodCategorySearchInterface {
    private static final String TAG = HomeFragment.class.getSimpleName();

    @Inject
    Application application;
    @Inject
    HomeViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    private HomeViewModel viewModel;
    private HomeFragmentBinding binding;
    private FoodCategorySearchAdapter adapter;
    private RecyclerView recyclerViewFoodTypeSearchFilter;
    private EditText searchEditText;
    private Button searchButton;
    private LottieAnimationView lottie;
    private boolean btnSearchColor = false;
    private String searchQuery = "";
    private CityAdapter cityAdapter;
    private CustomStateCityDialog stateCityDialog;
    private ValueAnimator colorAnimationButtonBackground, colorAnimationButtonText;

    private FusedLocationProviderClient locationProviderClient;
    private int PERMISSION_ID = 17;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        binding.setHomeViewModel(viewModel);
        binding.setLifecycleOwner(this);
        lottie = binding.animationView;

        viewModel.setFragmentInterface(this);
        viewModel.setUserAuthToken(sharedPreferences.getUserAuthTokenKey());
        recyclerViewFoodTypeSearchFilter = binding.recyclerviewFoodTypeSearchFilterHome;
        searchButton = binding.buttonSearchRestaurant;

        recyclerViewFoodTypeSearchFilter.setLayoutManager(new LinearLayoutManager(getActivity().getApplication(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFoodTypeSearchFilter.canScrollHorizontally(0);
        recyclerViewFoodTypeSearchFilter.setHasFixedSize(true);
        adapter = new FoodCategorySearchAdapter(this);
        recyclerViewFoodTypeSearchFilter.setAdapter(adapter);
        recyclerViewFoodTypeSearchFilter.setItemViewCacheSize(20);
        searchEditText = binding.search;
        colorAnimationButtonText = ValueAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(R.color.white), getResources().getColor(R.color.white));
        colorAnimationButtonBackground = ValueAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(R.color.colorSecondPrimaryDark), getResources().getColor(R.color.colorPrimaryDark));
        colorAnimationButtonBackground.setDuration(600);
        colorAnimationButtonText.setDuration(600);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getCategories();

        colorAnimationButtonBackground.addUpdateListener(animator -> searchButton.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnimationButtonText.addUpdateListener(animator -> {
            searchButton.setTextColor((int) animator.getAnimatedValue());

        });

        if (sharedPreferences.getCity() != null && sharedPreferences.getState() != null) {
            viewModel.cityLiveData.setValue(sharedPreferences.getCity());
            viewModel.stateLiveData.setValue(sharedPreferences.getState());
        }

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sharedPreferences.getCity() != null) {
                    searchQuery = s.toString();
                    //dont search restaurant name and set button on location mode
                    if (btnSearchColor && s.toString().length() == 0) {
                        colorAnimationButtonText.reverse();
                        colorAnimationButtonBackground.reverse();
                        btnSearchColor = false;
                        searchButton.animate().setDuration(300).setListener(new AnimatorListenerAdapter() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                searchButton.setText("رستوران\u200cهای نزدیک من");
                                searchButton.animate().setListener(null).setDuration(300).alpha(1);
                            }
                        }).alpha(1);
                    }
                    //set button on restaurant name mode
                    if (!btnSearchColor && s.toString().length() > 0) {
                        btnSearchColor = true;
                        colorAnimationButtonBackground.start();
                        colorAnimationButtonText.start();
                        searchButton.animate().setDuration(300).setListener(new AnimatorListenerAdapter() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                searchButton.setText("جستجو در رستوران" + "\u200cهای " + sharedPreferences.getCity());
                                searchButton.animate().setListener(null).setDuration(300).alpha(1);
                            }
                        }).alpha(1);
                    }
                } else
                    onFailure("لطفا شهر را انتخاب کنید");
            }

            @Override
            public void afterTextChanged(Editable query) {
            }
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
        lottie.setAnimation(R.raw.waiting_animate_burger);
        lottie.playAnimation();
    }

    @Override
    public void onSuccess(List<FoodCategories> categories) {
        adapter.setFoodCategoryItems(categories);
        binding.cvWaitingResponse.setVisibility(View.GONE);
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
    public void onFailure(String error) {
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + error, Snackbar.LENGTH_SHORT);
        snackbar.show();
        //binding.cvWaitingResponse.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetCities(List<AllCitiesList> allCitiesLists) {
        cityAdapter.setCitiesLists(allCitiesLists);
    }

    @Override
    public void searchRestaurantOnClick() {
        if (btnSearchColor) {//search restaurant name
            Bundle bundle = new Bundle();
            bundle.putBoolean("isSearchByName", true);
            bundle.putString("restaurantName", searchQuery);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_homeFragment_to_restaurantFragment, bundle);
        } else {//searchBy location
            binding.cvWaitingResponse.setVisibility(View.VISIBLE);
            lottie.setAnimation(R.raw.waiting_animate_burger);
            lottie.playAnimation();
            getLastLocation();
        }

    }

    @Override
    public void setCityAndState(String state, String city) {
        if (city != null && state != null) {
            sharedPreferences.setCity(city);
            viewModel.cityLiveData.setValue(city);
            sharedPreferences.setState(state);
            viewModel.stateLiveData.setValue(state);
            Bundle bundle = new Bundle();
            bundle.putBoolean("isSearchByLocation", true);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_homeFragment_to_restaurantFragment, bundle);
        } else onFailure("خطا در دریافت اطلاعت موقعیت شما");
        binding.cvWaitingResponse.setVisibility(View.GONE);

    }

    @Override
    public void tryAgain() {
        lottie.setAnimation(R.raw.no_internet_connection_animate);
        lottie.playAnimation();
    }

    @Override
    public void searchRestaurantsByScore() {
        Bundle bundle = new Bundle();
        bundle.putInt("sortByType", 1);
        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_restaurantFragment, bundle);
    }

    @Override
    public void searchRestaurantsByNewestDate() {
        Bundle bundle = new Bundle();
        bundle.putInt("sortByType", 2);
        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_restaurantFragment, bundle);
    }


    @Override
    public void onStateItemClick(AllStatesList state) {
        sharedPreferences.setState(state.getName());
        viewModel.stateLiveData.postValue(state.getName());
        viewModel.cityLiveData.setValue("شهر را انتخاب کنید");
        viewModel.getCities(sharedPreferences.getUserAuthTokenKey(), state.getId());
    }

    @Override
    public void onCityItemClick(AllCitiesList city) {
        sharedPreferences.setCity(city.getName());
        viewModel.cityLiveData.setValue(city.getName());
        stateCityDialog.dismiss();
    }

    @Override
    public void onCategoryClick(String name) {
        if (sharedPreferences.getCity() != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isSearchByCategory", true);
            bundle.putString("categoryName", name);
            bundle.putString("cityName", sharedPreferences.getCity());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_homeFragment_to_restaurantFragment, bundle);
        } else
            onFailure("لطفا شهر را انتخاب کنید");
    }


    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationProviderClient.getLastLocation().addOnCompleteListener(
                        task -> {
                            try {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    sharedPreferences.setLatitude(location.getLatitude());
                                    sharedPreferences.setLongitud(location.getLongitude());
                                    getCityName(location);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage() + "");
                            }
                        }
                );

            } else {
                showSetLocationEnabledAlertDialog();

            }
        } else {
            requestPermissions();

        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showSetLocationEnabledAlertDialog() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustomRTL);
        builder.setCancelable(false);
        builder.setTitle("تنظیمات موقعیت یاب");
        builder.setMessage("در صورت تمایل به استفاده از سیستم موقعیت یاب به منظور یافتن نزدیکترین رستوران\u200cها این بخش را فعال کنید.");
        // add the buttons
        builder.setPositiveButton("تایید", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        });
        builder.setNegativeButton("انصراف", (dialog, which) -> dialog.cancel());
        // create and show the alert dialog
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
                getLastLocation();
            }
        }
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            if (mLastLocation != null) {
                sharedPreferences.setLatitude(mLastLocation.getLatitude());
                sharedPreferences.setLongitud(mLastLocation.getLongitude());
                getCityName(mLastLocation);
            }

        }
    };

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(2);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationProviderClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private void getCityName(Location location) {
        viewModel.getReverseAddressParsimap(location.getLatitude(), location.getLongitude());
    }
}
