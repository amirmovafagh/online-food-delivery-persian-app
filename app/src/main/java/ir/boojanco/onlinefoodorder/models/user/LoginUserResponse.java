package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

public class LoginUserResponse {
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    /*//required fields
    public LoginUserResponse(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }*/

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }


}
