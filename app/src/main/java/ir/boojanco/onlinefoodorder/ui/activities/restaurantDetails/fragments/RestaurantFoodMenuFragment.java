package ir.boojanco.onlinefoodorder.ui.activities.restaurantFood.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.boojanco.onlinefoodorder.R;

public class RestaurantFoodMenuFragment extends Fragment {

    private RestaurantFoodMenuViewModel mViewModel;

    public static RestaurantFoodMenuFragment newInstance() {
        return new RestaurantFoodMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_food_menu_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RestaurantFoodMenuViewModel.class);
        // TODO: Use the ViewModel
    }

}
