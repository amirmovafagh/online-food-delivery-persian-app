package ir.boojanco.onlinefoodorder.data.networking;

import ir.boojanco.onlinefoodorder.models.food.getAllFood.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.DiscountCodeResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
import ir.boojanco.onlinefoodorder.models.state.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.models.user.AddUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.ChangeUserPasswordResponse;
import ir.boojanco.onlinefoodorder.models.user.EditUserAaddressResponse;
import ir.boojanco.onlinefoodorder.models.user.EditUserInfoResponse;
import ir.boojanco.onlinefoodorder.models.user.GetUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.VerifyEmailResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    /*@Headers("Authorization: 9900a9720d31dfd5fdb4352700c...")*/
    @GET("/api/v1/restaurant/last")
    Observable<LastRestaurantResponse> getLastRestaurant(@Header("Authorization") String authToken);

    @GET("/api/v1/food/restaurant/{restaurantId}")
    Observable<GetAllFoodResponse> getAllFood(@Header("Authorization") String authToken, @Path("restaurantId") long restaurantId);

    @GET("/api/v1/restaurant/{restaurantId}/info")
    Observable<RestaurantInfoResponse> getRestaurantInfo(@Header("Authorization") String authToken, @Path("restaurantId") long restaurantId);

    @GET("/api/v1/package/{restaurantId}/valid")
    Observable<AllPackagesResponse> getAllPackagesResponseObservable(@Header("Authorization") String authToken, @Path("restaurantId") long restaurantId) ;

    @GET("/api/v1/discountCode/validate/{code}")
    Observable<DiscountCodeResponse> getDiscountCodeResponseObservable(@Header("Authorization") String authToken, @Path("code") String code) ;

    @GET("/api/v1/address/user/all")
    Observable<GetUserAddressResponse> getUserAddressResponseObservable(@Header("Authorization") String authToken) ;

    @GET("/api/v1/state/state/all")
    Observable<GetAllStatesResponse> getAllStatesResponseObservable(@Header("Authorization") String authToken) ;

    @GET("https://pm1.parsimap.com/comapi.svc/areaInfo/{latitude}/{longitude}/18/1/ccc1a4bc-ade4-460d-b799-82885ab21d6d/1") /*get Reverse Address from LatLng with parsiMap API*/
    Observable<ReverseFindAddressResponse> getReverseAddresse(@Path("latitude") Double latitude, @Path("longitude") Double longitude );

    @POST("user/verify_Email")
    Call<VerifyEmailResponse> verifyEmail(@Body VerifyEmailResponse verifyEmailResponse);

    @POST("user/edit_info")
    Call<EditUserInfoResponse> editInfo(@Body EditUserInfoResponse editUserInfoResponse);

    @FormUrlEncoded
    @POST("user/change_password")
    Observable<ChangeUserPasswordResponse> changeUserPassword(@Field("id")long id ,@Field("newPassword")String newPassword ,@Field("currentPassword") String currentPassword);

    @FormUrlEncoded
    @POST("/user/add_address")
    Observable<AddUserAddressResponse> addUserAddress(@Field("id") long id, @Field("address") String address, @Field("zipCode") String zipCode, @Field("region_id") long regionId);

    @FormUrlEncoded
    @POST("edit_address")
    Observable<EditUserAaddressResponse> editUserAddress(@Field("id") long id, @Field("address") String address, @Field("zipCode") String zipCode, @Field("region_id") long regionId,@Field("address_index")int addressIndex);
}
