package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewFoodTypeHeaderBinding;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantFoodMenuBinding;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.FoodItem;

public class RestaurantFoodMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //private List<AllFoodList> foodLists;
    private ArrayList<ListItemType> items;
    private FoodTypeHeader header;
    public RecyclerViewRestaurantFoodMenuClickListener clickListener;

    public RestaurantFoodMenuAdapter(RecyclerViewRestaurantFoodMenuClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ListItemType.TYPE_HEADER){
            RecyclerviewFoodTypeHeaderBinding recyclerviewFoodTypeHeaderBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_food_type_header, parent, false);
            return new RestaurantFoodHeaderViewHolder(recyclerviewFoodTypeHeaderBinding);
        }else if(viewType == ListItemType.TYPE_ITEM){
            RecyclerviewRestaurantFoodMenuBinding recyclerviewRestaurantFoodMenuBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_restaurant_food_menu, parent, false);
            return new RestaurantFoodViewHolder(recyclerviewRestaurantFoodMenuBinding);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof RestaurantFoodHeaderViewHolder){
            header = (FoodTypeHeader) items.get(position);
            ((RestaurantFoodHeaderViewHolder) holder).recyclerviewFoodTypeHeaderBinding.setHeader(header);
        }else if(holder instanceof RestaurantFoodViewHolder){
            FoodItem foodItem = (FoodItem) items.get(position);
            ((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.setFoodItem(foodItem);
            ((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.cvFoodDetails.setOnClickListener(v -> {

                clickListener.onRecyclerViewItemClick(position,((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.cvFoodDetails, foodItem);
            });

            if(foodItem.getDiscount()>0){
                TextView textViewCostStrikeThrough  = ((RestaurantFoodViewHolder) holder).recyclerviewRestaurantFoodMenuBinding.textViewPrice;
                textViewCostStrikeThrough.setPaintFlags(textViewCostStrikeThrough.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

    }



    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }


    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }


    public void setFoodLists(ArrayList<ListItemType> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    class RestaurantFoodViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewRestaurantFoodMenuBinding recyclerviewRestaurantFoodMenuBinding; //this variable is from XML recyclerview_restauran_food
        public RestaurantFoodViewHolder(@NonNull RecyclerviewRestaurantFoodMenuBinding recyclerviewRestaurantFoodBinding) {
            super(recyclerviewRestaurantFoodBinding.getRoot());
            this.recyclerviewRestaurantFoodMenuBinding = recyclerviewRestaurantFoodBinding;
        }
    }
    class RestaurantFoodHeaderViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewFoodTypeHeaderBinding recyclerviewFoodTypeHeaderBinding; //this variable is from XML recyclerview_food_type_header
        public RestaurantFoodHeaderViewHolder(@NonNull RecyclerviewFoodTypeHeaderBinding recyclerviewFoodTypeHeaderBinding) {
            super(recyclerviewFoodTypeHeaderBinding.getRoot());
            this.recyclerviewFoodTypeHeaderBinding = recyclerviewFoodTypeHeaderBinding;
        }
    }
}
