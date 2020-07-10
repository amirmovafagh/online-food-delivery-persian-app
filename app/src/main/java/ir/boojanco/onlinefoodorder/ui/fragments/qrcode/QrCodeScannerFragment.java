package ir.boojanco.onlinefoodorder.ui.fragments.qrcode;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.QrCodeScannerFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.QrCodeScannerViewModel;

public class QrCodeScannerFragment extends Fragment {

    private QrCodeScannerFragmentBinding binding;
    private QrCodeScannerViewModel viewModel;


    public static QrCodeScannerFragment newInstance() {
        return new QrCodeScannerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.qr_code_scanner_fragment, container, false);
        viewModel = new ViewModelProvider(this).get(QrCodeScannerViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        IntentIntegrator qrScan = new IntentIntegrator(getActivity());
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        qrScan.setPrompt("لطفا QR کد را اسکن کنید");
        qrScan.setOrientationLocked(false);
        qrScan.setBarcodeImageEnabled(true);
        qrScan.setBeepEnabled(true);

        binding.buttonScanQr.setOnClickListener(v -> {
            //initiating the qr code scan
            qrScan.initiateScan();
            // result method in the activityMain
        });

    }


}
