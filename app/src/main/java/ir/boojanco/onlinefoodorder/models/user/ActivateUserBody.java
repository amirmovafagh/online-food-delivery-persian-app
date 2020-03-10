package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class ActivateUserBody {
    @SerializedName("id")
    private long id;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("password")
    private String password;

    public ActivateUserBody(long id, String mobile, String password) {
        this.id = id;
        this.mobile = mobile;
        this.password = password;
    }
}
