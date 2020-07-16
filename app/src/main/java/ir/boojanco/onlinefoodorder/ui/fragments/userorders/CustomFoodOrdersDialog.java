package ir.boojanco.onlinefoodorder.ui.fragments.userorders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.UserOrdersFoodCustomDialogLayoutBinding;
import ir.boojanco.onlinefoodorder.models.user.GetUserOrderCommentResponse;
import ir.boojanco.onlinefoodorder.models.user.OrderFoodList;
import ir.boojanco.onlinefoodorder.models.user.OrderItem;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FoodOrdersDialogInterface;

public class CustomFoodOrdersDialog extends Dialog {
    private static final String TAG = CustomFoodOrdersDialog.class.getSimpleName();
    private Activity activity;
    private FoodOrdersDialogInterface dialogInterface;

    private FoodAdapter foodAdapter;
    private RecyclerView foodRecyclerView;
    private List<OrderFoodList> orderFoodLists;
    private OrderItem orderItem;
    private GetUserOrderCommentResponse commentResponse;


    public MutableLiveData<Boolean> acceptButtonStateLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> rateBarStateLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> userCommentStateLiveData = new MutableLiveData<>();
    public MutableLiveData<String> userCommentLiveData = new MutableLiveData<>();
    public MutableLiveData<Float> foodQualityLiveData = new MutableLiveData<>();
    public MutableLiveData<Float> personnelBehaviourLiveData = new MutableLiveData<>();
    public MutableLiveData<Float> arrivalTimeLiveData = new MutableLiveData<>();
    public MutableLiveData<Float> systemExLiveData = new MutableLiveData<>();
    UserOrdersFoodCustomDialogLayoutBinding binding;

    public CustomFoodOrdersDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomFoodOrdersDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomFoodOrdersDialog(Activity activity, int themeResId, FoodOrdersDialogInterface dialogInterface, OrderItem orderItem, GetUserOrderCommentResponse commentResponse) {

        super(activity, themeResId);
        this.activity = activity;
        this.dialogInterface = dialogInterface;
        this.orderItem = orderItem;
        this.orderFoodLists = orderItem.getFoodLists();
        this.commentResponse = commentResponse;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.user_orders_food_custom_dialog_layout, null, false);
        binding.setVariables(this);
        foodRecyclerView = binding.recyclerviewFood;
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        foodAdapter = new FoodAdapter();
        foodRecyclerView.setAdapter(foodAdapter);
        foodAdapter.setFoodLists(orderFoodLists);

        checkCommentState();
        setContentView(binding.getRoot());

    }

    private void checkCommentState() {

        if (commentResponse.getArrivalTime() == 0.0 && commentResponse.getFoodQuality() == 0.0 &&
                commentResponse.getSystemEx() == 0.0 && commentResponse.getPersonnelBehaviour() == 0.0) {
            rateBarStateLiveData.setValue(false); // not indicator and can set the rate
        } else { // is indicator and read old values
            rateBarStateLiveData.setValue(true);
            foodQualityLiveData.setValue(commentResponse.getFoodQuality());
            personnelBehaviourLiveData.setValue(commentResponse.getPersonnelBehaviour());
            arrivalTimeLiveData.setValue(commentResponse.getArrivalTime());
            systemExLiveData.setValue(commentResponse.getSystemEx());
        }

        if (!commentResponse.getContext().equals("")) {//lock the edit text and set old user context
            userCommentStateLiveData.setValue(false);
            userCommentLiveData.setValue(commentResponse.getContext());
        } else {//enable
            userCommentStateLiveData.setValue(true);
        }

        if (!commentResponse.getContext().equals("") && commentResponse.getArrivalTime() != 0.0 && commentResponse.getFoodQuality() != 0.0 &&
                commentResponse.getSystemEx() != 0.0 && commentResponse.getPersonnelBehaviour() != 0.0) {
            acceptButtonStateLiveData.setValue(false); //disable accept btn
        } else acceptButtonStateLiveData.setValue(true); //enable accept btn
    }

    public void commentButtonOnClick() {

        if (orderItem.getId() != 0 && foodQualityLiveData.getValue() != null && systemExLiveData.getValue() != null &&
                arrivalTimeLiveData.getValue() != null && personnelBehaviourLiveData.getValue() != null && userCommentLiveData.getValue() != null) {
            dialogInterface.addComment(orderItem.getId(), foodQualityLiveData.getValue(),
                    systemExLiveData.getValue(), arrivalTimeLiveData.getValue(),
                    personnelBehaviourLiveData.getValue(), userCommentLiveData.getValue());
            this.cancel();
        } else
            dialogInterface.showMessage("لطفا فیلد های موردنظر را تکمیل کنید");
    }

    public void commentButtonCloseOnClick() {
        this.cancel();
    }
}
