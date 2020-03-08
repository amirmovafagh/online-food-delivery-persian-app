package ir.boojanco.onlinefoodorder.ui.fragments.favoriteRestaurants;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
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

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.FavoriteRestaurantsFragmentBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.FavoriteRestaurants;
import ir.boojanco.onlinefoodorder.viewmodels.FavoriteRestaurantsViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.FavoriteRestaurantsViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteRestaurantsFragmentInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteRestaurantsRecyclerViewInterface;

public class FavoriteRestaurantsFragment extends Fragment implements FavoriteRestaurantsFragmentInterface, FavoriteRestaurantsRecyclerViewInterface {

    private FavoriteRestaurantsFragmentBinding binding;
    private FavoriteRestaurantsViewModel viewModel;

    @Inject
    FavoriteRestaurantsViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;

    private RecyclerView recyclerViewFaveRestaurants;
    private FavoriteRestaurantsAdapter favoriteRestaurantsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.favorite_restaurants_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(FavoriteRestaurantsViewModel.class);
        viewModel.setFragmentInterface(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.getFavoriteRestaurants(sharedPreferences.getUserAuthTokenKey());
        recyclerViewFaveRestaurants = binding.recyclerViewFavoriteRestaurants;
        recyclerViewFaveRestaurants.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        recyclerViewFaveRestaurants.setHasFixedSize(true);
        recyclerViewFaveRestaurants.setItemViewCacheSize(20);
        favoriteRestaurantsAdapter = new FavoriteRestaurantsAdapter(this);
        recyclerViewFaveRestaurants.setAdapter(favoriteRestaurantsAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.userFavoriteRestaurantsPagedListLiveData.observe(getActivity(), favoriteRestaurants -> favoriteRestaurantsAdapter.submitList(favoriteRestaurants));

    }

    @Override
    public void onStarted() {
        binding.animationViewLoadRequest.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess() {
        binding.animationViewLoadRequest.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(application, "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecyclerViewFaveRestaurantClick(View v, FavoriteRestaurants favoriteRestaurant) {
        Toast.makeText(application, "" + favoriteRestaurant.getId(), Toast.LENGTH_SHORT).show();
    }
}
