package ir.boojanco.onlinefoodorder.models;

import android.util.Patterns;

// user model
public class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        if (email == null)
            return "";
        else
            return email;
    }

    public String getPassword() {
        if (password == null)
            return "";
        else
            return password;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }

    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }

}
