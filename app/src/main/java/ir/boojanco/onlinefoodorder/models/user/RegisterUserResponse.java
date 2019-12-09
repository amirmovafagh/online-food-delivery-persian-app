package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class RegisterUserResponse {
    @SerializedName("creationTime")
    private long creationTime;
    @SerializedName("sendCodeCunt")
    private int sendCodeCunt;
    @SerializedName("verifyCunt")
    private int verifyCunt;

    @SerializedName("regCode")
    private String regCode;

    public String getRegCode() {
        return regCode;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public int getSendCodeCunt() {
        return sendCodeCunt;
    }

    public int getVerifyCunt() {
        return verifyCunt;
    }
}
