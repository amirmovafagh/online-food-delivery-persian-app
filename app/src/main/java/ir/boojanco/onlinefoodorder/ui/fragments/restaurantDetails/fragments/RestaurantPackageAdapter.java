package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import android.content.Context;
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
    private List<RestaurantPackageItem> packageItems;
    private int selectedPosition = 1000;
    private int userpoint = 0;
    private RestaurantPackageInterface packageInterface;
    private Context context;

    public RestaurantPackageAdapter(RestaurantPackageInterface packageInterface, Context context) {
        this.context = context;
        this.packageInterface = packageInterface;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewRestaurantPackageItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recyclerview_restaurant_package_item, parent, false);
        return new PackageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        RestaurantPackageItem currentPackageItem = packageItems.get(position);
        holder.binding.setPackageItem(currentPackageItem);
        if (selectedPosition == position) {// is selected
            holder.binding.cvMainPackageLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));

        } else {//remove selected
            holder.binding.cvMainPackageLayout.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.binding.cvMainPackageLayout.setOnClickListener(v -> {
            //is select
            if (currentPackageItem.getRequiredPoint() <= userpoint){
                holder.binding.cvMainPackageLayout.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                packageInterface.onPackageItemClick(holder.binding.cvMainPackageLayout, currentPackageItem);
                selectedPosition = position;
                notifyDataSetChanged();
            }else packageInterface.onPackageMessage("امتیاز کلاب شما کافی نیست");
        });
        holder.binding.cvPackageName.setOnClickListener(v -> {
            //is select
            holder.binding.cvMainPackageLayout.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            packageInterface.onPackageItemClick(holder.binding.cvPackageName, currentPackageItem);

        });

    }

    public void setUserClubPoint(int point){
        this.userpoint = point;
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

    class PackageViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewRestaurantPackageItemBinding binding;

        public PackageViewHolder(@NonNull RecyclerviewRestaurantPackageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
