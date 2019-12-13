package ir.boojanco.onlinefoodorder.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ir.boojanco.onlinefoodorder.ui.activities.LoginActivity;
import ir.boojanco.onlinefoodorder.ui.activities.MainActivity;
import ir.boojanco.onlinefoodorder.ui.activities.RegisterActivity;
import ir.boojanco.onlinefoodorder.ui.fragments.HomeFragment;
import ir.boojanco.onlinefoodorder.ui.fragments.RestaurantFragment;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);
    void inject(RegisterActivity registerActivity);
    void inject(HomeFragment homeFragment);
    void inject(RestaurantFragment restaurantFragment);

}

