package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.boojanco.onlinefoodorder.R;

public class RestaurantCommentFragment extends Fragment {

    private RestaurantCommentViewModel mViewModel;

    public static RestaurantCommentFragment newInstance() {
        return new RestaurantCommentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_comment_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RestaurantCommentViewModel.class);
        // TODO: Use the ViewModel
    }

}
