package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.app.LauncherActivity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantFoodMenuFragmentBinding;
import ir.boojanco.onlinefoodorder.models.food.getAllFood.AllFoodList;
import ir.boojanco.onlinefoodorder.models.food.getAllFood.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.FoodTypeHeader;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.ListItemType;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.RecyclerViewRestaurantFoodMenuClickListener;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.RestaurantFoodMenuAdapter;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantFoodMenuViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFoodMenuInterface;

public class RestaurantFoodMenuFragment extends Fragment implements RestaurantFoodMenuInterface , RecyclerViewRestaurantFoodMenuClickListener {
    @Inject
    RestaurantFoodMenuViewModelFactory factory;
    @Inject
    Application application;
    @Inject
    MySharedPreferences sharedPreferences;

    private RestaurantFoodMenuViewModel restaurantFoodMenuViewModel;
    private RestaurantFoodMenuFragmentBinding binding;
    private RecyclerView recyclerView;
    private RestaurantFoodMenuAdapter adapter;

    public static RestaurantFoodMenuFragment newInstance() {
        return new RestaurantFoodMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_food_menu_fragment, container,false);
        restaurantFoodMenuViewModel = ViewModelProviders.of(this, factory).get(RestaurantFoodMenuViewModel.class);
        restaurantFoodMenuViewModel.foodInterface = this;
        binding.setFoodMenu(restaurantFoodMenuViewModel);
        binding.setLifecycleOwner(this);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null){
            int extraRestauranId = extras.getInt("RESTAURANT_ID",0);
            recyclerView = binding.recyclerViewRestauranFood;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
            recyclerView.setHasFixedSize(true);
            adapter = new RestaurantFoodMenuAdapter(this);
            recyclerView.setAdapter(adapter);

            restaurantFoodMenuViewModel.getAllFood(sharedPreferences.getUserAuthTokenKey(),extraRestauranId);

        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStarted() {
        Toast.makeText(application, "start", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(ArrayList<ListItemType> items) {
            recyclerView.scheduleLayoutAnimation();
            adapter.setFoodLists(items);
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(application, ""+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecyclerViewItemClick(View v, ListItemType items) {

    }
}
