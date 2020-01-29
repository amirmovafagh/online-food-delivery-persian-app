package ir.boojanco.onlinefoodorder.dagger;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import ir.boojanco.onlinefoodorder.R;

public class App extends Application {
    public final static String webServerMediaRoute = "http://198.143.180.86:8080/api/v1/media/download/";
    private AppComponent component;
    //Instantiating the component
    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        // Dagger%COMPONENT_NAME%
        component = DaggerAppComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) //This also corresponds to the name of your module: %component_name%Module
                .netModule(new NetModule("http://198.143.180.86:8080"))
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mAppComponent = com.codepath.dagger.components.DaggerAppComponent.create();
    }

    public AppComponent getComponent() {
        return component;
    }
}

