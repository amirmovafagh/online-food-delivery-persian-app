package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse {
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("newPassword")
    private String newPassword;
    @SerializedName("currentPassword")
    private String currentPassword;
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;

    //required fields
    public ChangePasswordResponse(String mobile, String newPassword, String currentPassword) {
        this.mobile = mobile;
        this.newPassword = newPassword;
        this.currentPassword = currentPassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

