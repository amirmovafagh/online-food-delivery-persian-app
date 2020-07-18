package ir.boojanco.onlinefoodorder.ui.fragments.qrcode;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import io.reactivex.Observable;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.QrCodeScannerFragmentBinding;
import ir.boojanco.onlinefoodorder.viewmodels.QrCodeScannerViewModel;

public class QrCodeScannerFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener{

    private QrCodeScannerFragmentBinding binding;
    private QrCodeScannerViewModel viewModel;
    private QRCodeReaderView qrCodeReaderView;
    private Button scanAgainButton;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.qr_code_scanner_fragment, container, false);
        viewModel = new ViewModelProvider(this).get(QrCodeScannerViewModel.class);
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
                if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.qrCodeScannerFragment) {
                    Navigation.findNavController(getView()).navigate(R.id.action_qrCodeScannerFragment_to_restaurantDetailsFragment, bundle);
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

    public boolean isNumeric(String str) {
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
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

}
