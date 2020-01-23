package ir.boojanco.onlinefoodorder.ui.activities;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityMainBinding;
import ir.boojanco.onlinefoodorder.ui.fragments.CartFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.home.HomeFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.UserProfileFragment;
import ir.boojanco.onlinefoodorder.viewmodels.MainViewModel;


public class MainActivity extends AppCompatActivity  {
    private static String TAG = "regTest";
    private MainViewModel mainViewModel;
    ActivityMainBinding binding;
    final Fragment homeFragment = new HomeFragment();
    final Fragment cartFragment = new CartFragment();
    final Fragment restaurantFragment = new RestaurantFragment();
    final Fragment userProfileFragment = new UserProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;
    BottomNavigationView bottomNavigationView ;
    Toolbar myToolbar;



    @Inject
    MySharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }else Toast.makeText(this, "YOU HAVE PERMISSION", Toast.LENGTH_SHORT).show();


        // get view model
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMain(mainViewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        bottomNavigationView = binding.bottomNavigation;
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());
        myToolbar = binding.myToolbar;
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(null);

        Toast.makeText(this, ""+sharedPreferences.getUserAuthTokenKey(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
