package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Random;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.databinding.RestaurantDetailsFragmentBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.util.reviewratings.BarLabels;
import ir.boojanco.onlinefoodorder.util.reviewratings.RatingReviews;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantDetailsViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantInfoSharedViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantDetailsViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantDetailsInterface;

public class RestaurantDetailsFragment extends Fragment implements RestaurantDetailsInterface {
    private static final String TAG = RestaurantDetailsFragment.class.getSimpleName();

    @Inject
    RestaurantDetailsViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;
    @Inject
    CartDataSource cartDataSource;


    private RestaurantInfoSharedViewModel sharedViewModel;
    private RestaurantDetailsViewModel viewModel;
    private RestaurantDetailsFragmentBinding binding;
    private RatingReviews ratingReviews;
    private TabLayout tabLayout;

    public static RestaurantDetailsFragment newInstance() {
        return new RestaurantDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_details_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(RestaurantDetailsViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(RestaurantInfoSharedViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.setHandler(this);
        binding.setManager(getChildFragmentManager());
        viewModel.setFragmentInterface(this);
        tabLayout = binding.tabs;
        ratingReviews = binding.ratingReviews;

        assert getArguments() != null;
        if (getArguments().getLong("restaurantID") != 0) {
            viewModel.getRestaurantInfo(sharedPreferences.getUserAuthTokenKey(), getArguments().getLong("restaurantID", 0));


        }

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @BindingAdapter({"handler"})
    public static void bindViewPagerAdapter(final ViewPager2 viewPager2, final RestaurantDetailsFragment fragment) {
        final RestaurantDetailsPagerAdapter adapter = new RestaurantDetailsPagerAdapter(fragment, fragment.getArguments());
        viewPager2.setAdapter(adapter);
    }

    @BindingAdapter({"pager"})
    public static void bindViewPagerTabs(final TabLayout tabLayout, final ViewPager2 viewPager) {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(RestaurantDetailsPagerAdapter.getPageTitle(position))
        ).attach();
    }


    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void initRatingViews(int[] colors, int[] raters) {
        ratingReviews.createRatingBars(100, BarLabels.STYPE_CUSTOM, colors, raters);
    }

    @Override
    public void onSuccess(RestaurantInfoResponse restaurantInfo) {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        sharedViewModel.infoResponseMutableLiveData.postValue(restaurantInfo);
    }

    @Override
    public void onFailure(String error) {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
