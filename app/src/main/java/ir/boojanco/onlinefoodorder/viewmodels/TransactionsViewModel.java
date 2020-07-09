package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.WalletActivity;
import ir.boojanco.onlinefoodorder.ui.fragments.usertransactions.TransactionsDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.usertransactions.TransactionsDataSourceFactory;
import ir.boojanco.onlinefoodorder.ui.fragments.usertransactions.TransactionsDataSourceInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.TransactionsFragmentInterface;

public class TransactionsViewModel extends ViewModel implements TransactionsDataSourceInterface {
    private static final String TAG = TransactionsViewModel.class.getCanonicalName();
    private Context context;
    private UserRepository userRepository;
    public TransactionsFragmentInterface fragmentInterface;
    private String userAuthToken;

    public LiveData<PagedList<WalletActivity>> transactionsPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, WalletActivity>> transactionsPageKeyedDataSourceLiveData;

    public TransactionsViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public void getUserWalletActivities(String authToken) {
        this.userAuthToken = authToken;
        TransactionsDataSourceFactory transactionsDataSourceFactory = new TransactionsDataSourceFactory(userRepository, this, authToken);
        transactionsPageKeyedDataSourceLiveData = transactionsDataSourceFactory.getTransactionsLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(TransactionsDataSource.PAGE_SIZE)
                        .build();
        transactionsPagedListLiveData = (new LivePagedListBuilder(transactionsDataSourceFactory, config)).build();
    }

    @Override
    public void onStartedTransactionsData() {
        fragmentInterface.onStarted();
    }

    @Override
    public void onSuccessTransactionsData() {
        fragmentInterface.onSuccess();
    }

    @Override
    public void onFailureTransactionsData(String error) {
        fragmentInterface.onFailure(error);
    }
}
