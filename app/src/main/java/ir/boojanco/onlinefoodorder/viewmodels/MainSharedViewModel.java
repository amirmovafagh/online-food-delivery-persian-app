package ir.boojanco.onlinefoodorder.viewmodels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainSharedViewModel extends ViewModel {
    private static final String TAG = MainSharedViewModel.class.getSimpleName();

    public MutableLiveData<String> cityNameLiveData;

    public MainSharedViewModel() {
        cityNameLiveData = new MutableLiveData<>();
    }
}
