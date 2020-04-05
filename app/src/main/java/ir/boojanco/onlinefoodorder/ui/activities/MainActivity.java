package ir.boojanco.onlinefoodorder.ui.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.TransitionManager;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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


public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private MainViewModel mainViewModel;
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    Toolbar myToolbar;

    private int PERMISSION_ID = 17;
    private FusedLocationProviderClient locationProviderClient;

    @Inject
    MySharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
        // get view model
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMain(mainViewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        bottomNavigationView = binding.bottomNavigation;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {
                                sharedPreferences.setLatitude(location.getLatitude());
                                sharedPreferences.setLongitud(location.getLongitude());
                                getCityName(location);
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
        Log.i(TAG,""+location.getLatitude()+"   "+ location.getLongitude());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            Toast.makeText(this, "آدرس: " + address, Toast.LENGTH_LONG).show();
            Toast.makeText(this, "استان: " + state, Toast.LENGTH_LONG).show();
            Toast.makeText(this, "شهر: " + city ,Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    @Override
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
                    Toast.makeText(this, result.getContents()+"", Toast.LENGTH_LONG).show();

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
    }
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
                getCityName(mLastLocation);
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.restaurants_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
