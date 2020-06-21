package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

public interface RestaurantFragmentInterface {
    void onStarted();
    void onSuccess();
    void onFailure(String error);
    void expandSortView();
    void expandFilterView();
    void openBottomSheet();
    void closeBottomSheet();

    void updateRestaurantsRecyclerView(Object categoryList, Object city, Object restaurantName, Object deliveryFilter,
                                       Object discountFilter, Object servingFilter, Object getInPlaceFilter,
                                       Object latitude, Object longitude, Object sortBy);
}
