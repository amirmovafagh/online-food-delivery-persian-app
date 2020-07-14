package ir.boojanco.onlinefoodorder.data.repositories;

import androidx.annotation.NonNull;

import ir.boojanco.onlinefoodorder.data.networking.ApiInterface;
import ir.boojanco.onlinefoodorder.models.food.FavoriteFoodsResponse;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurant.FavoriteRestaurantsResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllCitiesResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.models.user.ActivateUserBody;
import ir.boojanco.onlinefoodorder.models.user.AddUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.EditUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.GetUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.GetUserOrderCommentResponse;
import ir.boojanco.onlinefoodorder.models.user.GetUserOrdersResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.RecreatePass;
import ir.boojanco.onlinefoodorder.models.user.UserProfileResponse;
import ir.boojanco.onlinefoodorder.models.user.UserSession;
import ir.boojanco.onlinefoodorder.models.user.VerificationNewUserResponse;
import ir.boojanco.onlinefoodorder.models.user.WalletActivitiesResponse;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;

public class UserRepository {

    private ApiInterface apiInterface;

    public UserRepository(Retrofit retrofit) {
        apiInterface = retrofit.create(ApiInterface.class);

    }

    public Observable<LoginUserResponse> loginUser(@NonNull String phoneNumber, String password) {
        return apiInterface.loginUser(phoneNumber, password);
    }

    public Observable<Long> registerUser(@NonNull String phoneNumber) {
        return apiInterface.registerUser(phoneNumber);
    }

    public Observable<Long> forgotPassword(@NonNull String phoneNumber) {
        return apiInterface.forgotPassword(phoneNumber);
    }

    public Observable<Response<Void>> recreatePass(@NonNull RecreatePass pass) {
        return apiInterface.recreatePass(pass);
    }

    public Observable<UserSession> getUerSession(String authToken) {
        return apiInterface.getUserSession(authToken);
    }

    public Observable<VerificationNewUserResponse> verifyNewUser(String verificationCode, String phoneNumber) {
        return apiInterface.verificationNewUser(verificationCode, phoneNumber);
    }

    public Observable<VerificationNewUserResponse> verifyForgotPass(String verificationCode, String phoneNumber) {
        return apiInterface.verificationForgotPass(verificationCode, phoneNumber);
    }

    public Observable<Response<Void>> activateNewUser(ActivateUserBody activateUserBody) {
        return apiInterface.activateNewUser(activateUserBody);
    }

    public Observable<GetUserAddressResponse> getUserAddressResponseObservable(String authToken, int page, int size) {
        return apiInterface.getUserAddressResponseObservable(authToken, page, size);
    }

    public Observable<Response<Void>> getEditUserAddressResponseObservable(String authToken, long addressId, EditUserAddressResponse editedAddressBody) {
        return apiInterface.editUserAddress(authToken, addressId, editedAddressBody);
    }

    public Observable<Response<Void>> deleteUserAddress(String authToken, long addressId) {
        return apiInterface.deleteUserAddress(authToken, addressId);
    }

    public Observable<Response<Void>> addUserAddressResponseObservable(String authToken, AddUserAddressResponse addUserAddressResponse) {
        return apiInterface.addUserAddress(authToken, addUserAddressResponse);
    }

    public Observable<ReverseFindAddressResponse> getReverseFindAddressResponse(Double latitude, Double longitude) {
        return apiInterface.getReverseAddresse(latitude, longitude);
    }

    public Observable<GetAllStatesResponse> getAllStatesResponseObservable() {
        return apiInterface.getAllStatesResponseObservable();
    }

    public Observable<GetAllCitiesResponse> getAllCitiesResponseObservable(long stateId) {
        return apiInterface.getAllCitiesResponseObservable(stateId);
    }

    public Observable<GetUserOrdersResponse> getUserOrdersResponseObservable(String authToken, int page, int size) {
        return apiInterface.getUserOrdersResponseObservable(authToken, page, size);
    }

    public Observable<GetUserOrderCommentResponse> getUserOrderCommentResponseObservable(String authToken, long orderId) {
        return apiInterface.getUserOrderCommentObservable(authToken, orderId);
    }

    public Observable<UserProfileResponse> getUserProfileResponseObservable(String authToken) {
        return apiInterface.getUserProfileResponseObservable(authToken);
    }

    public Observable<Response<Void>> editUserProfileObservable(String authToken, UserProfileResponse userProfileBody) {
        return apiInterface.editUserProfileObservable(authToken, userProfileBody);
    }

    public Observable<Response<Void>> addUserOrderCommentResponseObservable(String authToken, GetUserOrderCommentResponse userOrderCommentBody) {
        return apiInterface.addUserOrderCommentObservable(authToken, userOrderCommentBody);
    }

    public Observable<FavoriteRestaurantsResponse> getFavoriteRestaurantsResponseObservable(String authToken, int page, int size) {
        return apiInterface.getFavoriteRestaurantsResponseObservable(authToken, page, size);
    }

    public Observable<FavoriteFoodsResponse> getFavoriteFoodsResponseObservable(String authToken, int page, int size) {
        return apiInterface.getFavoriteFoodsResponseObservable(authToken, page, size);
    }

    public Observable<WalletActivitiesResponse> getWalletActivities(String authToken, int page, int size) {
        return apiInterface.getWalletActivities(authToken, page, size);
    }

}
