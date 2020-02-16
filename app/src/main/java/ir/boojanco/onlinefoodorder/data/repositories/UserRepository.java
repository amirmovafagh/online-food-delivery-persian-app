package ir.boojanco.onlinefoodorder.data.repositories;

import androidx.annotation.NonNull;

import ir.boojanco.onlinefoodorder.data.networking.ApiInterface;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
import ir.boojanco.onlinefoodorder.models.state.GetAllStatesResponse;
import ir.boojanco.onlinefoodorder.models.user.GetUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
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

    public Observable<GetUserAddressResponse> getUserAddressResponseObservable(String authToken){
        return apiInterface.getUserAddressResponseObservable(authToken);
    }
    public Observable<ReverseFindAddressResponse> getReverseFindAddressResponse(Double latitude, Double longitude){
        return apiInterface.getReverseAddresse(latitude, longitude);
    }

    public Observable<GetAllStatesResponse> getAllStatesResponseObservable(String authToken){
        return apiInterface.getAllStatesResponseObservable(authToken);
    }

}
