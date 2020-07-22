package ir.boojanco.onlinefoodorder.ui.fragments.qrcode;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.QrCodeScannerFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.QrCodeScannerViewModel;

public class QrCodeScannerFragment extends Fragment{

    private int MY_CAMERA_REQUEST_CODE = 21;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.qrCodeScannerFragment) {
                    Navigation.findNavController(getView()).navigate(R.id.action_qrCodeScannerFragment_to_qrCodeCameraScannerFragment);
                }
            } else {
                Snackbar snackbar = Snackbar.make(binding.mainContent, "مزه به دوربین دسترسی ندارد", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setTextColor(getResources().getColor(R.color.materialGray900))
                        .setBackgroundTint(getResources().getColor(R.color.colorLowGold))
                        .setAction("دادن دسترسی", v -> {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                        });

                snackbar.show();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        binding.buttonScanQr.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED){
                if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.qrCodeScannerFragment) {
                    Navigation.findNavController(getView()).navigate(R.id.action_qrCodeScannerFragment_to_qrCodeCameraScannerFragment);
                }
            }else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
                         // result method in the activityMain
        });


    }
}
