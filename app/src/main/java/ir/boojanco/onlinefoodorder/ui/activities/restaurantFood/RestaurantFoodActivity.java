package ir.boojanco.onlinefoodorder.ui.activities.restaurantFood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityRestaurantFoodBinding;
import ir.boojanco.onlinefoodorder.models.food.getAllFood.AllFoodList;
import ir.boojanco.onlinefoodorder.models.food.getAllFood.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantFoodViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantFoodViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFoodInterface;

public class RestaurantFoodActivity extends AppCompatActivity implements RestaurantFoodInterface, RecyclerViewRestaurantFoodClickListener {
    private static final String TAG = RestaurantFoodActivity.class.getSimpleName();

    RestaurantFoodViewModel restaurantFoodViewModel;
    ActivityRestaurantFoodBinding binding;

    @Inject
    Application application;
    @Inject
    RestaurantFoodViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    private RecyclerView recyclerView;
    private RestaurantFoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);

        //get viewModel
        restaurantFoodViewModel = ViewModelProviders.of(this,factory).get(RestaurantFoodViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_food);
        binding.setFoodViewModel(restaurantFoodViewModel);
        binding.setLifecycleOwner(this);
        restaurantFoodViewModel.foodInterface = this;
        TabLayout tabLayout= findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("TAb1");

        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("TAb2");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int extraRestauranId = extras.getInt("RESTAURANT_ID",0);
            recyclerView = binding.recyclerViewRestauranFood;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            adapter = new RestaurantFoodAdapter(this);
            recyclerView.setAdapter(adapter);
            restaurantFoodViewModel.getAllFood(sharedPreferences.getUserAuthTokenKey(),extraRestauranId);
        }

    }

    @Override
    public void onStarted() {
        Toast.makeText(application, "start", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(MutableLiveData<GetAllFoodResponse> liveData) {
        liveData.observe(this, getAllFoodResponse -> {
            adapter.setFoodLists(getAllFoodResponse.getAllFoodList());
        });
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(application, ""+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecyclerViewItemClick(View v, AllFoodList allFoodList) {

    }
}
