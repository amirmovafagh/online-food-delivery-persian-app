package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantInfoFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantInfoSharedViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantInfoFragmentViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantInfoFragmentInterface;

public class RestaurantInfoFragment extends Fragment implements RestaurantInfoFragmentInterface {
    @Inject
    RestaurantInfoFragmentViewModelFactory factory;
    @Inject
    Application application;
    @Inject
    MySharedPreferences sharedPreferences;

    private RestaurantInfoFragmentBinding binding;


    RestaurantInfoViewModel viewModel;
    RestaurantInfoSharedViewModel sharedViewModel;

    // Create supportMapFragment
    SupportMapFragment mapFragment;



    public static RestaurantInfoFragment newInstance() {
        return new RestaurantInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(application, getString(R.string.mapbox_access_token));

        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_info_fragment, container, false);
        viewModel = ViewModelProviders.of(this, factory).get(RestaurantInfoViewModel.class);
        viewModel.infoFragmentInterface = this;
        binding.setInfoViewModel(viewModel);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(RestaurantInfoSharedViewModel.class);
        binding.setLifecycleOwner(this);
        sharedViewModel.infoResponseMutableLiveData.observe(this, restaurantInfoResponse -> {
            viewModel.setRestaurantInfo(restaurantInfoResponse);
        });



        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {

            // Create fragment
            final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            // Build mapboxMap
            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(getActivity(), null);
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(31.839271, 54.361095))
                    .zoom(15)
                    .build());

                // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);

        // Add map fragment to parent container
            transaction.add(R.id.container_map, mapFragment, "com.mapbox.map");
            transaction.commit();

        } else {
            mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentByTag("com.mapbox.map");

        }

        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    Log.i("amir21",""+mapFragment);

                    mapboxMap.setStyle("https://www.parsimap.com/styles/street.json", new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

            // Map is set up and the style has loaded. Now you can add data or make other map adjustments


                        }
                    });
                }
            });
        }

    }


    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(application, ""+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
