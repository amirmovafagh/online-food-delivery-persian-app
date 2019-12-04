package ir.boojanco.onlinefoodorder.ui.activities;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.databinding.ActivityMainBinding;
import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;
import ir.boojanco.onlinefoodorder.ui.base.BaseActivity;
import ir.boojanco.onlinefoodorder.ui.fragments.CartFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.HomeFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.RestaurantFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.UserProfileFragment;
import ir.boojanco.onlinefoodorder.ui.navigator.MainNavigator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get view model
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMain(mainViewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }


}
