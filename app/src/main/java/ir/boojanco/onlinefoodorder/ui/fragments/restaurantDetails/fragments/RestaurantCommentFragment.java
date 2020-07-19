package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.RestaurantCommentFragmentBinding;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.RestaurantCommentAdapter;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantInfoSharedViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantCommentFragmentViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantCommentFragmentInterface;

public class RestaurantCommentFragment extends Fragment implements RestaurantCommentFragmentInterface {

    private RestaurantCommentViewModel viewModel;
    private RestaurantCommentFragmentBinding binding;
    private RestaurantCommentAdapter commentAdapter;
    private RecyclerView commentRecyclerView;
    private RestaurantInfoSharedViewModel sharedViewModel;

    @Inject
    RestaurantCommentFragmentViewModelFactory factory;
    @Inject
    Application application;
    @Inject
    MySharedPreferences sharedPreferences;

    public static RestaurantCommentFragment newInstance() {
        return new RestaurantCommentFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_comment_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(RestaurantCommentViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity()).get(RestaurantInfoSharedViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setFragmentInterface(this);

        Bundle extras = getArguments();
        if (extras != null) {
            long extraRestauranId = extras.getLong("restaurantID");

            viewModel.getRestaurantComments(sharedPreferences.getUserAuthTokenKey(),extraRestauranId);
            commentRecyclerView = binding.recyclerViewRestaurantComment;
            commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
            commentRecyclerView.setHasFixedSize(false);
            commentAdapter = new RestaurantCommentAdapter();
            commentRecyclerView.setAdapter(commentAdapter);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.restaurantCommentsPagedListLiveData.observe(getViewLifecycleOwner(), restaurantComments -> commentAdapter.submitList(restaurantComments));

    }

    @Override
    public void onStarted() {
        binding.animationViewLoadRequest.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess() {
        binding.animationViewLoadRequest.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String error) {
        binding.animationViewLoadRequest.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + error, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.materialGray900))
                .setBackgroundTint(getResources().getColor(R.color.colorLowGold));
        snackbar.show();
    }
}
