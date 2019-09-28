package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("code")
    private String code;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("message")
    private String message;
    @SerializedName("verificationCode")
    private String verificationCode;
    @SerializedName("reason")
    private String reason;

    //required fields
    public RegisterResponse(String mobile) {
        this.mobile = mobile;

    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
