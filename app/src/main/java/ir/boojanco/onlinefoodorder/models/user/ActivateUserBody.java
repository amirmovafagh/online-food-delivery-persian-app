package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class ActivateUserBody {
    @SerializedName("confirmPassword")
    private String confirmPassword;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("password")
    private String password;

    public ActivateUserBody(String confirmPass, String mobile, String password) {
        this.confirmPassword = confirmPassword;
        this.mobile = mobile;
        this.password = password;
    }
}
