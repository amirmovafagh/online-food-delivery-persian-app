package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Body;

public class EditUserInfoResponse {
    @SerializedName("mobile") //maybe change in future
    private String mobile;
    @SerializedName("name")
    private String name;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("birthDate")
    private String birthDate;
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;

    //required fields
    public EditUserInfoResponse(String mobile, String name, String lastName, String birthDate) {
        this.mobile = mobile;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
