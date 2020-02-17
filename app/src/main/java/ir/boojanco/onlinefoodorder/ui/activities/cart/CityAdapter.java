package ir.boojanco.onlinefoodorder.ui.activities.cart;

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
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewCityItemBinding;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewStateItemBinding;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.StateCityDialogInterface;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private String TAG = CityAdapter.class.getSimpleName();
    List<AllCitiesList> citiesLists;
    List<AllCitiesList> citiesListsFiltered;

    private int selectedPosition = 100000;
    private StateCityDialogInterface dialogInterface;
    private Context context;

    public CityAdapter(StateCityDialogInterface dialogInterface, Context context) {
        this.dialogInterface = dialogInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewCityItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_city_item , parent, false);
        return new CityViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        AllCitiesList currentState = citiesListsFiltered.get(position);
        holder.binding.setCityItem(currentState);
        /*if (selectedPosition == position) {// is selected
            holder.binding.cvStateTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.binding.textViewState.setTextColor(context.getResources().getColor(R.color.white));
        } else {//remove selected
            holder.binding.cvStateTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.binding.textViewState.setTextColor(context.getResources().getColor(R.color.gray));
        }
        holder.binding.linearLayoutState.setOnClickListener(v -> {
            dialogInterface.onStateItemClick(currentState);
            //is select
            holder.binding.cvStateTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.binding.textViewState.setTextColor(context.getResources().getColor(R.color.white));

            selectedPosition = position;
            notifyDataSetChanged();
        });*/

    }

    @Override
    public int getItemCount() {
        if (citiesListsFiltered != null) {
            return citiesListsFiltered.size();
        } else {
            return 0;
        }
    }
    public void setCitiesLists(List<AllCitiesList> citiesLists) {
        this.citiesLists = citiesLists;
        this.citiesListsFiltered = citiesLists;
        notifyDataSetChanged();
    }

    public Filter getFilter (){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                if (charString.isEmpty()){
                    Log.i(TAG,"empty");
                    citiesListsFiltered = citiesLists;
                }else {
                    List<AllCitiesList> filteredList = new ArrayList<>();
                    for (AllCitiesList item : citiesLists){

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for state name
                        if (item.getName().toLowerCase().contains((charString.toLowerCase()))){

                            filteredList.add(item);
                        }
                    }
                    citiesListsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = citiesListsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                citiesListsFiltered = (List<AllCitiesList>)  results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class CityViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewCityItemBinding binding;
        public CityViewHolder(@NonNull RecyclerviewCityItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
