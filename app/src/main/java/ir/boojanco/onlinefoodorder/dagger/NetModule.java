package ir.boojanco.onlinefoodorder.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.data.networking.NetworkConnectionInterceptor;
import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.HomeViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.LoginViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.RegisterViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantViewModelFactory;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    String mBaseUrl;

    // Constructor needs one parameter to instantiate.
    public NetModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }
    @Provides
    @Singleton
    LoginViewModelFactory provideLoginViewModelFactory(Application application, UserRepository userRepository){
        LoginViewModelFactory factory =
                new LoginViewModelFactory(application, userRepository);
        return factory;
    }

    @Provides
    @Singleton
    RegisterViewModelFactory provideRegisterViewModelFactory(Application application, UserRepository userRepository){
        RegisterViewModelFactory factory =
                new RegisterViewModelFactory(application, userRepository);
        return factory;
    }

    @Provides
    @Singleton
    HomeViewModelFactory provideHomeViewModelFactory(Application application){
        HomeViewModelFactory factory =
                new HomeViewModelFactory(application);
        return factory;
    }

    @Provides
    @Singleton
    RestaurantRepository provideRestaurantRepository(Retrofit retrofit) {
        RestaurantRepository restaurantRepository = new RestaurantRepository(retrofit);
        return restaurantRepository;
    }

    @Provides
    @Singleton
    RestaurantViewModelFactory provideRestaurntViewModelFactory(Application application ,RestaurantRepository restaurantRepository){
        RestaurantViewModelFactory factory =
                new RestaurantViewModelFactory(application, restaurantRepository);
        return factory;
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    MySharedPreferences providesMySharedPreferences(Application application) {
        MySharedPreferences mySharedPreferences =
                new MySharedPreferences(application);
        return mySharedPreferences;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, NetworkConnectionInterceptor networkConnectionInterceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor);
        //client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(Retrofit retrofit) {
        UserRepository userRepository = new UserRepository(retrofit);
        return userRepository;
    }


    @Provides
    @Singleton
    NetworkConnectionInterceptor provideNetworkConnectionInterceptor( Application application) {
        NetworkConnectionInterceptor networkConnectionInterceptor = new NetworkConnectionInterceptor(application);
        return networkConnectionInterceptor;
    }
}