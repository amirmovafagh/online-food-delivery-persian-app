package ir.boojanco.onlinefoodorder.ui.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.TransitionManager;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityMainBinding;
import ir.boojanco.onlinefoodorder.viewmodels.MainViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.MainViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.MainActivityInterface;


public class MainActivity extends AppCompatActivity implements MainActivityInterface {
    private static String TAG = MainActivity.class.getSimpleName();
    private MainViewModel viewModel;
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    private NavController navController;
    private int PERMISSION_ID = 17;
    private FusedLocationProviderClient locationProviderClient;

    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    MainViewModelFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
        // get view model
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        viewModel.setActivityInterface(this);
        bottomNavigationView = binding.bottomNavigation;

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.restaurantDetailsFragment) {
                //toolbar.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                //toolbar.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

    }

    @BindingAdapter({"animatedVisibility"})
    public static void setAnimatedVisibility(View view, boolean isVisible) {
        TransitionManager.beginDelayedTransition((ViewGroup) view.getRootView());
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showSetLocationEnabledAlertDialog() {
        // setup the alert builder
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogCustomRTL);
        builder.setCancelable(false);
        builder.setTitle("تنظیمات موقعیت یاب");
        builder.setMessage("در صورت تمایل به استفاده از سیستم موقعیت یاب به منظور یافتن نزدیکترین رستوران ها این بخش را فعال کنید.");
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

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                locationProviderClient.getLastLocation().addOnCompleteListener(
                        task -> {
                            try {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    sharedPreferences.setLatitude(location.getLatitude());
                                    sharedPreferences.setLongitud(location.getLongitude());
                                    getCityNameFromParsiMap(location);
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

    private void getCityName(Location location) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, new Locale("fa"));
        Log.i(TAG, "" + location.getLatitude() + "   " + location.getLongitude());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            Toast.makeText(this, "" + address, Toast.LENGTH_SHORT).show();
            if (city != null)
                sharedPreferences.setCity(city);
            if (state != null)
                sharedPreferences.setState(state);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getCityNameFromParsiMap(Location location) {
        viewModel.getReverseAddressParsimap(location.getLatitude(), location.getLongitude());
    }

    //for remove focus with touch outside of edit text
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    Toast.makeText(this, result.getContents() + "", Toast.LENGTH_LONG).show();

                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    Toast.makeText(this, "" + obj.getString("name") + "  " + obj.getString("address"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(2);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationProviderClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            if (mLastLocation != null) {
                sharedPreferences.setLatitude(mLastLocation.getLatitude());
                sharedPreferences.setLongitud(mLastLocation.getLongitude());
                getCityNameFromParsiMap(mLastLocation);
            }

        }
    };


    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess(String state, String city) {
        if (state != null && !state.equals("") && city != null && !city.equals("")) {
            sharedPreferences.setState(state);
            sharedPreferences.setCity(city);
        }
    }

    @Override
    public void onFailure(String error) {

    }
}
