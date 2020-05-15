package ir.boojanco.onlinefoodorder.ui.fragments.home;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
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
    private SearchView searchView;
    private Button searchButton;
    private boolean btnSearchColor = false;
    private CityAdapter cityAdapter;
    private CustomStateCityDialog stateCityDialog;
    private ValueAnimator colorAnimationButtonBackground, colorAnimationButtonText;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        binding.setHomeViewModel(viewModel);
        binding.setLifecycleOwner(this);

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
        searchView = binding.search;
        colorAnimationButtonText = ValueAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.white));
        colorAnimationButtonBackground = ValueAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(R.color.colorGold), getResources().getColor(R.color.colorSecondPrimary));
        colorAnimationButtonBackground.setDuration(1000);
        colorAnimationButtonText.setDuration(1000);

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

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (sharedPreferences.getCity() != null && sharedPreferences.getState() != null) {
                viewModel.cityLiveData.setValue(sharedPreferences.getCity());
                viewModel.stateLiveData.setValue(sharedPreferences.getState());
            }
        }, 5000);

        searchView.setOnClickListener(v -> {
            searchView.onActionViewExpanded();


        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (sharedPreferences.getCity() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("restaurantName", query);
                    bundle.putString("cityName", sharedPreferences.getCity());
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_homeFragment_to_restaurantFragment, bundle);
                    /*  NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(action);*/
                } else
                    onFailure("لطفا شهر را انتخاب کنید");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (btnSearchColor)
                    return false;
                btnSearchColor = true;
                colorAnimationButtonBackground.start();
                colorAnimationButtonText.start();
                searchButton.animate().setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        searchButton.setText("یافتن رستوران" + " های " + sharedPreferences.getCity());
                        searchButton.animate().setListener(null).setDuration(500).alpha(1);
                    }
                }).alpha(1);
                return false;
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
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
        binding.cvWaitingResponse.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGetCities(List<AllCitiesList> allCitiesLists) {
        cityAdapter.setCitiesLists(allCitiesLists);
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
            bundle.putString("categoryName", name);
            bundle.putString("cityName", sharedPreferences.getCity());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_homeFragment_to_restaurantFragment, bundle);
                    /*  NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(action);*/
        } else
            onFailure("لطفا شهر را انتخاب کنید");
    }
}
