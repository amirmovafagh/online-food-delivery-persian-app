package ir.boojanco.onlinefoodorder.ui.fragments.qrcode;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.QrCodeCameraScannerFragmentBinding;
import ir.boojanco.onlinefoodorder.databinding.QrCodeScannerFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.QrCodeCameraScannerViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.QrCodeScannerViewModel;

public class QrCodeCameraScannerFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener{

    private QrCodeCameraScannerFragmentBinding binding;
    private QrCodeCameraScannerViewModel viewModel;
    private QRCodeReaderView qrCodeReaderView;
    private Button scanAgainButton;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.qr_code_camera_scanner_fragment, container, false);
        viewModel = new ViewModelProvider(this).get(QrCodeCameraScannerViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        qrCodeReaderView = binding.qrdecoderview;
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();

        scanAgainButton = binding.scanAgain;

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        scanAgainButton.setOnClickListener(v -> {
            qrCodeReaderView.startCamera();
            scanAgainButton.setVisibility(View.GONE);
        });

    }


    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (text.contains("mazee")) {
            String[] str = text.split("/");
            //4 index is restaurant id
            if (isNumeric(str[4])) {
                qrCodeReaderView.stopCamera();
                Bundle bundle = new Bundle();
                bundle.putLong("restaurantID", Long.parseLong(str[4]));
                if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.qrCodeCameraScannerFragment) {
                    Navigation.findNavController(getView()).navigate(R.id.action_qrCodeCameraScannerFragment_to_restaurantDetailsFragment, bundle);
                }
            } else {
                Toast.makeText(getActivity(), "خطا در بررسی اطلاعات رستوران", Toast.LENGTH_SHORT).show();
                qrCodeReaderView.stopCamera();
            }
        } else {
            Toast.makeText(getActivity(), "لطفا از QR های مزه استفاده کنید", Toast.LENGTH_SHORT).show();
            qrCodeReaderView.stopCamera();
        }
        if (scanAgainButton.getVisibility() == View.GONE) {
            scanAgainButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        binding.scanningAnimate.setVisibility(View.VISIBLE);
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.scanningAnimate.setVisibility(View.INVISIBLE);
        qrCodeReaderView.stopCamera();
    }

}
