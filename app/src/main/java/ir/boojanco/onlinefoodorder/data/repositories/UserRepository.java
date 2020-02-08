package ir.boojanco.onlinefoodorder.data.repositories;

import androidx.annotation.NonNull;

import ir.boojanco.onlinefoodorder.data.networking.ApiInterface;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
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
        return apiInterface.getUserAddressResponseObsercable(authToken);
    }
    public Observable<ReverseFindAddressResponse> getReverseFindAddressResponse(Double latitude, Double longitude){
        return apiInterface.getReverseAddresse(latitude, longitude);
    }

/*
    public MutableLiveData<AddUserAddressResponse> addUserAddress(long id, String address, String zipCode, long regionId) {

        apiInterface.addUserAddress(id, address, zipCode, regionId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddUserAddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AddUserAddressResponse addUserAddressResponse) {
                        addAddressData.setValue(addUserAddressResponse);
                    }
                });
        return addAddressData;
    }
*/

/*
    public MutableLiveData<ChangeUserPasswordResponse> changeUserPassword(long id ,String newPassword ,String currentPassword){
        apiInterface.changeUserPassword(id, newPassword, currentPassword).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangeUserPasswordResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ChangeUserPasswordResponse changeUserPasswordResponse) {
                        changeUserPasswordData.setValue(changeUserPasswordResponse);
                    }
                });
        return changeUserPasswordData;
    }
*/


}
