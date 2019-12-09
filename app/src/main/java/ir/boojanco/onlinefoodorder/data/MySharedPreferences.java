package ir.boojanco.onlinefoodorder.data;

import android.app.Application;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String userAuthTokenKey = "userAuthTokenKey";

    public MySharedPreferences(Application application){
        sharedPreferences = application.getSharedPreferences("boojanPref",0);
        editor = sharedPreferences.edit();
    }

    public void setUserAuthTokenKey(String userToken){
        editor.putString(userAuthTokenKey,userToken);
        editor.commit();
    }

    public String getUserAuthTokenKey(){
        return sharedPreferences.getString(userAuthTokenKey,null);
    }
}
