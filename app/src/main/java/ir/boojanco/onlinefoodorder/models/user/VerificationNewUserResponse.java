package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class VerificationNewUserResponse {
    @SerializedName("id")
    private long id;
    @SerializedName("mobile")
    private String mobile;

    public long getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }
}
