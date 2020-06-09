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
    private Object restaurantName;
    private Object city;
    private Object categoryList;
    private Object discountFilter;
    private Object deliveryFilter;
    private Object servingFilter;
    private Object getInPlaceFilter;
    private Object latitude;
    private Object longitude;
    private Object sortBy;

    public SearchRestaurantDataSourceFactory(RestaurantRepository restaurantRepository, RestaurantDataSourceInterface dataSourceInterface, Object categoryList, Object city, Object restaurantName, Object deliveryFilter,
                                             Object discountFilter, Object servingFilter, Object getInPlaceFilter,
                                             Object latitude, Object longitude, Object sortBy) {
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
