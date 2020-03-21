package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import ir.boojanco.onlinefoodorder.util.persianDate.PersianDate;
import ir.boojanco.onlinefoodorder.util.persianDate.PersianDateFormat;

public class UserProfileResponse {

    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("birthDate")
    private long birthDate;

    PersianDate pdate;
    PersianDateFormat pdformater;

    public UserProfileResponse() {
        pdformater = new PersianDateFormat("Y/m/d");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getShamsiDate() {
        pdate = new PersianDate(birthDate);
        return pdformater.format(pdate);
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public long getBirthDate() {
        return birthDate;
    }
}
