package ir.boojanco.onlinefoodorder.ui.fragments.home;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.HomeFragmentBinding;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.CityAdapter;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.CustomStateCityDialog;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.StateAdapter;
import ir.boojanco.onlinefoodorder.viewmodels.HomeViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.HomeViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.HomeFragmentInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogInterface;

public class HomeFragment extends Fragment implements HomeFragmentInterface, StateCityDialogInterface {
    private static final String TAG = HomeFragment.class.getSimpleName();

    @Inject
    Application application;
    @Inject
    HomeViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    private HomeViewModel homeViewModel;
    private HomeFragmentBinding binding;
    private SliderView sliderView;
    private ArrayList<FoodTypeFilterItem> foodTypeFilterItems;
    private FoodTypeSearchFilterAdapter adapter;
    private RecyclerView recyclerViewFoodTypeSearchFilter;
    private SearchView searchView;
    private CityAdapter cityAdapter;
    private CustomStateCityDialog stateCityDialog;

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
        homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        binding.setHomeViewModel(homeViewModel);
        binding.setLifecycleOwner(this);
        sliderView = binding.imageSlider;
        homeViewModel.setFragmentInterface(this);
        homeViewModel.setUserAuthToken(sharedPreferences.getUserAuthTokenKey());
        recyclerViewFoodTypeSearchFilter = binding.recyclerviewFoodTypeSearchFilterHome;

        recyclerViewFoodTypeSearchFilter.setLayoutManager(new LinearLayoutManager(getActivity().getApplication(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFoodTypeSearchFilter.canScrollHorizontally(0);
        recyclerViewFoodTypeSearchFilter.setHasFixedSize(true);
        adapter = new FoodTypeSearchFilterAdapter();
        recyclerViewFoodTypeSearchFilter.setAdapter(adapter);
        recyclerViewFoodTypeSearchFilter.setItemViewCacheSize(20);
        searchView = binding.search;

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sliderView.setSliderAdapter(new SliderAdapterHome(getContext()));
        foodTypeFilterItems = new ArrayList<>();
        foodTypeFilterItems.add(new FoodTypeFilterItem("برگر", getResources().getIdentifier("@drawable/burger", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("چینی", getResources().getIdentifier("@drawable/chinese", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("غذای اصلی", getResources().getIdentifier("@drawable/main_course", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("پیتزا", getResources().getIdentifier("@drawable/pizza", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("سوپ", getResources().getIdentifier("@drawable/soup", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("برگر", getResources().getIdentifier("@drawable/burger", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("چینی", getResources().getIdentifier("@drawable/chinese", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("غذای اصلی", getResources().getIdentifier("@drawable/main_course", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("پیتزا", getResources().getIdentifier("@drawable/pizza", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("سوپ", getResources().getIdentifier("@drawable/soup", "drawable", getActivity().getPackageName())));

        adapter.setFoodTypeFilterItems(foodTypeFilterItems);

        if (sharedPreferences.getCity() != null && sharedPreferences.getState() != null) {
            homeViewModel.cityLiveData.setValue(sharedPreferences.getCity());
            homeViewModel.stateLiveData.setValue(sharedPreferences.getState());
        }

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

    }

    @Override
    public void onSuccess() {

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
    }

    @Override
    public void onSuccessGetcities(List<AllCitiesList> allCitiesLists) {
        cityAdapter.setCitiesLists(allCitiesLists);
    }

    @Override
    public void onStateItemClick(AllStatesList state) {
        sharedPreferences.setState(state.getName());
        homeViewModel.stateLiveData.setValue(state.getName());
        homeViewModel.getCities(sharedPreferences.getUserAuthTokenKey(), state.getId());
    }

    @Override
    public void onCityItemClick(AllCitiesList city) {
        sharedPreferences.setCity(city.getName());
        homeViewModel.cityLiveData.setValue(city.getName());
        stateCityDialog.dismiss();
    }
}
