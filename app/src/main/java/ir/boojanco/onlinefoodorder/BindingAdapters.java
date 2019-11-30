package ir.boojanco.onlinefoodorder;

import androidx.databinding.BindingAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BindingAdapters {
    @BindingAdapter("onNavigationItemSelected")
    public static void setOnNavigationItemSelected(
            BottomNavigationView view, BottomNavigationView.OnNavigationItemSelectedListener listener) {
        view.setOnNavigationItemSelectedListener(listener);
    }

    @BindingAdapter("selectedItemPosition")
    public static void setSelectedItemPosition(
            BottomNavigationView view, int position) {
        view.setSelectedItemId(position);
    }
}