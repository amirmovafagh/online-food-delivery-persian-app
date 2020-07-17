package ir.boojanco.onlinefoodorder.ui.fragments.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.material.snackbar.Snackbar;

import ir.boojanco.onlinefoodorder.R;

public class QrCodeScannerActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {


    private QRCodeReaderView qrCodeReaderView;
    private Button scanAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();

        scanAgainButton = findViewById(R.id.scan_again);
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
                Toast.makeText(this, "ogeye", Toast.LENGTH_SHORT).show();
                qrCodeReaderView.stopCamera();
            } else {
                Toast.makeText(this, "خطا در بررسی اطلاعات رستوران", Toast.LENGTH_SHORT).show();
                qrCodeReaderView.stopCamera();
            }
        } else {
            Toast.makeText(this, "لطفا از QR های مزه استفاده کنید", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}