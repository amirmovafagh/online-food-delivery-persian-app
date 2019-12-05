package ir.boojanco.onlinefoodorder.networking;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.models.user.AddUserAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.ChangeUserPasswordResponse;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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


    public static UserRepository getInstance() {
        registerData = new MutableLiveData<>();//must define here if get instance every time we open the app old data return from observer
        loginData = new MutableLiveData<>();
        addAddressData = new MutableLiveData<>();
        changeUserPasswordData = new MutableLiveData<>();
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    private UserApiInterface userApiInterface;

    public UserRepository() {
        userApiInterface = RetrofitService.getService().create(UserApiInterface.class);
    }

    public Observable<LoginUserResponse> loginUser(@NonNull String phoneNumber, String password) {
        return userApiInterface.loginUser(phoneNumber, password);
    /*.enqueue(new Callback<LoginUserResponse>() {
            @Override
            public void onResponse(Call<LoginUserResponse> call, Response<LoginUserResponse> response) {
                if (response.isSuccessful())

                        loginData.setValue(response.body());


                 *//*else {
                         loginData.setValue(response.errorBody().);

                }*//*
            }

            @Override
            public void onFailure(Call<LoginUserResponse> call, Throwable t) {
                loginData.setValue(null);
            }
        });
        return loginData;*/
    }

    public MutableLiveData<RegisterUserResponse> registerUser(@NonNull String phoneNumber) {


        /*userApiInterface.registerUser(phoneNumber).enqueue(new Callback<RegisterUserResponse>() {
            @Override
            public void onResponse(Call<RegisterUserResponse> call, Response<RegisterUserResponse> response) {
                if (response.isSuccessful())

                    registerData.setValue(response.body());

            }

            @Override
            public void onFailure(Call<RegisterUserResponse> call, Throwable t) {
                Log.e("amir", t.getMessage());
                registerData.setValue(null);
            }
        });*/

        userApiInterface.registerUser(phoneNumber).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegisterUserResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RegisterUserResponse registerUserResponse) {
                        registerData.setValue(registerUserResponse);
                    }
                });
        return registerData;
    }

    public MutableLiveData<AddUserAddressResponse> addUserAddress(long id, String address, String zipCode, long regionId) {

        userApiInterface.addUserAddress(id, address, zipCode, regionId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
        userApiInterface.changeUserPassword(id, newPassword, currentPassword).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
