package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantList;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.RestaurantDetailsActivity;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RestaurantViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFragmentInterface;

public class RestaurantFragment extends Fragment implements RestaurantFragmentInterface, RecyclerViewRestaurantClickListener {
    private static final String TAG = RestaurantFragment.class.getSimpleName();
    @Inject
    RestaurantViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    private RestaurantViewModel restaurantViewModel;
    private RestaurantFragmentBinding binding;
    private RestaurantAdapter restaurantAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.restaurant_fragment,container, false);
        restaurantViewModel = ViewModelProviders.of(this, factory).get(RestaurantViewModel.class);
        restaurantViewModel.restaurantInterface = this;
        binding.setRestaurantViewModel(restaurantViewModel);
        binding.setLifecycleOwner(this);

        recyclerView = binding.recyclerViewAllRestaurant;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        recyclerView.setHasFixedSize(true);
        restaurantAdapter = new RestaurantAdapter(this);
        recyclerView.setAdapter(restaurantAdapter);
        restaurantViewModel.getAllRestaurant(sharedPreferences.getUserAuthTokenKey());
        return binding.getRoot();

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
        Log.e(TAG, "AA OnStart");
    }

    @Override
    public void onSuccess(MutableLiveData<LastRestaurantResponse> data) {
        Log.i(TAG, "AA onSuccess");
        data.observe(this, lastRestaurantResponse -> {
            Log.e(TAG, "AA onSuccess in observer");
            restaurantAdapter.setRestaurantsList(lastRestaurantResponse.getRestaurantsList());
            recyclerView.scheduleLayoutAnimation();

        });
    }

    @Override
    public void onFailure(String error) {
        Log.e(TAG, "onFailure: "+error);
        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecyclerViewItemClick(View v, LastRestaurantList restaurantList) {
        switch (v.getId()){
            case R.id.toggle_bookmark:
                Toast.makeText(getActivity(), "bookmark: "+ restaurantList.getName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.cons_layout:

                Intent intent = new Intent(getContext(), RestaurantDetailsActivity.class);
                intent.putExtra("RESTAURANT_ID",restaurantList.getId());
                /*intent.putExtra("RESTAURANT_COVER", restaurantList.getCover());
                intent.putExtra("RESTAURANT_LOGO", restaurantList.getLogo());
                intent.putExtra("RESTAURANT_NAME", restaurantList.getName());
                intent.putExtra("RESTAURANT_AVERAGE_SCORE", restaurantList.getAverageScore());
                intent.putExtra("RESTAURANT_TAG_LIST", restaurantList.getTagList());*/
                startActivity(intent);
                break;
        }
    }
}
