package ir.boojanco.onlinefoodorder.data.networking;

import ir.boojanco.onlinefoodorder.models.food.FavoriteFoodsResponse;
import ir.boojanco.onlinefoodorder.models.food.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.DiscountCodeResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.FavoriteRestaurantsResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.GetRestaurantCommentResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllCitiesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.models.user.ActivateUserBody;
import ir.boojanco.onlinefoodorder.models.user.AddUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.CartOrderResponse;
import ir.boojanco.onlinefoodorder.models.user.EditUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.GetUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.GetUserOrderCommentResponse;
import ir.boojanco.onlinefoodorder.models.user.GetUserOrdersResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.UserProfileResponse;
import ir.boojanco.onlinefoodorder.models.user.VerificationNewUserResponse;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface ApiInterface {

    @POST("api/v1/auth/register")
    //@Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    Observable<Long> registerUser(@Field("mobile") String phoneNumber/*@Body RegisterUserResponse registerResponse*/);

    /**
     * Update the APIService registerUser(...) method from Call to become an Observable for using Rx java.
     */

    @POST("/api/v1/auth/login")
    @FormUrlEncoded
    Observable<LoginUserResponse> loginUser(@Field("mobile") String phoneNumber, @Field("password") String password);

    @POST("/api/v1/auth/verify")
    Observable<VerificationNewUserResponse> verificationNewUser(@Query("code") String verificationCode, @Query("mobile") String phoneNumber);

    @PUT("/api/v1/auth/activate")
    Observable<Response<Void>> activateNewUser(@Body ActivateUserBody activateUserBody);

    /*@Headers("Authorization: 9900a9720d31dfd5fdb4352700c...")*/
    @GET("/api/v1/restaurant/last")
    Observable<RestaurantResponse> getLastRestaurant(/*@Header("Authorization") String authToken,*/ @Query("page") int page, @Query("size") int size);

    @GET("/api/v1/restaurant/search")
    Observable<RestaurantResponse> searchRestaurant(/*@Header("Authorization") String authToken,*/ @Query("query") String query, @Query("city") String city, @Query("region") String region, @Query("page") int page, @Query("size") int size);

    @GET("/api/v1/food/restaurant/{restaurantId}")
    Observable<GetAllFoodResponse> getAllFood(@Header("Authorization") String authToken, @Path("restaurantId") long restaurantId);

    @GET("/api/v1/restaurant/{restaurantId}/info")
    Observable<RestaurantInfoResponse> getRestaurantInfo(@Header("Authorization") String authToken, @Path("restaurantId") long restaurantId);

    @GET("/api/v1/restaurant/{restaurantId}/addToFavoriteList")
    Observable<Response<Void>> addRestaurantToFavoriteList(@Header("Authorization") String authToken, @Path("restaurantId") long restaurantId);

    @GET("/api/v1/food/{foodId}/addToFavoriteList")
    Observable<Response<Void>> addFoodToFavoriteList(@Header("Authorization") String authToken, @Path("foodId") long foodId);

    @GET("/api/v1/restaurant/{restaurantId}/removeFromFavoriteList")
    Observable<Response<Void>> removeRestaurantFromFavoriteList(@Header("Authorization") String authToken, @Path("restaurantId") long restaurantId);

    @GET("/api/v1/food/{foodId}/removeFromFavoriteList")
    Observable<Response<Void>> removeFoodFromFavoriteList(@Header("Authorization") String authToken, @Path("foodId") long foodId);

    @GET("/api/v1/package/{restaurantId}/valid")
    Observable<AllPackagesResponse> getAllPackagesResponseObservable(@Header("Authorization") String authToken, @Path("restaurantId") long restaurantId);

    @GET("/api/v1/discountCode/validate/{code}")
    Observable<DiscountCodeResponse> getDiscountCodeResponseObservable(@Header("Authorization") String authToken, @Path("code") String code, @Query("restaurantId") Long restaurantId, @Query("totalCost") int totalCost);

    @GET("/api/v1/address/user/all")
    Observable<GetUserAddressResponse> getUserAddressResponseObservable(@Header("Authorization") String authToken, @Query("page") int page, @Query("size") int size);

    @POST("/api/v1/address/add")
    Observable<Response<Void>> addUserAddress(@Header("Authorization") String authToken, @Body AddUserAddressResponse addressBody);

    @POST("/api/v1/order/add")
    Observable<Response<Boolean>> addOrder(@Header("Authorization") String authToken, @Body CartOrderResponse cartOrderBody);

    @PUT("/api/v1/address/{addressId}/edit")
    Observable<Response<Void>> editUserAddress(@Header("Authorization") String authToken, @Path("addressId") long addressId, @Body EditUserAddressResponse editedAddressBody);

    @GET("/api/v1/state/state/all")
    Observable<GetAllStatesResponse> getAllStatesResponseObservable(@Header("Authorization") String authToken);

    @GET("/api/v1/city/all/state/{stateId}")
    Observable<GetAllCitiesResponse> getAllCitiesResponseObservable(@Header("Authorization") String authToken, @Path("stateId") long stateId);

    @GET("https://pm1.parsimap.com/comapi.svc/areaInfo/{latitude}/{longitude}/18/1/ccc1a4bc-ade4-460d-b799-82885ab21d6d/1") /*get Reverse Address from LatLng with parsiMap API*/
    Observable<ReverseFindAddressResponse> getReverseAddresse(@Path("latitude") Double latitude, @Path("longitude") Double longitude);

    @GET("/api/v1/comment/restaurant/{restaurantId}/all")
    Observable<GetRestaurantCommentResponse> getRestaurantCommentsResponseObservable(@Header("Authorization") String authToken, @Path("restaurantId") long restaurantId, @Query("page") int page, @Query("size") int size);

    @GET("/api/v1/order/user/getAll/")
    Observable<GetUserOrdersResponse> getUserOrdersResponseObservable(@Header("Authorization") String authToken, @Query("page") int page, @Query("size") int size);

    @GET("/api/v1/comment/order/{orderId}")
    Observable<GetUserOrderCommentResponse> getUserOrderCommentObservable(@Header("Authorization") String authToken, @Path("orderId") long orderId);

    @GET("/api/v1/user/getProfile")
    Observable<UserProfileResponse> getUserProfileResponseObservable(@Header("Authorization") String authToken);

    @POST("/api/v1/comment/add")
    Observable<Response<Void>> addUserOrderCommentObservable(@Header("Authorization") String authToken, @Body GetUserOrderCommentResponse userOrderCommentBody);

    @GET("/api/v1/user/getFaveRestaurants")
    Observable<FavoriteRestaurantsResponse> getFavoriteRestaurantsResponseObservable(@Header("Authorization") String authToken, @Query("page") int page, @Query("size") int size);

    @GET("/api/v1/user/getFaveFoods")
    Observable<FavoriteFoodsResponse> getFavoriteFoodsResponseObservable(@Header("Authorization") String authToken, @Query("page") int page, @Query("size") int size);
}
