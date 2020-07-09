package ir.boojanco.onlinefoodorder.ui.fragments.usertransactions;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.WalletActivity;

public class TransactionsDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, WalletActivity>> transactionsLiveDataSource = new MutableLiveData<>();
    private UserRepository userRepository;
    private TransactionsDataSourceInterface dataSourceInterface;
    private String authToken;

    public TransactionsDataSourceFactory(UserRepository userRepository, TransactionsDataSourceInterface dataSourceInterface, String authToken) {
        this.userRepository = userRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.authToken = authToken;
    }


    @Override
    public DataSource create() {
        TransactionsDataSource transactionsDataSource = new TransactionsDataSource(userRepository, dataSourceInterface, authToken);
        transactionsLiveDataSource.postValue(transactionsDataSource);
        return transactionsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, WalletActivity>> getTransactionsLiveDataSource() {
        return transactionsLiveDataSource;
    }
}
