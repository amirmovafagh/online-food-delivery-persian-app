package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantInfoFragmentBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantInfoSharedViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantInfoFragmentViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantInfoFragmentInterface;

public class RestaurantInfoFragment extends Fragment implements RestaurantInfoFragmentInterface {
    @Inject
    RestaurantInfoFragmentViewModelFactory factory;
    @Inject
    Application application;
    @Inject
    MySharedPreferences sharedPreferences;

    private RestaurantInfoFragmentBinding binding;


    RestaurantInfoViewModel viewModel;
    RestaurantInfoSharedViewModel sharedViewModel;



    public static RestaurantInfoFragment newInstance() {
        return new RestaurantInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_info_fragment, container, false);
        viewModel = ViewModelProviders.of(this, factory).get(RestaurantInfoViewModel.class);
        viewModel.infoFragmentInterface = this;
        binding.setInfoViewModel(viewModel);
        binding.setLifecycleOwner(this);

        sharedViewModel = ViewModelProviders.of(getActivity()).get(RestaurantInfoSharedViewModel.class);
        binding.setLifecycleOwner(this);
        sharedViewModel.infoResponseMutableLiveData.observe(this, restaurantInfoResponse -> {
            viewModel.setRestaurantInfo(restaurantInfoResponse);
        });

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
        Toast.makeText(application, ""+error, Toast.LENGTH_SHORT).show();
    }
}
