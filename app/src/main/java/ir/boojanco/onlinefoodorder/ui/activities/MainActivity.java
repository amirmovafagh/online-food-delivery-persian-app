package ir.boojanco.onlinefoodorder.ui.activities;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

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


public class MainActivity extends BaseActivity implements MainNavigator {
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

        fm.beginTransaction().add(R.id.fragment_frame_layout, cartFragment, "3").hide(cartFragment).commit();
        fm.beginTransaction().add(R.id.fragment_frame_layout, userProfileFragment, "3").hide(userProfileFragment).commit();
        fm.beginTransaction().add(R.id.fragment_frame_layout, restaurantFragment, "2").hide(restaurantFragment).commit();
        fm.beginTransaction().add(R.id.fragment_frame_layout, homeFragment, "1").commit();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.home_fragment:
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        return true;

                    case R.id.cart_fragment:
                        fm.beginTransaction().hide(active).show(cartFragment).commit();
                        active = cartFragment;
                        return true;

                    case R.id.user_profile_fragment:
                        fm.beginTransaction().hide(active).show(userProfileFragment).commit();
                        active = userProfileFragment;
                        return true;
                    case R.id.restaurants_fragment:
                        fm.beginTransaction().hide(active).show(restaurantFragment).commit();
                        active = restaurantFragment;
                        return true;
                }
                return false;
            };
}
