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
import ir.boojanco.onlinefoodorder.models.user.GetUserOrdersResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.VerificationNewUserResponse;
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

    public Observable<VerificationNewUserResponse> verifyNewUser(String verificationCode, String phoneNumber) {
        return apiInterface.verificationNewUser(verificationCode, phoneNumber);
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

    public Observable<Response<Void>> addUserAddressResponseObservable(String authToken, AddUserAddressResponse addUserAddressResponse) {
        return apiInterface.addUserAddress(authToken, addUserAddressResponse);
    }

    public Observable<ReverseFindAddressResponse> getReverseFindAddressResponse(Double latitude, Double longitude) {
        return apiInterface.getReverseAddresse(latitude, longitude);
    }

    public Observable<GetAllStatesResponse> getAllStatesResponseObservable(String authToken) {
        return apiInterface.getAllStatesResponseObservable(authToken);
    }

    public Observable<GetAllCitiesResponse> getAllCitiesResponseObservable(String authToken, long stateId) {
        return apiInterface.getAllCitiesResponseObservable(authToken, stateId);
    }

    public Observable<GetUserOrdersResponse> getUserOrdersResponseObservable(String authToken, int page, int size) {
        return apiInterface.getUserOrdersResponseObservable(authToken, page, size);
    }

    public Observable<FavoriteRestaurantsResponse> getFavoriteRestaurantsResponseObservable(String authToken, int page, int size) {
        return apiInterface.getFavoriteRestaurantsResponseObservable(authToken, page, size);
    }

    public Observable<FavoriteFoodsResponse> getFavoriteFoodsResponseObservable(String authToken, int page, int size) {
        return apiInterface.getFavoriteFoodsResponseObservable(authToken, page, size);
    }

}
