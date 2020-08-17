package ir.boojanco.onlinefoodorder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Bundle;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.ActivityEnterBinding;
import ir.boojanco.onlinefoodorder.viewmodels.EnterViewModel;


public class EnterActivity extends AppCompatActivity {
    private static String TAG = EnterActivity.class.getSimpleName();
    private EnterViewModel viewModel;
    ActivityEnterBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EnterViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        Navigation.findNavController(this,R.id.enter_nav_host_fragment).navigate(R.id.mazeehLogoFragment);
    }

}