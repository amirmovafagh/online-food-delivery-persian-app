package ir.boojanco.onlinefoodorder.ui.activities.cart;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewStateItemBinding;
import ir.boojanco.onlinefoodorder.models.state.AllStatesList;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {
    List<AllStatesList> statesLists;
    private int selectedPosition = 100000;
    public RecyclerViewCartClickListener clickListener;

    public StateAdapter(RecyclerViewCartClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewStateItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_user_address_item , parent, false);
        return new StateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StateViewHolder holder, int position) {
        AllStatesList currentState = statesLists.get(position);
        holder.binding.setStateItem(currentState);

    }

    @Override
    public int getItemCount() {
        if (statesLists != null) {
            return statesLists.size();
        } else {
            return 0;
        }
    }

    public class StateViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewStateItemBinding binding;
        public StateViewHolder(@NonNull RecyclerviewStateItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
