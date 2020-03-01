package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.OrderItem;
import ir.boojanco.onlinefoodorder.ui.fragments.userorders.OrdersDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.userorders.OrdersDataSourceFactory;
import ir.boojanco.onlinefoodorder.ui.fragments.userorders.OrdersDataSourceInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.OrdersFragmentInterface;

public class OrdersViewModel extends ViewModel implements OrdersDataSourceInterface {
    private Context context;
    private UserRepository userRepository;
    public OrdersFragmentInterface fragmentInterface;

    public LiveData<PagedList<OrderItem>> userOrdersPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, OrderItem>> userOrdersPageKeyedDataSourceLiveData;

    public OrdersViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public void getUserOrders(String authToken) {
        OrdersDataSourceFactory ordersDataSourceFactory = new OrdersDataSourceFactory(userRepository, this, authToken);
        userOrdersPageKeyedDataSourceLiveData = ordersDataSourceFactory.getOrdersLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(OrdersDataSource.PAGE_SIZE)
                        .build();
        userOrdersPagedListLiveData = (new LivePagedListBuilder(ordersDataSourceFactory, config)).build();
    }

    @Override
    public void onStartedOrdersData() {
        fragmentInterface.onStarted();
    }

    @Override
    public void onSuccessOrdersData() {
        fragmentInterface.onSuccess();
    }

    @Override
    public void onFailureOrdersData(String error) {
        fragmentInterface.onFailure(error);
    }
}
