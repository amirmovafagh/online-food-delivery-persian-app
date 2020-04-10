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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

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

        ratingReviews = binding.ratingReviews;

        assert getArguments() != null;
        if (getArguments().getLong("restaurantID") != 0) {
            viewModel.getRestaurantInfo(sharedPreferences.getUserAuthTokenKey(), getArguments().getLong("restaurantID", 0));


        }

        int colors[] = new int[]{
                Color.parseColor("#0e9d58"),
                Color.parseColor("#bfd047"),
                Color.parseColor("#ffc105"),
                };

        int raters[] = new int[]{
                new Random().nextInt(5),
                new Random().nextInt(5),
                new Random().nextInt(5)
        };

        ratingReviews.createRatingBars(5, BarLabels.STYPE_CUSTOM, colors, raters);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }

    @BindingAdapter({"handler"})
    public static void bindViewPagerAdapter(final ViewPager view, final RestaurantDetailsFragment fragment) {
        final RestaurantDetailsPagerAdapter adapter = new RestaurantDetailsPagerAdapter(fragment.getChildFragmentManager(), 0, fragment.getArguments());
        view.setAdapter(adapter);
    }

    @BindingAdapter({"pager"})
    public static void bindViewPagerTabs(final TabLayout view, final ViewPager pagerView) {
        view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        view.setupWithViewPager(pagerView, true);
        pagerView.setCurrentItem(1);
    }

    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
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
