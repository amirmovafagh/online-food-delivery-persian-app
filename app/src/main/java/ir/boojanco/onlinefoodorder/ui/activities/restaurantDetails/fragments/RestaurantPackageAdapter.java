package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.RecyclerviewRestaurantPackageItemBinding;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.RestaurantPackageItem;

class RestaurantPackageAdapter extends RecyclerView.Adapter<RestaurantPackageAdapter.PackageViewHolder> {
    List<RestaurantPackageItem> packageItems;

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRestaurantPackageItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_restaurant_package_item, parent, false);
        return new PackageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        holder.binding.setPackageItem(packageItems.get(position));

    }

    @Override
    public int getItemCount() {
        if (packageItems != null) {
            return packageItems.size();
        } else {
            return 0;
        }
    }

    public void setPackageItems(List<RestaurantPackageItem> packageItems) {
        this.packageItems = packageItems;
        notifyDataSetChanged();
    }

    class PackageViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewRestaurantPackageItemBinding binding;
        public PackageViewHolder(@NonNull RecyclerviewRestaurantPackageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
