package ir.boojanco.onlinefoodorder.data.repositories;

import ir.boojanco.onlinefoodorder.data.networking.ApiInterface;
import ir.boojanco.onlinefoodorder.models.food.getAllFood.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
import retrofit2.Retrofit;
import rx.Observable;

public class RestaurantRepository {
    private ApiInterface apiInterface;

    public RestaurantRepository(Retrofit retrofit) {
        this.apiInterface = retrofit.create(ApiInterface.class);
    }

    public Observable<LastRestaurantResponse> getLastRestaurant(String authToken) {
        return apiInterface.getLastRestaurant(authToken);
    }

    public Observable<RestaurantInfoResponse> getRestaurantInfo(String authToken, long restaurantId) {
        return apiInterface.getRestaurantInfo(authToken, restaurantId);
    }

    public Observable<GetAllFoodResponse> getAllFood(String authToken, long restaurantId) {
        return apiInterface.getAllFood(authToken, restaurantId);
    }

    public Observable<AllPackagesResponse> getAllPackagesResponse(String authToken, long restaurantId) {
        return apiInterface.getAllPackagesResponseObsercable(authToken, restaurantId);
    }
}
