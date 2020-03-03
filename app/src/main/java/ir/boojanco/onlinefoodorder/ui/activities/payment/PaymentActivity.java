package ir.boojanco.onlinefoodorder.ui.activities.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.databinding.ActivityPaymentBinding;
import ir.boojanco.onlinefoodorder.ui.activities.cart.FinalPaymentPrice;
import ir.boojanco.onlinefoodorder.util.OrderType;
import ir.boojanco.onlinefoodorder.viewmodels.PaymentViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.PaymentViewModelFactory;

public class PaymentActivity extends AppCompatActivity implements PaymentInterface {

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
    private final String taxAndServiceExtraName = "taxAndService";
    private final String shippingCostExtraName = "shippingCost";
    private final String orderTypeExtraName = "orderType";
    private final String restaurantIdExtraName = "restaurantId";
    private final String restaurantPackageIdExtraName = "restaurantPackageId";
    private final String shippingAddressIdExtraName = "shippingAddressId";

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
//                viewModel.checkDiscountCode(sharedPreferences.getUserAuthTokenKey());
                viewModel.checkOrderAtServerSide();
            }
        });
        viewModel.setUserAuthToken(sharedPreferences.getUserAuthTokenKey());

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            ArrayList<FinalPaymentPrice> finalPaymentPrice = (ArrayList<FinalPaymentPrice>) extras.getSerializable(finalPaymentPricesExtraName);
            viewModel.setVariablesInTempVar(finalPaymentPrice, (List<CartItem>) extras.getSerializable(cartItemExtraName),
                    extras.getInt(totalAllPriceExtraName), extras.getInt(totalRawPriceExtraName), extras.getInt(totalDiscountExtraName),
                    extras.getInt(packingCostLiveDataExtraName), extras.getInt(taxAndServiceExtraName), extras.getInt(shippingCostExtraName),
                    (OrderType)extras.getSerializable(orderTypeExtraName),extras.getLong(restaurantIdExtraName),
                    extras.getLong(restaurantPackageIdExtraName),extras.getLong(shippingAddressIdExtraName));
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
        Toast.makeText(this, "" + Error, Toast.LENGTH_SHORT).show();
    }
}
