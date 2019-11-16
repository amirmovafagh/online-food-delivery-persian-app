package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class ChangeUserPasswordResponse {
    @SerializedName("id")
    private long id;
    @SerializedName("newPassword")
    private String newPassword;
    @SerializedName("currentPassword")
    private String currentPassword;
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;

    //required fields
    /*public ChangeUserPasswordResponse(String id, String newPassword, String currentPassword) {
        this.id = id;
        this.newPassword = newPassword;
        this.currentPassword = currentPassword;
    }*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

