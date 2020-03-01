package ir.boojanco.onlinefoodorder.ui.fragments.userorders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.UserOrdersFoodCustomDialogLayoutBinding;
import ir.boojanco.onlinefoodorder.models.user.OrderFoodList;

public class CustomFoodOrdersDialog extends Dialog {
    private Activity activity;

    private FoodAdapter foodAdapter;
    private RecyclerView foodRecyclerView;
    private List<OrderFoodList> orderFoodLists;


    UserOrdersFoodCustomDialogLayoutBinding binding;

    public CustomFoodOrdersDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomFoodOrdersDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomFoodOrdersDialog(Activity activity, List<OrderFoodList> orderFoodLists) {
        super(activity);
        this.activity = activity;
        this.orderFoodLists = orderFoodLists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.user_orders_food_custom_dialog_layout, null, false);

        foodRecyclerView = binding.recyclerviewFood;
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        foodAdapter = new FoodAdapter();
        foodRecyclerView.setAdapter(foodAdapter);
        foodAdapter.setFoodLists(orderFoodLists);

        setContentView(binding.getRoot());

    }

}
