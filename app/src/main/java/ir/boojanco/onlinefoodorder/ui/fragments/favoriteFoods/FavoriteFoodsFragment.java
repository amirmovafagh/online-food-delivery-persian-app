package ir.boojanco.onlinefoodorder.ui.fragments.favoriteFoods;

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

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.FavoriteFoodsFragmentBinding;
import ir.boojanco.onlinefoodorder.models.food.FavoriteFoods;
import ir.boojanco.onlinefoodorder.viewmodels.FavoriteFoodsViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.FavoriteFoodsViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteFoodsFragmentInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteFoodsRecyclerViewInterface;


public class FavoriteFoodsFragment extends Fragment implements FavoriteFoodsFragmentInterface, FavoriteFoodsRecyclerViewInterface {

    private FavoriteFoodsFragmentBinding binding;
    private FavoriteFoodsViewModel viewModel;

    @Inject
    FavoriteFoodsViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;

    private RecyclerView recyclerViewFaveFoods;
    private FavoriteFoodsAdapter favoriteFoodsAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.favorite_foods_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(FavoriteFoodsViewModel.class);
        viewModel.setFragmentInterface(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.getFavoriteFoods(sharedPreferences.getUserAuthTokenKey());
        recyclerViewFaveFoods = binding.recyclerViewFavoriteFoods;
        recyclerViewFaveFoods.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        recyclerViewFaveFoods.setHasFixedSize(true);
        recyclerViewFaveFoods.setItemViewCacheSize(20);
        favoriteFoodsAdapter = new FavoriteFoodsAdapter(this);
        recyclerViewFaveFoods.setAdapter(favoriteFoodsAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.userFavoriteFoodsPagedListLiveData.observe(getActivity(), favoriteFoods -> favoriteFoodsAdapter.submitList(favoriteFoods));

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
        Toast.makeText(application, "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecyclerViewFaveFoodsClick(View v, FavoriteFoods favoriteFoods) {
        Toast.makeText(application, "" + favoriteFoods.getId(), Toast.LENGTH_SHORT).show();
    }
}
