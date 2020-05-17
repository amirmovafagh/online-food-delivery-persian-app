package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantFragmentBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;
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
    private NavController navController;


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

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        searchView = binding.search;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });

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
        boolean searchByLocation = getArguments().getBoolean("isSearchByLocation");
        boolean searchByCategory = getArguments().getBoolean("isSearchByCategory");
        boolean searchByRestaurantName = getArguments().getBoolean("isSearchByName");
        String cityName = sharedPreferences.getCity();
        if (!searchByCategory && !searchByLocation && !searchByRestaurantName) //show all restaurants in the city
            restaurantViewModel.getAllSearchedRestaurant("", cityName, "");
        else {

            if (searchByLocation)//search restaurants by location
                restaurantViewModel.getAllSearchedByLocation(sharedPreferences.getLatitude(), sharedPreferences.getLongitud());
            else if (searchByCategory) //search restaurants by category
                restaurantViewModel.getAllSearchedByCategory(getArguments().getString("categoryName"), cityName);
            else //search restaurants by restaurant name
                restaurantViewModel.getAllSearchedRestaurant(getArguments().getString("restaurantName"), cityName, "");

        }

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
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + error, Snackbar.LENGTH_SHORT);
        snackbar.show();
        binding.animationViewLoadRequest.setVisibility(View.GONE);
    }

    @Override
    public void onRecyclerViewItemClick(View v, RestaurantList restaurantList) {
        switch (v.getId()) {
            case R.id.toggle_bookmark:

                break;
            case R.id.cons_layout:
                Bundle bundle = new Bundle();
                bundle.putLong("restaurantID", restaurantList.getId());
                navController.navigate(R.id.action_restaurantFragment_to_restaurantDetailsFragment, bundle);
                break;
        }
    }
}
