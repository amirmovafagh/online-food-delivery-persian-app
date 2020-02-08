package ir.boojanco.onlinefoodorder.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.FragmentMapDialogBinding;
import ir.boojanco.onlinefoodorder.viewmodels.CartViewModel;

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback {
    private static final String TAG = MapDialogFragment.class.getSimpleName();
    private FragmentMapDialogBinding binding;
    private GoogleMap googleMap;
    private Marker marker;
    private CartViewModel cartViewModel;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_dialog, container,false);
        cartViewModel= new ViewModelProvider(getActivity()).get(CartViewModel.class);

        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.onResume();
        binding.mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if(map != null){
            this.googleMap = map;

            googleMap.setOnMapClickListener(latLng -> {
                Log.i(TAG,""+latLng);
                if(latLng == null)
                    return;
                if(marker != null)
                    marker.remove();
                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("آدرس من"));
                cartViewModel.getReverseAddressParsiMap(latLng.latitude, latLng.longitude);
            });
        }

    }
}
