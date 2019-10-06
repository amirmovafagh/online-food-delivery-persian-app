package ir.boojanco.onlinefoodorder.networking;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.models.user.RegisterResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static UserRepository userRepository;

    public static UserRepository getInstance(){
        if(userRepository == null){
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    private UserApiInterface userApiInterface;

    public UserRepository(){
        userApiInterface = RetrofitService.getService().create(UserApiInterface.class);
    }

    public MutableLiveData<RegisterResponse> registerUser(String phoneNumber){
        RegisterResponse register = new RegisterResponse(phoneNumber);
        MutableLiveData<RegisterResponse> registerData = new MutableLiveData<>();
        userApiInterface.registerUser(register).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful())

                    registerData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("amir",t.getMessage());
                //registerData.setValue(null);
            }
        });
        return registerData;
    }

}
