package ir.boojanco.onlinefoodorder.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ir.boojanco.onlinefoodorder.ui.activities.LoginActivity;
import ir.boojanco.onlinefoodorder.ui.activities.MainActivity;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(LoginActivity mainActivity);

    void inject(MainActivity usbService);

}

