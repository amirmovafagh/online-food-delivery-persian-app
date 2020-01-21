package ir.boojanco.onlinefoodorder.ui.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smarteist.autoimageslider.SliderView;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.databinding.HomeFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.HomeViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.HomeViewModelFactory;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    @Inject
    HomeViewModelFactory factory;

    private HomeViewModel homeViewModel;
    private HomeFragmentBinding binding;
    private SliderView sliderView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        homeViewModel = ViewModelProviders.of(this,factory).get(HomeViewModel.class);
        binding.setHomeViewModel(homeViewModel);
        binding.setLifecycleOwner(this);
        sliderView = binding.imageSlider;
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sliderView.setSliderAdapter(new SliderAdapterHome());
        // TODO: Use the ViewModel
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

    }
}
