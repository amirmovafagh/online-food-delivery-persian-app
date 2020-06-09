package ir.boojanco.onlinefoodorder.ui.fragments.restaurants;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantList;

public class SearchRestaurantDataSourceFactory extends DataSource.Factory {
    private MutableLiveData<PageKeyedDataSource<Integer, RestaurantList>> searchRestaurantLiveDataSource = new MutableLiveData<>();
    private RestaurantRepository restaurantRepository;
    private RestaurantDataSourceInterface dataSourceInterface;
    private String restaurantName;
    private String city;
    private ArrayList<String> categoryList;
    private boolean discountFilter;
    private boolean deliveryFilter;
    private boolean servingFilter;
    private boolean getInPlaceFilter;
    private double latitude;
    private double longitude;
    private int sortBy;

    public SearchRestaurantDataSourceFactory(RestaurantRepository restaurantRepository, RestaurantDataSourceInterface dataSourceInterface, ArrayList<String> categoryList, String city, String restaurantName, boolean deliveryFilter,
                                             boolean discountFilter, boolean servingFilter, boolean getInPlaceFilter,
                                             double latitude, double longitude, int sortBy) {
        this.restaurantRepository = restaurantRepository;
        this.dataSourceInterface = dataSourceInterface;
        this.restaurantName = restaurantName;
        this.city = city;
        this.discountFilter = discountFilter;
        this.deliveryFilter = deliveryFilter;
        this.servingFilter = servingFilter;
        this.getInPlaceFilter = getInPlaceFilter;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sortBy = sortBy;
        this.categoryList = categoryList;
    }


    @Override
    public DataSource create() {
        SearchRestaurantDataSource searchRestaurantDataSource = new SearchRestaurantDataSource(restaurantRepository, dataSourceInterface, categoryList, city, restaurantName, deliveryFilter, discountFilter,
                servingFilter, getInPlaceFilter, latitude, longitude, sortBy);
        searchRestaurantLiveDataSource.postValue(searchRestaurantDataSource);
        return searchRestaurantDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, RestaurantList>> getSearchRestaurantLiveDataSource() {
        return searchRestaurantLiveDataSource;
    }
}
