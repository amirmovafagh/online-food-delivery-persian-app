package ir.boojanco.onlinefoodorder.ui.fragments.usertransactions;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.TransactionsFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.TransactionsViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.TransactionsViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.TransactionsFragmentInterface;

public class TransactionsFragment extends Fragment implements TransactionsFragmentInterface {

    private TransactionsFragmentBinding binding;

    private TransactionsViewModel viewModel;
    @Inject
    TransactionsViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;

    private RecyclerView recyclerViewUserWalletActivities;
    private TransactionsAdapter transactionsAdapter;
    private Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.transactions_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(TransactionsViewModel.class);
        viewModel.fragmentInterface = this;
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        toolbar = binding.toolbar;

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        viewModel.getUserWalletActivities(sharedPreferences.getUserAuthTokenKey());
        recyclerViewUserWalletActivities = binding.recyclerViewWalletActivities;
        recyclerViewUserWalletActivities.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        recyclerViewUserWalletActivities.setHasFixedSize(true);
        transactionsAdapter = new TransactionsAdapter(getContext());
        recyclerViewUserWalletActivities.setAdapter(transactionsAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.transactionsPagedListLiveData.observe(getActivity(), walletActivities -> transactionsAdapter.submitList(walletActivities));
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
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
