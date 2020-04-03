package ir.boojanco.onlinefoodorder.ui.fragments.cart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewStateItemBinding;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogAnimateInterface;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogInterface;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {
    private String TAG = StateAdapter.class.getSimpleName();
    List<AllStatesList> statesLists;
    List<AllStatesList> statesListsFiltered;

    private int selectedPosition = 100000;
    private StateCityDialogInterface dialogInterface;
    private StateCityDialogAnimateInterface dialogAnimateInterface;
    private Context context;

    public StateAdapter( StateCityDialogInterface dialogInterface, Context context) {
        this.dialogInterface = dialogInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewStateItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_state_item , parent, false);
        return new StateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StateViewHolder holder, int position) {
        AllStatesList currentState = statesListsFiltered.get(position);
        holder.binding.setStateItem(currentState);
        if (selectedPosition == position) {// is selected
            holder.binding.cvStateTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.binding.textViewState.setTextColor(context.getResources().getColor(R.color.white));
        } else {//remove selected
            holder.binding.cvStateTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.binding.textViewState.setTextColor(context.getResources().getColor(R.color.gray));
        }
        holder.binding.linearLayoutState.setOnClickListener(v -> {
            dialogInterface.onStateItemClick(currentState);
            dialogAnimateInterface.onStateItemClickAnimate();
            //is select
            holder.binding.cvStateTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.binding.textViewState.setTextColor(context.getResources().getColor(R.color.white));

            selectedPosition = position;
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        if (statesListsFiltered != null) {
            return statesListsFiltered.size();
        } else {
            return 0;
        }
    }
    public void setStatesLists(List<AllStatesList> statesLists, StateCityDialogAnimateInterface animateInterface) {
        this.dialogAnimateInterface = animateInterface;
        this.statesLists = statesLists;
        this.statesListsFiltered = statesLists;
        notifyDataSetChanged();
    }

    public Filter getFilter (){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                if (charString.isEmpty()){
                    Log.i(TAG,"empty");
                    statesListsFiltered = statesLists;
                }else {
                    List<AllStatesList> filteredList = new ArrayList<>();
                    for (AllStatesList item : statesLists){

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for state name
                        if (item.getName().toLowerCase().contains((charString.toLowerCase()))){

                            filteredList.add(item);
                        }
                    }
                    statesListsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = statesListsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                statesListsFiltered = (List<AllStatesList>)  results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class StateViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewStateItemBinding binding;
        public StateViewHolder(@NonNull RecyclerviewStateItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
