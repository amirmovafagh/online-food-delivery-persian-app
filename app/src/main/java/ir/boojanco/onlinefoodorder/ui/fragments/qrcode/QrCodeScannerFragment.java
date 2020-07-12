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
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.QrCodeScannerFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.QrCodeScannerViewModel;

public class QrCodeScannerFragment extends Fragment {

    private QrCodeScannerFragmentBinding binding;
    private QrCodeScannerViewModel viewModel;


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

        binding.buttonScanQr.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),QrCodeScannerActivity.class));
        });

    }


}
