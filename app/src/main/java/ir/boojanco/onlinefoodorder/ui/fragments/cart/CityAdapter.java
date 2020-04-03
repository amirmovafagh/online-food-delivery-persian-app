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
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewCityItemBinding;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
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
        AllCitiesList currentCity = citiesListsFiltered.get(position);
        holder.binding.setCityItem(currentCity);
        if (selectedPosition == position) {// is selected
            holder.binding.cvCityTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.binding.textViewCity.setTextColor(context.getResources().getColor(R.color.white));
        } else {//remove selected
            holder.binding.cvCityTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.binding.textViewCity.setTextColor(context.getResources().getColor(R.color.gray));
        }
        holder.binding.linearLayoutCity.setOnClickListener(v -> {
            dialogInterface.onCityItemClick(currentCity);
            //is select
            holder.binding.cvCityTextBackground.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.binding.textViewCity.setTextColor(context.getResources().getColor(R.color.white));

            selectedPosition = position;
            notifyDataSetChanged();
        });

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
