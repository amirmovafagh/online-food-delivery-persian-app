package ir.boojanco.onlinefoodorder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavHostController;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;


import com.google.android.material.navigation.NavigationView;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.ActivityEnterBinding;
import ir.boojanco.onlinefoodorder.viewmodels.EnterViewModel;


public class EnterActivity extends AppCompatActivity {
    private static String TAG = EnterActivity.class.getSimpleName();
    private EnterViewModel viewModel;
    ActivityEnterBinding binding;
    NavHostFragment hostFragment;


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