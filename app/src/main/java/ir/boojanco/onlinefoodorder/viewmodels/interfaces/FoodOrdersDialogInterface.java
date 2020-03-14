package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

public interface FoodOrdersDialogInterface {
    void addComment(long orderId, float foodQuality, float systemEx, float arrivalTime, float personnelBehaviour, String userComment);
}
