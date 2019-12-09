package ir.boojanco.onlinefoodorder.data.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.data.networking.ApiInterface;
import ir.boojanco.onlinefoodorder.models.user.AddUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.ChangeUserPasswordResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserRepository {
    private static UserRepository userRepository;
    private static MutableLiveData<RegisterUserResponse> registerData;
    private static MutableLiveData<LoginUserResponse> loginData;
    private static MutableLiveData<AddUserAddressResponse> addAddressData;
    private static MutableLiveData<ChangeUserPasswordResponse> changeUserPasswordData;
    

    private ApiInterface apiInterface;

    public UserRepository(Retrofit retrofit) {
        apiInterface = retrofit.create(ApiInterface.class);
        registerData = new MutableLiveData<>();//must define here if get instance every time we open the app old data return from observer
        loginData = new MutableLiveData<>();
        addAddressData = new MutableLiveData<>();
        changeUserPasswordData = new MutableLiveData<>();

    }

    public Observable<LoginUserResponse> loginUser(@NonNull String phoneNumber, String password) {
        return apiInterface.loginUser(phoneNumber, password);
    }

    public Observable<RegisterUserResponse> registerUser(@NonNull String phoneNumber) {

        return apiInterface.registerUser(phoneNumber);
    }

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


}
