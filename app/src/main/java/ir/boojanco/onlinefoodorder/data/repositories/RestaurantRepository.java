package ir.boojanco.onlinefoodorder.data.repositories;

import ir.boojanco.onlinefoodorder.data.networking.ApiInterface;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;
import retrofit2.Retrofit;
import rx.Observable;

public class RestaurantRepository {
    private ApiInterface apiInterface;

    public RestaurantRepository(Retrofit retrofit) {
        this.apiInterface = retrofit.create(ApiInterface.class);
    }

    public Observable<LastRestaurantResponse> getLastRestaurant(){
        return apiInterface.getLastRestaurant();
    }
}
