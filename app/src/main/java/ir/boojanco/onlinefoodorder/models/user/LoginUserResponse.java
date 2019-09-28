package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class LoginUserResponse {
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("password")
    private String password;
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;

    //required fields
    public LoginUserResponse(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

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

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
