package ir.boojanco.onlinefoodorder.ui.fragments.userorders;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.OrdersFragmentBinding;
import ir.boojanco.onlinefoodorder.models.user.GetUserOrderCommentResponse;
import ir.boojanco.onlinefoodorder.models.user.OrderFoodList;
import ir.boojanco.onlinefoodorder.models.user.OrderItem;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.viewmodels.OrdersViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.OrdersViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FoodOrdersDialogInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.OrdersFragmentInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.OrdersRecyclerViewInterface;

public class OrdersFragment extends Fragment implements OrdersFragmentInterface, OrdersRecyclerViewInterface, FoodOrdersDialogInterface {

    private OrdersFragmentBinding binding;

    private OrdersViewModel viewModel;
    @Inject
    OrdersViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;
    private OrderItem orderItem;
    private CustomFoodOrdersDialog customFoodOrdersDialog;
    private RecyclerView recyclerViewUserOrders;
    private OrdersAdapter ordersAdapter;
    private Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.orders_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(OrdersViewModel.class);
        viewModel.fragmentInterface = this;
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        toolbar = binding.toolbar;

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        viewModel.getUserOrders(sharedPreferences.getUserAuthTokenKey());
        recyclerViewUserOrders = binding.recyclerViewOrders;
        recyclerViewUserOrders.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        recyclerViewUserOrders.setHasFixedSize(true);
        ordersAdapter = new OrdersAdapter(this, application);
        recyclerViewUserOrders.setAdapter(ordersAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.userOrdersPagedListLiveData.observe(getActivity(), orderItems -> ordersAdapter.submitList(orderItems));
    }

    @Override
    public void onRecyclerViewOrderClick(View v, OrderItem orderItem) {
        this.orderItem = orderItem;
        viewModel.getOrderComment(orderItem.getId());
        /**/
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
    public void onSuccessOrderComment(GetUserOrderCommentResponse commentResponse) {
        if (commentResponse != null && orderItem != null) {

            customFoodOrdersDialog = new CustomFoodOrdersDialog(getActivity(), R.style.Theme_Custom_Dialog, this, orderItem, commentResponse);
            customFoodOrdersDialog.setCanceledOnTouchOutside(true);
            if (customFoodOrdersDialog != null && !customFoodOrdersDialog.isShowing())
                customFoodOrdersDialog.show();
            else
                onFailure("خطا در دریافت اطلاعات ");
        }

    }

    @Override
    public void onFailure(String error) {
        binding.animationViewLoadRequest.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + error, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.materialGray900))
                .setBackgroundTint(getResources().getColor(R.color.colorLowGold));
        snackbar.show();

    }

    @Override
    public void addComment(long orderId, float foodQuality, float systemEx, float arrivalTime, float personnelBehaviour, String userComment) {
        viewModel.addOrderComment(orderId, foodQuality, systemEx, arrivalTime, personnelBehaviour, userComment);
    }

    @Override
    public void showMessage(String msg) {
        onFailure(msg);
    }
}
