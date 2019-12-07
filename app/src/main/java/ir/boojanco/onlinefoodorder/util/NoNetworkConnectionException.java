package ir.boojanco.onlinefoodorder.util;

import java.io.IOException;

/*NetworkConnectionInterceptor interceptor will handle every API call,
        when, if no network is found
        then it interrupts the normal flow of execution so
        we will create our own NoNetworkConnectionException NoNetworkConnectionException class.
        that throws an NoNetworkConnectionException.*/
public class NoNetworkConnectionException extends IOException {

    @Override
    public String getMessage() {
        return "لطفا اتصال به اینترنت را بررسی کنید.";
    }

}