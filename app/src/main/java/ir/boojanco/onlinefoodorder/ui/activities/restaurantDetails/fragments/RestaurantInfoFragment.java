package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.boojanco.onlinefoodorder.R;

public class RestaurantInfoFragment extends Fragment {

    private RestaurantInfoViewModel mViewModel;

    public static RestaurantInfoFragment newInstance() {
        return new RestaurantInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_info_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RestaurantInfoViewModel.class);
        // TODO: Use the ViewModel
    }

}
