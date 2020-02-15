package ir.boojanco.onlinefoodorder.ui.activities.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.databinding.ActivityPaymentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.PaymentViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.PaymentViewModelFactory;

public class PaymentActivity extends AppCompatActivity {

    @Inject
    PaymentViewModelFactory factory;

    private ActivityPaymentBinding binding;
    private PaymentViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);
        viewModel = new ViewModelProvider(this, factory).get(PaymentViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }
}
