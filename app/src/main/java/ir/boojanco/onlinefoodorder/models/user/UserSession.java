package ir.boojanco.onlinefoodorder.models.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSession {

    @SerializedName("accountBalance")
    int accountBalance;
    @SerializedName("mobile")
    String mobile;
    @SerializedName("name")
    String name;
    @SerializedName("permissionList")
    List<String> permissionList;
    @SerializedName("roleList")
    List<String> roleList;

    public int getAccountBalance() {
        return accountBalance;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public List<String> getRoleList() {
        return roleList;
    }
}
