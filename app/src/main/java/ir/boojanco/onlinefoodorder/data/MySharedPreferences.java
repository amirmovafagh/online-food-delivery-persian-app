package ir.boojanco.onlinefoodorder.data;

import android.app.Application;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String userAuthTokenKey = "userAuthTokenKey";
    private String cityKey = "cityKey";
    private String phoneKey = "phoneKey";
    private String stateKey = "stateKey";
    private String latitudeKey = "locationLatitude";
    private String longitudKey = "locationLongitude";

    public MySharedPreferences(Application application){
        sharedPreferences = application.getSharedPreferences("boojanPref",0);
        editor = sharedPreferences.edit();
    }

    public void setUserAuthTokenKey(String userToken){
        editor.putString(userAuthTokenKey,userToken);
        editor.commit();
    }

    public void removeUserAuthTokenKey() {
        editor.remove(userAuthTokenKey);
        editor.commit();
    }

    public String getUserAuthTokenKey(){
        return sharedPreferences.getString(userAuthTokenKey,null);
    }

    public void setPhoneNumber(String phoneNumber){
        editor.putString(phoneKey,phoneNumber);
        editor.commit();
    }

    public void removePhoneNumber() {
        editor.remove(phoneKey);
        editor.commit();
    }

    public String getPhoneNumber(){
        return sharedPreferences.getString(phoneKey,null);
    }

    public void setCity(String city){
        editor.putString(cityKey,city);
        editor.commit();
    }

    public String getCity(){
        return sharedPreferences.getString(cityKey,"تهران");
    }

    public void setState(String state) {
        editor.putString(stateKey, state);
        editor.commit();
    }

    public String getState() {
        return sharedPreferences.getString(stateKey, "تهران");
    }

    public void setLatitude(double latitude){
        editor.putLong(latitudeKey, Double.doubleToRawLongBits(latitude));
        editor.commit();
    }

    public double getLatitude(){
        return Double.longBitsToDouble(sharedPreferences.getLong(latitudeKey, Double.doubleToLongBits(0)));
    }

    public void setLongitud(double longitude){
        editor.putLong(longitudKey, Double.doubleToRawLongBits(longitude));
        editor.commit();
    }

    public double getLongitud(){
        return Double.longBitsToDouble(sharedPreferences.getLong(longitudKey, Double.doubleToLongBits(0)));
    }
}
