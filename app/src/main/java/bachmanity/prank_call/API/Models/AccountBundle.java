package bachmanity.prank_call.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jonathan Chiou on 8/26/2016.
 */
public class AccountBundle {
    @SerializedName("phone_number")
    @Expose
    private String number;

    @SerializedName("password")
    @Expose
    private String password;

    public AccountBundle(String number, String password) {
        this.number = number;
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
