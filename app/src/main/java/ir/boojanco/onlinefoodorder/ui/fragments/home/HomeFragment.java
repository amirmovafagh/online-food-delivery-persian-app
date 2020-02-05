package ir.boojanco.onlinefoodorder.ui.fragments.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

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
    private ArrayList<FoodTypeFilterItem> foodTypeFilterItems;
    private FoodTypeSearchFilterAdapter adapter;
    private RecyclerView recyclerViewFoodTypeSearchFilter;

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
        homeViewModel = new ViewModelProvider(this,factory).get(HomeViewModel.class);
        binding.setHomeViewModel(homeViewModel);
        binding.setLifecycleOwner(this);
        sliderView = binding.imageSlider;
        SnapHelper snapHelper = new PagerSnapHelper(); //for stay on center
        recyclerViewFoodTypeSearchFilter = binding.recyclerviewFoodTypeSearchFilterHome;

        recyclerViewFoodTypeSearchFilter.setLayoutManager(new LinearLayoutManager(getActivity().getApplication(), LinearLayoutManager.HORIZONTAL,false));
        recyclerViewFoodTypeSearchFilter.canScrollHorizontally(0);
        recyclerViewFoodTypeSearchFilter.setHasFixedSize(true);
        snapHelper.attachToRecyclerView(recyclerViewFoodTypeSearchFilter);
        adapter = new FoodTypeSearchFilterAdapter();
        recyclerViewFoodTypeSearchFilter.setAdapter(adapter);
        recyclerViewFoodTypeSearchFilter.setItemViewCacheSize(20);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sliderView.setSliderAdapter(new SliderAdapterHome(getContext()));
        foodTypeFilterItems = new ArrayList<>();
        foodTypeFilterItems.add(new FoodTypeFilterItem("برگر", getResources().getIdentifier("@drawable/burger", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("چینی", getResources().getIdentifier("@drawable/chinese", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("غذای اصلی", getResources().getIdentifier("@drawable/main_course", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("پیتزا", getResources().getIdentifier("@drawable/pizza", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("سوپ", getResources().getIdentifier("@drawable/soup", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("برگر", getResources().getIdentifier("@drawable/burger", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("چینی", getResources().getIdentifier("@drawable/chinese", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("غذای اصلی", getResources().getIdentifier("@drawable/main_course", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("پیتزا", getResources().getIdentifier("@drawable/pizza", "drawable", getActivity().getPackageName())));
        foodTypeFilterItems.add(new FoodTypeFilterItem("سوپ", getResources().getIdentifier("@drawable/soup", "drawable", getActivity().getPackageName())));

        adapter.setFoodTypeFilterItems(foodTypeFilterItems);

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
