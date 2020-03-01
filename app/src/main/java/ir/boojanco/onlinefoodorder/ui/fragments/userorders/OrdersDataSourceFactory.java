package ir.boojanco.onlinefoodorder.ui.fragments.userorders;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.OrderItem;

public class OrdersDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, OrderItem>> ordersLiveDataSource = new MutableLiveData<>();
    private UserRepository userRepository;
    private OrdersDataSourceInterface dataSourceInterface;
    private String authToken;

    public OrdersDataSourceFactory(UserRepository userRepository, OrdersDataSourceInterface dataSourceInterface, String authToken) {
        this.userRepository = userRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.authToken = authToken;
    }


    @Override
    public DataSource create() {
        OrdersDataSource ordersDataSource = new OrdersDataSource(userRepository, dataSourceInterface, authToken);
        ordersLiveDataSource.postValue(ordersDataSource);
        return ordersDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OrderItem>> getOrdersLiveDataSource() {
        return ordersLiveDataSource;
    }
}
