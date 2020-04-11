package ir.boojanco.onlinefoodorder.data.repositories;

import ir.boojanco.onlinefoodorder.data.networking.ApiInterface;
import ir.boojanco.onlinefoodorder.models.food.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.DiscountCodeResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.GetRestaurantCommentResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantAssessmentResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllCitiesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.models.user.CartOrderResponse;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;

public class RestaurantRepository {
    private ApiInterface apiInterface;

    public RestaurantRepository(Retrofit retrofit) {
        this.apiInterface = retrofit.create(ApiInterface.class);
    }

    public Observable<RestaurantResponse> getLastRestaurant(int page, int size) {
        return apiInterface.getLastRestaurant(page, size);
    }

    public Observable<RestaurantAssessmentResponse> getRestaurantAssessment(long restaurantId) {
        return apiInterface.getRestaurantAssessment(restaurantId);
    }

    public Observable<RestaurantResponse> searchRestaurantObservable(String query, String city, String region, int page, int size) {
        return apiInterface.searchRestaurant(query, city, region, page, size);
    }

    public Observable<RestaurantInfoResponse> getRestaurantInfo(String authToken, long restaurantId) {
        return apiInterface.getRestaurantInfo(authToken, restaurantId);
    }

    public Observable<GetAllFoodResponse> getAllFood(String authToken, long restaurantId) {
        return apiInterface.getAllFood(authToken, restaurantId);
    }

    public Observable<AllPackagesResponse> getAllPackagesResponse(String authToken, long restaurantId) {
        return apiInterface.getAllPackagesResponseObservable(authToken, restaurantId);
    }

    public Observable<GetRestaurantCommentResponse> getRestaurantCommentResponseObservable(String authToken, long restaurantId, int page, int size) {
        return apiInterface.getRestaurantCommentsResponseObservable(authToken, restaurantId, page, size);
    }

    public Observable<DiscountCodeResponse> getDiscountCodeResponse(String authToken, String code, long restaurantId, int totalCost) {
        return apiInterface.getDiscountCodeResponseObservable(authToken, code, restaurantId, totalCost);
    }

    public Observable<Response<Boolean>> addOrder(String authToken, CartOrderResponse cartOrderBody) {
        return apiInterface.addOrder(authToken, cartOrderBody);
    }

    public Observable<Response<Void>> addRestaurantToFavoriteList(String authToken, long restaurantId) {
        return apiInterface.addRestaurantToFavoriteList(authToken, restaurantId);
    }

    public Observable<Response<Void>> addFoodToFavoriteList(String authToken, long foodId) {
        return apiInterface.addFoodToFavoriteList(authToken, foodId);
    }

    public Observable<Response<Void>> removeRestaurantFromFavoriteList(String authToken, long restaurantId) {
        return apiInterface.removeRestaurantFromFavoriteList(authToken, restaurantId);
    }

    public Observable<Response<Void>> removeFoodFromFavoriteList(String authToken, long foodId) {
        return apiInterface.removeFoodFromFavoriteList(authToken, foodId);
    }

    public Observable<GetAllStatesResponse> getAllStatesResponseObservable(String authToken) {
        return apiInterface.getAllStatesResponseObservable(authToken);
    }

    public Observable<GetAllCitiesResponse> getAllCitiesResponseObservable(String authToken, long stateId) {
        return apiInterface.getAllCitiesResponseObservable(authToken, stateId);
    }

}
