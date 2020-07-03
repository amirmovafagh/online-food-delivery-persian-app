package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class ChangePassword {
    @SerializedName("confirmNewPassword")
    String confirmNewPassword;
    @SerializedName("newPassword")
    String newPassword;
    @SerializedName("oldPassword")
    String oldPassword;

    public ChangePassword(String confirmNewPassword, String newPassword, String oldPassword) {
        this.confirmNewPassword = confirmNewPassword;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
