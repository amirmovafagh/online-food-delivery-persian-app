package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityRestaurantDetailsBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantDetailsViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantInfoSharedViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantDetailsViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantDetailsInterface;

public class RestaurantDetailsActivity extends AppCompatActivity implements RestaurantDetailsInterface {
    private static final String TAG = RestaurantDetailsActivity.class.getSimpleName();

    RestaurantDetailsViewModel restaurantDetailsViewModel;

    ActivityRestaurantDetailsBinding binding;

    @Inject
    Application application;
    @Inject
    RestaurantDetailsViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    RestaurantInfoSharedViewModel sharedViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);

        //get viewModel
        restaurantDetailsViewModel = ViewModelProviders.of(this,factory).get(RestaurantDetailsViewModel.class);
        sharedViewModel = ViewModelProviders.of(this).get(RestaurantInfoSharedViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_details);
        binding.setDetailsViewModel(restaurantDetailsViewModel);
        binding.setLifecycleOwner(this);
        binding.setHandler(this);
        binding.setManager(getSupportFragmentManager());
        restaurantDetailsViewModel.detailsInterface = this;

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            long extraRestauranId = extras.getLong("RESTAURANT_ID",0);
            /*String extraRestauranLogo = extras.getString("RESTAURANT_LOGO"," ");
            String extraRestauranName = extras.getString("RESTAURANT_NAME"," ");
            String extraRestauranTagList = extras.getString("RESTAURANT_TAG_LIST"," ");
            Float extraRestauranAverageScore = extras.getFloat("RESTAURANT_AVERAGE_SCORE",0);
            restaurantDetailsViewModel.restaurantCover.setValue(extraRestauranCover);
            restaurantDetailsViewModel.restaurantLogo.setValue(extraRestauranLogo);
            restaurantDetailsViewModel.restaurantAverageScore.setValue(extraRestauranAverageScore);
            restaurantDetailsViewModel.restaurantName.setValue(extraRestauranName);*/
            restaurantDetailsViewModel.getRestaurantInfo(sharedPreferences.getUserAuthTokenKey(), extraRestauranId);
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


    @SuppressLint("RestrictedApi")
    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
        binding.fab.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onSuccess(RestaurantInfoResponse restaurantInfo) {

        binding.cvWaitingResponse.setVisibility(View.GONE);
        binding.fab.setVisibility(View.VISIBLE);
        sharedViewModel.infoResponseMutableLiveData.postValue(restaurantInfo);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onFailure(String error) {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        binding.fab.setVisibility(View.VISIBLE);
        Toast.makeText(application, ""+error, Toast.LENGTH_LONG).show();
    }
}
