package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantInfoFragmentBinding;
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
        sharedViewModel = new ViewModelProvider(getActivity()).get(RestaurantInfoSharedViewModel.class);
        binding.setLifecycleOwner(this);
        sharedViewModel.infoResponseMutableLiveData.observe(getViewLifecycleOwner(), restaurantInfoResponse -> {
            viewModel.setRestaurantInfo(restaurantInfoResponse);
        });

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
        if(map != null){
            this.googleMap = map;
            LatLng restaurantLatLng = new LatLng(31.839271, 54.361095);

            googleMap.addMarker(new MarkerOptions().position(restaurantLatLng).title("Marker in Restaurant"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLatLng,16));



            /*// Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);*/

            /*Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(getContext(), new Locale("fa"));

            try {
                addresses = geocoder.getFromLocation(31.839271, 54.361095, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            Toast.makeText(application, "آدرس: " + address, Toast.LENGTH_LONG).show();
            Toast.makeText(application, "استان: " + state, Toast.LENGTH_LONG).show();
            Toast.makeText(application, "شهر: " + city ,Toast.LENGTH_LONG).show();
            Toast.makeText(application, "نام شناخته شده: " + knownName ,Toast.LENGTH_LONG).show();
            Toast.makeText(application, "getPremises: " + addresses.get(0).getPremises() ,Toast.LENGTH_LONG).show();
            Toast.makeText(application, "getAdminArea: " + addresses.get(0).getAdminArea() ,Toast.LENGTH_LONG).show();
            Toast.makeText(application, "getPhone: " + addresses.get(0).getPhone() ,Toast.LENGTH_LONG).show();
            Toast.makeText(application, "getPostalCode: " + addresses.get(0).getPostalCode() ,Toast.LENGTH_LONG).show();
            Toast.makeText(application, "getSubAdminArea: " + addresses.get(0).getSubAdminArea() ,Toast.LENGTH_LONG).show();
            Toast.makeText(application, "getSubLocality: " + addresses.get(0).getSubLocality() ,Toast.LENGTH_LONG).show();
            Toast.makeText(application, "getThoroughfare: " + addresses.get(0).getThoroughfare() ,Toast.LENGTH_LONG).show();
            Toast.makeText(application, "getUrl: " + addresses.get(0).getUrl() ,Toast.LENGTH_LONG).show();*/



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



}
