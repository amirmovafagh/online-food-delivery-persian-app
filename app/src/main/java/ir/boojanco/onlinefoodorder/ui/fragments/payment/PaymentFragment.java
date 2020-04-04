package ir.boojanco.onlinefoodorder.ui.fragments.payment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.databinding.FragmentPaymentBinding;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.FinalPaymentPrice;
import ir.boojanco.onlinefoodorder.util.OrderType;
import ir.boojanco.onlinefoodorder.viewmodels.PaymentViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.PaymentViewModelFactory;

public class PaymentFragment extends Fragment implements PaymentInterface {
    private static final String TAG = PaymentFragment.class.getSimpleName();
    @Inject
    PaymentViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;

    FragmentPaymentBinding binding;
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
    private final String restaurantIdExtraName = "restaurantID";
    private final String restaurantPackageIdExtraName = "restaurantPackageId";
    private final String shippingAddressIdExtraName = "shippingAddressId";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false);
        viewModel = new ViewModelProvider(this, factory).get(PaymentViewModel.class);
        viewModel.setPaymentInterface(this);
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

        Bundle extras = getArguments();
        if (extras != null) {
            ArrayList<FinalPaymentPrice> finalPaymentPrice = extras.getParcelableArrayList(finalPaymentPricesExtraName);
            List<CartItem> cartItems = (List<CartItem>) extras.getSerializable(cartItemExtraName);
            viewModel.setVariablesInTempVar(finalPaymentPrice, cartItems,
                    extras.getInt(totalAllPriceExtraName), extras.getInt(totalRawPriceExtraName), extras.getInt(totalDiscountExtraName),
                    extras.getInt(packingCostLiveDataExtraName), extras.getInt(taxAndServiceExtraName), extras.getInt(shippingCostExtraName),
                    (OrderType) extras.getSerializable(orderTypeExtraName), extras.getLong(restaurantIdExtraName),
                    extras.getLong(restaurantPackageIdExtraName), extras.getLong(shippingAddressIdExtraName));
        }

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String Error) {
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + Error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
