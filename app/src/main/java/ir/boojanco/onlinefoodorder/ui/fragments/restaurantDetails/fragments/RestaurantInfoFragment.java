package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantInfoFragmentBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.MenuType;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantInfoSharedViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantInfoFragmentViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantInfoFragmentInterface;

public class RestaurantInfoFragment extends Fragment implements RestaurantInfoFragmentInterface, OnMapReadyCallback {
    @Inject
    RestaurantInfoFragmentViewModelFactory factory;
    @Inject
    Application application;
    @Inject
    MySharedPreferences sharedPreferences;

    private RestaurantInfoFragmentBinding binding;


    RestaurantInfoViewModel viewModel;
    RestaurantInfoSharedViewModel sharedViewModel;
    private RecyclerView recyclerViewRestaurantsMenuTypeInfo;
    private RestaurantMenuTypeInfoAdapter restaurantsMenuTypeAdapter;

    private GoogleMap googleMap;

    public static RestaurantInfoFragment newInstance() {
        return new RestaurantInfoFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.

        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_info_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(RestaurantInfoViewModel.class);
        viewModel.infoFragmentInterface = this;
        binding.setInfoViewModel(viewModel);
        sharedViewModel = new ViewModelProvider(getParentFragment()).get(RestaurantInfoSharedViewModel.class);
        binding.setLifecycleOwner(this);
        sharedViewModel.infoResponseMutableLiveData.observe(getViewLifecycleOwner(), restaurantInfoResponse -> {
            viewModel.setRestaurantInfo(restaurantInfoResponse);
        });

        recyclerViewRestaurantsMenuTypeInfo = binding.rvMenuTypeInfoItems;
        recyclerViewRestaurantsMenuTypeInfo.setLayoutManager(new LinearLayoutManager(application));
        recyclerViewRestaurantsMenuTypeInfo.setHasFixedSize(true);
        restaurantsMenuTypeAdapter = new RestaurantMenuTypeInfoAdapter();
        recyclerViewRestaurantsMenuTypeInfo.setAdapter(restaurantsMenuTypeAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.mapViewRestaurantLocation.onCreate(savedInstanceState);
        binding.mapViewRestaurantLocation.onResume();
        binding.mapViewRestaurantLocation.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (map != null) {
            this.googleMap = map;
            LatLng restaurantLatLng = new LatLng(31.839271, 54.361095);

            googleMap.addMarker(new MarkerOptions().position(restaurantLatLng).title("Marker in Restaurant"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLatLng, 16));



            /*// Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);*/

        }
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess(List<MenuType> menuType) {
        restaurantsMenuTypeAdapter.setMenuItems(menuType);
    }

    @Override
    public void onFailure(String error) {
        Snackbar snackbar = Snackbar.make(binding.frameLayoutInfoRestaurant, "" + error, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.materialGray900))
                .setBackgroundTint(getResources().getColor(R.color.colorLowGold));
        snackbar.show();
    }


}
