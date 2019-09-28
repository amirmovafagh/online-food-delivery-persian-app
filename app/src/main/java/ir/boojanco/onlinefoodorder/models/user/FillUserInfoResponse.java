package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class FillUserInfoResponse {
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("name")
    private String name;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("password")
    private String password;
    @SerializedName("birthDate")
    private String birthDate;
    @SerializedName("email")
    private String email;
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;

    //required fields

    public FillUserInfoResponse(String mobile, String name, String lastName, String password, String birthDate, String email) {
        this.mobile = mobile;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.birthDate = birthDate;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
