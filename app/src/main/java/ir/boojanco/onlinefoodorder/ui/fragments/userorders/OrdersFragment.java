package ir.boojanco.onlinefoodorder.ui.fragments.userorders;

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

import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.OrdersFragmentBinding;
import ir.boojanco.onlinefoodorder.models.user.OrderFoodList;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.viewmodels.OrdersViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.OrdersViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.OrdersFragmentInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.OrdersRecyclerViewInterface;

public class OrdersFragment extends Fragment implements OrdersFragmentInterface, OrdersRecyclerViewInterface {

    private OrdersFragmentBinding binding;

    private OrdersViewModel viewModel;
    @Inject
    OrdersViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    Application application;

    private CustomFoodOrdersDialog customFoodOrdersDialog;
    private RecyclerView recyclerViewUserOrders;
    private OrdersAdapter ordersAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.orders_fragment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(OrdersViewModel.class);
        viewModel.fragmentInterface = this;
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
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
    public void onRecyclerViewOrderClick(View v, List<OrderFoodList> foodLists) {
        customFoodOrdersDialog = new CustomFoodOrdersDialog(getActivity(), foodLists);
        customFoodOrdersDialog.setCanceledOnTouchOutside(true);
        if (customFoodOrdersDialog != null && !customFoodOrdersDialog.isShowing())
            customFoodOrdersDialog.show();
        else Toast.makeText(application, "خطا در دریافت اطلاعات ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(application, "" + error, Toast.LENGTH_SHORT).show();
    }
}
