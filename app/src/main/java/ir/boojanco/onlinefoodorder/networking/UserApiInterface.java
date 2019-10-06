package ir.boojanco.onlinefoodorder.networking;

import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.models.user.ChangePasswordResponse;
import ir.boojanco.onlinefoodorder.models.user.EditInfoResponse;
import ir.boojanco.onlinefoodorder.models.user.FillUserInfoResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.RegisterResponse;
import ir.boojanco.onlinefoodorder.models.user.VerifyEmailResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


public interface UserApiInterface {

    @POST("user/register")
    Call<RegisterResponse> registerUser(@Body RegisterResponse registerResponse);
    /**
    * Update the APIService savePost(...) method from Call to become an Observable for using Rx java.
    * */

    /*@POST("user/fill_info")
    Call<FillUserInfoResponse> fillUserInfo(@Body FillUserInfoResponse fillUserInfoResponse);

    @POST("user/user_login")
    Call<LoginUserResponse> loginUser(@Body LoginUserResponse loginUserResponse);

    @POST("user/verify_Email")
    Call<VerifyEmailResponse> verifyEmail(@Body VerifyEmailResponse verifyEmailResponse);

    @POST("user/edit_info")
    Call<EditInfoResponse> editInfo(@Body EditInfoResponse editInfoResponse);

    @POST("user/change_password")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordResponse changePasswordResponse);*/
}
