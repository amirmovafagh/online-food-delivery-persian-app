package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantFragmentBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.RestaurantDetailsActivity;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFragmentInterface;

public class RestaurantFragment extends Fragment implements RestaurantFragmentInterface, RecyclerViewRestaurantClickListener {
    private static final String TAG = RestaurantFragment.class.getSimpleName();
    @Inject
    RestaurantViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    private RestaurantViewModel restaurantViewModel;
    private RestaurantFragmentBinding binding;
    private RestaurantAdapter restaurantAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private SearchView searchView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_fragment, container, false);
        restaurantViewModel = new ViewModelProvider(this, factory).get(RestaurantViewModel.class);
        restaurantViewModel.restaurantInterface = this;
        binding.setRestaurantViewModel(restaurantViewModel);
        binding.setLifecycleOwner(this);
        toolbar = binding.toolbar;
        searchView = binding.search;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), "onQueryTextSubmit: " + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getActivity(), "onQueryTextChange: " + newText, Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        recyclerView = binding.recyclerViewAllRestaurant;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        restaurantAdapter = new RestaurantAdapter(this);
        recyclerView.setAdapter(restaurantAdapter);


        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getArguments() != null;
        if (getArguments().getString("cityName") == null) {
            restaurantViewModel.getAllRestaurant(sharedPreferences.getUserAuthTokenKey());
        } else
            restaurantViewModel.getAllSearchedRestaurant(getArguments().getString("restaurantName"), getArguments().getString("cityName"), "");
        restaurantViewModel.restaurantPagedListLiveData.observe(getViewLifecycleOwner(), restaurantLists -> restaurantAdapter.submitList(restaurantLists));
    }

    @Override
    public void onStarted() {
        binding.animationViewLoadRequest.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess() {
        recyclerView.scheduleLayoutAnimation();
        binding.animationViewLoadRequest.setVisibility(View.GONE);
    }


    @Override
    public void onFailure(String error) {
        Log.e(TAG, "onFailure: " + error);
        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecyclerViewItemClick(View v, RestaurantList restaurantList) {
        switch (v.getId()) {
            case R.id.toggle_bookmark:
                Toast.makeText(getActivity(), "bookmark: " + restaurantList.getName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.cons_layout:

                Intent intent = new Intent(getContext(), RestaurantDetailsActivity.class);
                intent.putExtra("RESTAURANT_ID", restaurantList.getId());
                startActivity(intent);
                break;
        }
    }
}
