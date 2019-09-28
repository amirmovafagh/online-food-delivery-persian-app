package ir.boojanco.onlinefoodorder.networking;

import ir.boojanco.onlinefoodorder.models.user.ChangePasswordResponse;
import ir.boojanco.onlinefoodorder.models.user.EditInfoResponse;
import ir.boojanco.onlinefoodorder.models.user.FillUserInfoResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.RegisterResponse;
import ir.boojanco.onlinefoodorder.models.user.VerifyEmailResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("user/register")
    Call<RegisterResponse> registerUser(@Body RegisterResponse registerResponse);

    @POST("user/fill_info")
    Call<FillUserInfoResponse> fillUserInfo(@Body FillUserInfoResponse fillUserInfoResponse);

    @POST("user/user_login")
    Call<LoginUserResponse> loginUser(@Body LoginUserResponse loginUserResponse);

    @POST("user/verify_Email")
    Call<VerifyEmailResponse> verifyEmail(@Body VerifyEmailResponse verifyEmailResponse);

    @POST("user/edit_info")
    Call<EditInfoResponse> editInfo(@Body EditInfoResponse editInfoResponse);

    @POST("user/change_password")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordResponse changePasswordResponse);
}
