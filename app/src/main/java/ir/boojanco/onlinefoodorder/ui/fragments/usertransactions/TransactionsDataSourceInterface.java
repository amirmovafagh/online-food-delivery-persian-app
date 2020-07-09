package ir.boojanco.onlinefoodorder.ui.fragments.usertransactions;

public interface TransactionsDataSourceInterface {
    void onStartedTransactionsData();
    void onSuccessTransactionsData();
    void onFailureTransactionsData(String error);
}
