package ir.boojanco.onlinefoodorder.dagger;

import android.app.Application;
import android.app.PendingIntent;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;

import ir.boojanco.onlinefoodorder.R;

public class App extends Application {
    public final static String webServerMediaRoute = "https://mazeeh.ir/api/media/download/";
    private AppComponent component;

    //Instantiating the component
    @Override
    public void onCreate() {
        super.onCreate();
        // Dagger%COMPONENT_NAME%
        component = DaggerAppComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) //This also corresponds to the name of your module: %component_name%Module
                .netModule(new NetModule("https://mazeeh.ir"))
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead:
        //  mAppComponent = com.codepath.dagger.components.DaggerAppComponent.create();
    }

    public AppComponent getComponent() {
        return component;
    }

    // important code for loading image here
    @BindingAdapter({"image"})
    public static void loadImage(ImageView imageView, String imageURL) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(imageView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(new RequestOptions())
                .load(imageURL)
                .placeholder(circularProgressDrawable)
                .into(imageView);
    }

}

