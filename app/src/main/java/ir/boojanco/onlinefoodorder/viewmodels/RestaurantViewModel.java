package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;
import android.view.View;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantResponse;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.RestaurantDataSourceInterface;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.SearchRestaurantDataSource;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurants.SearchRestaurantDataSourceFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFragmentInterface;

public class RestaurantViewModel extends ViewModel implements RestaurantDataSourceInterface {
    private static final String TAG = RestaurantViewModel.class.getSimpleName();

    public RestaurantFragmentInterface restaurantInterface;
    private RestaurantRepository restaurantRepository;
    private Context context;
    public MutableLiveData<RestaurantResponse> responseMutableLiveData;
    public MutableLiveData<String> cityNameLiveData;
    public MutableLiveData<String> sortByNameLiveData;
    public MutableLiveData<Boolean> stateWaitingOrNoConnection;
    public LiveData<PagedList<RestaurantList>> restaurantPagedListLiveData;
    public LiveData<PageKeyedDataSource<Integer, RestaurantList>> liveDataSource;

    ArrayList<String> categoryList;
    String restaurantName;
    Boolean deliveryFilter, discountFilter, servingFilter, getInPlaceFilter;
    Double latitude, longitude;
    int sortBy;

    private boolean sortOnClickState = false;

    public void setCategoryList(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setLatLon(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public RestaurantViewModel(Context context, RestaurantRepository restaurantRepository) {
        responseMutableLiveData = new MutableLiveData<>();
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        cityNameLiveData = new MutableLiveData<>();
        sortByNameLiveData = new MutableLiveData<>();
        sortByNameLiveData.setValue("مرتبط ترین\u200cها");

        stateWaitingOrNoConnection = new MutableLiveData<>();
        stateWaitingOrNoConnection.setValue(false); // dont show try again btn textView

    }

    public void getAllSearchedRestaurant(Object categoryList, Object city, Object restaurantName, Object deliveryFilter,
                                         Object discountFilter, Object servingFilter, Object getInPlaceFilter,
                                         Object latitude, Object longitude, Object sortBy) {
        SearchRestaurantDataSourceFactory searchRestaurantDataSourceFactory = new SearchRestaurantDataSourceFactory(
                restaurantRepository, this, categoryList, city, restaurantName, deliveryFilter, discountFilter,
                servingFilter, getInPlaceFilter, latitude, longitude, sortBy);
        liveDataSource = searchRestaurantDataSourceFactory.getSearchRestaurantLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)).setPageSize(SearchRestaurantDataSource.PAGE_SIZE)
                        .build();
        restaurantPagedListLiveData = (new LivePagedListBuilder(searchRestaurantDataSourceFactory, config)).build();
    }

    public void openFilterBottomSheetOnClick() {
        restaurantInterface.openBottomSheet();
    }

    public void closeFilterBottomSheetOnClick() {
        restaurantInterface.closeBottomSheet();
    }

    public void setSortOnClick(View v) {
        switch (v.getId()) {
            case R.id.chip_most_relevant:
                sortByNameLiveData.setValue(context.getString(R.string.most_relevant));
                sortBy = 0;
                restaurantInterface.updateRestaurantsRecyclerView(categoryList, cityNameLiveData.getValue(),
                        restaurantName, deliveryFilter, discountFilter, servingFilter, getInPlaceFilter,
                        latitude, longitude, sortBy);
                return;
            case R.id.chip_more_score:
                sortByNameLiveData.setValue(context.getString(R.string.fave_resturants));
                sortBy = 1;
                restaurantInterface.updateRestaurantsRecyclerView(categoryList, cityNameLiveData.getValue(),
                        restaurantName, deliveryFilter, discountFilter, servingFilter, getInPlaceFilter,
                        latitude, longitude, sortBy);
                return;
            case R.id.chip_newest:
                sortByNameLiveData.setValue(context.getString(R.string.new_restaurants));
                sortBy = 2;
                restaurantInterface.updateRestaurantsRecyclerView(categoryList, cityNameLiveData.getValue(),
                        restaurantName, deliveryFilter, discountFilter, servingFilter, getInPlaceFilter,
                        latitude, longitude, sortBy);
        }

    }

    private void updateRestaurants() {
        restaurantInterface.updateRestaurantsRecyclerView(categoryList, cityNameLiveData.getValue(),
                restaurantName, deliveryFilter, discountFilter, servingFilter, getInPlaceFilter,
                latitude, longitude, sortBy);
    }

    public void setFilterCategoryOnClick(View v, boolean checked) {
        switch (v.getId()) {
            case R.id.chip_delivery_withpeyk:
                deliveryFilter = checked ? true : null;
                updateRestaurants();
                return;
            case R.id.chip_discounted:
                discountFilter = checked ? true : null;
                updateRestaurants();
                return;
            case R.id.chip_serving:
                servingFilter = checked ? true : null;
                updateRestaurants();
                return;
            case R.id.chip_get_inplace:
                getInPlaceFilter = checked ? true : null;
                updateRestaurants();
                return;
            case R.id.chip_fastfood:
                initCategoryList("فست فود", checked);
                return;
            case R.id.chip_burger:
                initCategoryList("برگر", checked);
                return;
            case R.id.chip_pizza:
                initCategoryList("پیتزا", checked);
                return;
            case R.id.chip_sandwich:
                initCategoryList("ساندویچ", checked);
                return;
            case R.id.chip_hotdog:
                initCategoryList("هات داگ", checked);
                return;
            case R.id.chip_fried:
                initCategoryList("سوخاری", checked);
                return;
            case R.id.chip_iranianfood:
                initCategoryList("ایرانی", checked);
                return;
            case R.id.chip_coffeeshop:
                initCategoryList("کافی شاپ", checked);
                return;
            case R.id.chip_chaineesefood:
                initCategoryList("چینی", checked);
                return;
            case R.id.chip_italianfood:
                initCategoryList("ایتالیایی", checked);
                return;
            case R.id.chip_kebab:
                initCategoryList("کباب", checked);
                return;
            case R.id.chip_vegetarianfood:
                initCategoryList("گیاهی", checked);
                return;
            case R.id.chip_seafood:
                initCategoryList("دریایی", checked);
                return;
            case R.id.chip_dessert:
                initCategoryList("دسر", checked);
                return;
        }

    }

    public void searchBtnOnClick() {
        restaurantInterface.updateRestaurantsRecyclerView(categoryList, cityNameLiveData.getValue(),
                restaurantName, deliveryFilter, discountFilter, servingFilter, getInPlaceFilter,
                latitude, longitude, sortBy);

    }

    public void sortBtnOnClick() {
        restaurantInterface.expandSortView();
    }

    public void filterBtnOnClick() {
        restaurantInterface.expandFilterView();
    }

    private void initCategoryList(String name, boolean checked) {
        if (categoryList == null)
            categoryList = new ArrayList<>();
        if (checked)
            categoryList.add(name);
        else for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).equals(name)) {
                categoryList.remove(i);
                if (categoryList.size() == 0) {
                    categoryList = null;
                    break; //use for nullPointer exception
                }

            }
        }
    }

    public void tryAgainOnClick() {
        updateRestaurants();
    }

    @Override
    public void onStarted() {
        restaurantInterface.onStarted();
    }

    @Override
    public void onSuccess() {
        restaurantInterface.onSuccess();
    }

    @Override
    public void onFailure(String error) {
        restaurantInterface.onFailure(error);
        restaurantInterface.tryAgain();
        stateWaitingOrNoConnection.setValue(true); //show tryAgain view
    }
}
