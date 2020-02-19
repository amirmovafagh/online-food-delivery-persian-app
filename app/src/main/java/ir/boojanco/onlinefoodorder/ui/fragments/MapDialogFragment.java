package ir.boojanco.onlinefoodorder.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.FragmentMapDialogBinding;
import ir.boojanco.onlinefoodorder.viewmodels.CartViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.MapDialogInterface;

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback, MapDialogInterface {
    private static final String TAG = MapDialogFragment.class.getSimpleName();
    private FragmentMapDialogBinding binding;
    private GoogleMap googleMap;
    private Marker marker;
    private CartViewModel cartViewModel;

    @Inject
    MySharedPreferences sharedPreferences;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getComponent().inject(this);

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.popup_background);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_dialog, container,false);
        cartViewModel= new ViewModelProvider(getActivity()).get(CartViewModel.class);
        cartViewModel.mapDialogInterface = this;
        binding.setViewModel(cartViewModel);
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

            LatLng restaurantLatLng = new LatLng(sharedPreferences.getLatitude(), sharedPreferences.getLongitud());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLatLng,15));

            googleMap.setOnMapClickListener(latLng -> {
                Log.i(TAG,""+latLng);
                if(latLng == null)
                    return;
                if(marker != null)
                    marker.remove();
                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("آدرس من"));
                cartViewModel.getReverseAddressParsiMap(latLng.latitude, latLng.longitude, sharedPreferences.getUserAuthTokenKey());

            });
        }

    }


    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess() {
        binding.btnChooseMapLatlong.setEnabled(true);
    }

    @Override
    public void onFailure(String Error) {
        binding.btnChooseMapLatlong.setEnabled(false);
    }
}
