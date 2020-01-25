package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.Application;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityRestaurantDetailsBinding;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantDetailsViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantFoodViewModelFactory;

public class RestaurantDetailsActivity extends AppCompatActivity  {
    private static final String TAG = RestaurantDetailsActivity.class.getSimpleName();

    RestaurantDetailsViewModel restaurantDetailsViewModel;
    ActivityRestaurantDetailsBinding binding;

    @Inject
    Application application;
    @Inject
    RestaurantFoodViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);

        //get viewModel
        restaurantDetailsViewModel = ViewModelProviders.of(this,factory).get(RestaurantDetailsViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_details);
        binding.setDetailsViewModel(restaurantDetailsViewModel);
        binding.setLifecycleOwner(this);
        binding.setHandler(this);
        binding.setManager(getSupportFragmentManager());

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String extraRestauranCover = extras.getString("RESTAURANT_COVER"," ");
            String extraRestauranLogo = extras.getString("RESTAURANT_LOGO"," ");
            restaurantDetailsViewModel.restaurantCover.setValue(extraRestauranCover);
            restaurantDetailsViewModel.restaurantLogo.setValue(extraRestauranLogo);
        }

    }

    @BindingAdapter({"handler"})
    public static void bindViewPagerAdapter(final ViewPager view, final RestaurantDetailsActivity activity)
    {
        final RestaurantFoodMenuPagerAdapter adapter = new RestaurantFoodMenuPagerAdapter( activity.getSupportFragmentManager(),0);
        view.setAdapter(adapter);
    }

    @BindingAdapter({"pager"})
    public static void bindViewPagerTabs(final TabLayout view, final ViewPager pagerView)
    {
        view.setupWithViewPager(pagerView, true);
        pagerView.setCurrentItem(1);


    }




}
