package ir.boojanco.onlinefoodorder.networking;

import ir.boojanco.onlinefoodorder.models.user.AddUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.ChangeUserPasswordResponse;
import ir.boojanco.onlinefoodorder.models.user.EditUserAaddressResponse;
import ir.boojanco.onlinefoodorder.models.user.EditUserInfoResponse;
import ir.boojanco.onlinefoodorder.models.user.FillUserInfoResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;
import ir.boojanco.onlinefoodorder.models.user.VerifyEmailResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface UserApiInterface {

    @POST("user/register")
    //@Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    Observable<RegisterUserResponse> registerUser(@Field("mobile") String phoneNumber/*@Body RegisterUserResponse registerResponse*/);

    /**
     * Update the APIService registerUser(...) method from Call to become an Observable for using Rx java.
     */

    @POST("user/fill_info")
    Call<FillUserInfoResponse> fillUserInfo(@Body FillUserInfoResponse fillUserInfoResponse);

    @POST("user/user_login")
    Call<LoginUserResponse> loginUser(@Field("mobile") String phoneNumber, @Field("password") String password);

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
