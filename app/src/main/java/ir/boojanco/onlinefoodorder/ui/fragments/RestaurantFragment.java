package ir.boojanco.onlinefoodorder.ui.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.RestaurantAdapter;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantFragmentBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantList;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFragmentInterface;

public class RestaurantFragment extends Fragment implements RestaurantFragmentInterface {
    private static final String TAG = RestaurantFragment.class.getSimpleName();
    @Inject
    RestaurantViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    private RestaurantViewModel restaurantViewModel;
    private RestaurantFragmentBinding binding;
    private RestaurantAdapter restaurantAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_fragment,container, false);
        restaurantViewModel = ViewModelProviders.of(this, factory).get(RestaurantViewModel.class);
        restaurantViewModel.restaurantInterface = this;
        binding.setRestaurantViewModel(restaurantViewModel);
        binding.setLifecycleOwner(this);

        RecyclerView recyclerView = binding.recyclerViewAllRestaurant;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        recyclerView.setHasFixedSize(true);
        restaurantAdapter = new RestaurantAdapter();
        recyclerView.setAdapter(restaurantAdapter);
        restaurantViewModel.getAllRestaurant(sharedPreferences.getUserAuthTokenKey());
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStarted() {
        Log.i(TAG, "AA OnStart");
    }

    @Override
    public void onSuccess(MutableLiveData<LastRestaurantResponse> data) {
        Log.i(TAG, "AA onSuccess");
        data.observe(this, lastRestaurantResponse -> {
            Log.i(TAG, "AA onSuccess in observer");
            Toast.makeText(getActivity(), ""+lastRestaurantResponse.getRestaurantsList(), Toast.LENGTH_SHORT).show();
            restaurantAdapter.setRestaurantsList(lastRestaurantResponse.getRestaurantsList());
        });
    }

    @Override
    public void onFailure(String error) {
        Log.i(TAG, "onFailure: "+error);
        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
    }

}
