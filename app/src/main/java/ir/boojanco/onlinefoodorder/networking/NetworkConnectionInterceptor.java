package ir.boojanco.onlinefoodorder.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkConnectionInterceptor implements Interceptor {
    Context context;

    public NetworkConnectionInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isInternetAvailable()) {
            throw new NoNetworkConnectionException();
        }
        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        }
        return false;
    }
}
