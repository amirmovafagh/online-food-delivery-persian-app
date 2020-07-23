package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantMenuTypeInfoItemBinding;
import ir.boojanco.onlinefoodorder.models.restaurant.MenuType;

class RestaurantMenuTypeInfoAdapter extends RecyclerView.Adapter<RestaurantMenuTypeInfoAdapter.MenuTypeInfoViewHolder> {
    private List<MenuType> menuItems;

    @NonNull
    @Override
    public MenuTypeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRestaurantMenuTypeInfoItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_restaurant_menu_type_info_item, parent, false);
        return new MenuTypeInfoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuTypeInfoViewHolder holder, int position) {
        MenuType currentMenuItem = menuItems.get(position);
        holder.binding.setItem(currentMenuItem);

    }


    @Override
    public int getItemCount() {
        if (menuItems != null) {
            return menuItems.size();
        } else {
            return 0;
        }
    }

    public void setMenuItems(List<MenuType> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
    }

    class MenuTypeInfoViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewRestaurantMenuTypeInfoItemBinding binding;

        public MenuTypeInfoViewHolder(@NonNull RecyclerviewRestaurantMenuTypeInfoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
