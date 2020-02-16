package ir.boojanco.onlinefoodorder.ui.activities.cart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.StateCityCustomDialogLayoutBinding;
import ir.boojanco.onlinefoodorder.models.state.AllStatesList;

public class CustomStateCityDialog extends Dialog {
    private Activity activity;
    private StateAdapter adapter;
    private RecyclerView recyclerView;
    private List<AllStatesList> statesLists;
    StateCityCustomDialogLayoutBinding binding;

    public CustomStateCityDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomStateCityDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomStateCityDialog(Activity activity, StateAdapter adapter, List<AllStatesList> statesLists) {
        super(activity);
        this.activity = activity;
        this.adapter = adapter;
        this.statesLists = statesLists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getOwnerActivity()), R.layout.state_city_custom_dialog_layout, null, false);
        Toast.makeText(activity, ""+statesLists.get(1).getName(), Toast.LENGTH_SHORT).show();
        recyclerView = binding.recyclerviewStateItem;
        recyclerView.setLayoutManager( new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
        adapter.setStatesLists(statesLists);
        setContentView(binding.getRoot());

    }
}
