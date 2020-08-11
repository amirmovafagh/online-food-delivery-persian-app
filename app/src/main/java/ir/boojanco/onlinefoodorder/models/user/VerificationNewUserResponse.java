package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class VerificationNewUserResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("mobile")
    private String mobile;

    public String getStatus() {
        return status;
    }

    public String getMobile() {
        return mobile;
    }
}
