package ir.boojanco.onlinefoodorder.ui.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    public static RestaurantFragment newInstance() {
        return new RestaurantFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_fragment,container, false);
        restaurantViewModel = ViewModelProviders.of(this, factory).get(RestaurantViewModel.class);
        binding.setRestaurantViewModel(restaurantViewModel);
        binding.setLifecycleOwner(this);
        restaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);
        Toast.makeText(getContext(), ""+sharedPreferences.getUserAuthTokenKey(), Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.restaurant_fragment, container, false);

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

    }

    @Override
    public void onSuccess(LiveData<LastRestaurantResponse> liveData) {

    }

    @Override
    public void onFailure(String error) {

    }
}
