package ir.boojanco.onlinefoodorder.ui.fragments.favoriteRestaurants;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.FavoriteRestaurantsFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.FavoriteRestaurantsViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.FavoriteRestaurantsViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteRestaurantsFragmentInterface;

public class FavoriteRestaurantsFragment extends Fragment implements FavoriteRestaurantsFragmentInterface {

    private FavoriteRestaurantsFragmentBinding binding;
    private FavoriteRestaurantsViewModel viewModel;

    @Inject
    FavoriteRestaurantsViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.favorite_restaurants_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(FavoriteRestaurantsViewModel.class);
        viewModel.setFragmentInterface(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String error) {

    }
}
