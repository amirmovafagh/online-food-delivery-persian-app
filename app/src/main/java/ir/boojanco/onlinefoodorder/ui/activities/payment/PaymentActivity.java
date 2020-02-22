package ir.boojanco.onlinefoodorder.ui.activities.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityPaymentBinding;
import ir.boojanco.onlinefoodorder.ui.activities.cart.FinalPaymentPrice;
import ir.boojanco.onlinefoodorder.viewmodels.PaymentViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.PaymentViewModelFactory;

public class PaymentActivity extends AppCompatActivity implements PaymentInterface  {

    @Inject
    PaymentViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    private ActivityPaymentBinding binding;
    private PaymentViewModel viewModel;

    private final String cartItemExtraName = "cartItem";
    private final String finalPaymentPricesExtraName = "finalPaymentPrices";
    private final String totalAllPriceExtraName = "totalAllPrice";
    private final String totalRawPriceExtraName = "totalRaw";
    private final String totalDiscountExtraName = "totalDiscount";
    private final String packingCostLiveDataExtraName = "packingCost";
    private final String restaurantShippingCostExtraName = "restaurantShipping";
    private final String taxAndServiceExtraName = "taxAndService";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);
        viewModel = new ViewModelProvider(this, factory).get(PaymentViewModel.class);
        viewModel.paymentInterface = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.linearLayoutAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.checkDiscountCode(sharedPreferences.getUserAuthTokenKey());
            }
        });
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            extras.getSerializable(cartItemExtraName);

            viewModel.totalAllPriceLiveData.setValue(extras.getString(totalAllPriceExtraName));
            ArrayList<FinalPaymentPrice> finalPaymentPrice = (ArrayList<FinalPaymentPrice>) extras.getSerializable(finalPaymentPricesExtraName);
            if(finalPaymentPrice != null)
                viewModel.finalPaymentPrices = finalPaymentPrice;
            viewModel.totalRawPriceLiveData.setValue(extras.getLong(totalRawPriceExtraName));
            viewModel.totalDiscountLiveData.setValue(extras.getString(totalDiscountExtraName));
            viewModel.packingCostLiveData.setValue(extras.getInt(packingCostLiveDataExtraName));
            viewModel.restaurantShippingCostLiveData.setValue(extras.getString(restaurantShippingCostExtraName));
            viewModel.taxAndServiceLivedata.setValue(extras.getInt(taxAndServiceExtraName));
        }
    }

    @Override
    public void onStarted() {
        Toast.makeText(this, "st", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String Error) {
        Toast.makeText(this, ""+Error, Toast.LENGTH_SHORT).show();
    }
}
