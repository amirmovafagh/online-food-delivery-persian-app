package ir.boojanco.onlinefoodorder.ui.fragments.home;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

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
import ir.boojanco.onlinefoodorder.databinding.FragmentMapDialogHomeBinding;
import ir.boojanco.onlinefoodorder.databinding.FragmentMapDialogProfileBinding;
import ir.boojanco.onlinefoodorder.viewmodels.HomeViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.UserProfileViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.MapDialogInterface;

public class MapDialogHomeFragment extends DialogFragment implements OnMapReadyCallback, MapDialogInterface {
    private static final String TAG = MapDialogHomeFragment.class.getSimpleName();
    private FragmentMapDialogHomeBinding binding;
    private GoogleMap googleMap;
    private Marker marker;
    private HomeViewModel homeViewModel;
    @Inject
    MySharedPreferences sharedPreferences;

    public MapDialogHomeFragment(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
    }

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
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_dialog_home, container, false);
        homeViewModel.setMapDialogInterface(this);
        binding.setViewModel(homeViewModel);
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
        if (map != null) {
            this.googleMap = map;

            LatLng restaurantLatLng = new LatLng(sharedPreferences.getLatitude(), sharedPreferences.getLongitud());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLatLng, 15));

            googleMap.setOnMapClickListener(latLng -> {
                Log.i(TAG, "" + latLng);
                if (latLng == null)
                    return;
                if (marker != null)
                    marker.remove();
                marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("رستوران\u200cهای این ناحیه"));
                homeViewModel.setLocationForSearch(latLng.latitude, latLng.longitude);

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
