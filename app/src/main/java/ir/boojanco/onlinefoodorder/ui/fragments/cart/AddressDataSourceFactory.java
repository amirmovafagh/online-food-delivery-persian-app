package ir.boojanco.onlinefoodorder.ui.fragments.cart;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;

public class AddressDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, UserAddressList>> addresstLiveDataSource = new MutableLiveData<>();
    private UserRepository userRepository;
    private AddressDataSourceInterface dataSourceInterface;
    private String authToken;

    public AddressDataSourceFactory(UserRepository userRepository, AddressDataSourceInterface dataSourceInterface, String authToken) {
        this.userRepository = userRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.authToken = authToken;
    }


    @Override
    public DataSource create() {
        AddressDataSource addressDataSource = new AddressDataSource(userRepository, dataSourceInterface, authToken);
        addresstLiveDataSource.postValue(addressDataSource);
        return addressDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, UserAddressList>> getAddresstLiveDataSource() {
        return addresstLiveDataSource;
    }
}
