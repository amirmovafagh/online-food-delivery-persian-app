package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class LoginUserResponse {
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("password")
    private String password;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("message")
    private String message;
    @SerializedName("id")
    private String id;

    /*//required fields
    public LoginUserResponse(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }*/

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
