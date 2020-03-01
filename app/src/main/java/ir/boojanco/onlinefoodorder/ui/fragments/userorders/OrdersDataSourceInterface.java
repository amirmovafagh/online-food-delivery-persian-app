package ir.boojanco.onlinefoodorder.ui.fragments.userorders;

public interface OrdersDataSourceInterface {
    void onStartedOrdersData();
    void onSuccessOrdersData();
    void onFailureOrdersData(String error);
}
