package ir.boojanco.onlinefoodorder.data.repositories;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.data.networking.ApiInterface;
import ir.boojanco.onlinefoodorder.models.food.FoodCategoriesResponse;
import ir.boojanco.onlinefoodorder.models.food.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.DiscountCodeResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.GetRestaurantCommentResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.MenuTypesInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantAssessmentResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllCitiesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.models.user.CartOrderResponse;
import ir.boojanco.onlinefoodorder.models.user.UserPointRestaurantClubResponse;
import ir.boojanco.onlinefoodorder.models.user.UserSession;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Query;
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

    public Observable<UserSession> getUerSession(String authToken){
        return apiInterface.getUserSession(authToken);
    }

    public Observable<RestaurantResponse> searchRestaurantObservable(Object categoryList, Object city, Object restaurantName, Object deliveryFilter,
                                                                     Object discountFilter, Object servingFilter, Object getInPlaceFilter,
                                                                     Object latitude, Object longitude, int page, int size, Object sortBy) {
        return apiInterface.searchRestaurants(categoryList, city, restaurantName, deliveryFilter, discountFilter,
                servingFilter, getInPlaceFilter, latitude, longitude, page, size, sortBy);
    }

    public Observable<ReverseFindAddressResponse> getReverseFindAddressResponse(Double latitude, Double longitude) {
        return apiInterface.getReverseAddresse(latitude, longitude);
    }

    public Observable<RestaurantInfoResponse> getRestaurantInfo(String authToken, long restaurantId) {
        return apiInterface.getRestaurantInfo(authToken, restaurantId);
    }

    public Observable<MenuTypesInfoResponse> getMenuTypesInfo(long restaurantId) {
        return apiInterface.getMenuTypesInfo(restaurantId);
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

    public Observable<CartOrderResponse> addOrder(String authToken, CartOrderResponse cartOrderBody) {
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

    public Observable<UserPointRestaurantClubResponse> getUserPointInRestaurantClub(String authToken, long restaurantId) {
        return apiInterface.getUserPointInRestaurantClub(authToken, restaurantId);
    }

    public Observable<FoodCategoriesResponse> getCategoriesResponseObservable() {
        return apiInterface.getCategoriesResponse();
    }

}
