package ir.boojanco.onlinefoodorder.ui.fragments.cart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.StateCityCustomDialogLayoutBinding;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogAnimateInterface;

public class CustomStateCityDialog extends Dialog implements StateCityDialogAnimateInterface {
    private Activity activity;
    private StateAdapter stateAdapter;
    CityAdapter cityAdapter;
    private RecyclerView stateRecyclerView, cityRecyclerView;
    private TextView tvSearch;
    private List<AllStatesList> statesLists;
    private AutoTransition transition;
    private ConstraintLayout mainConstreaintLayout;
    private CardView expandableLayoutCityCardView;

    StateCityCustomDialogLayoutBinding binding;

    public CustomStateCityDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomStateCityDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomStateCityDialog(Activity activity, StateAdapter stateAdapter, List<AllStatesList> statesLists, CityAdapter cityAdapter ) {
        super(activity);
        this.activity = activity;
        this.stateAdapter = stateAdapter;
        this.cityAdapter = cityAdapter;
        this.statesLists = statesLists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.state_city_custom_dialog_layout, null, false);

        transition = new AutoTransition();
        mainConstreaintLayout = binding.consLayoutMainStateCityDialog;
        expandableLayoutCityCardView = binding.cvCityItem;

        tvSearch = binding.editTextSearchStateFilter;
        stateRecyclerView = binding.recyclerviewStateItem;
        stateRecyclerView.setLayoutManager( new LinearLayoutManager(activity));
        stateRecyclerView.setAdapter(stateAdapter);
        stateAdapter.setStatesLists(statesLists, this);

        cityRecyclerView = binding.recyclerviewCityItem;
        cityRecyclerView.setLayoutManager( new LinearLayoutManager(activity));
        cityRecyclerView.setAdapter(cityAdapter);



        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                stateAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setContentView(binding.getRoot());

    }

    @Override
    public void onStateItemClickAnimate() {
        if (expandableLayoutCityCardView.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(mainConstreaintLayout, transition);
            expandableLayoutCityCardView.setVisibility(View.VISIBLE);

        }
    }



}
