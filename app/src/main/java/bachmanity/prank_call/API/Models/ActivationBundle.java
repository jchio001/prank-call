package bachmanity.prank_call.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jman0_000 on 9/25/2016.
 */
public class ActivationBundle {
    @SerializedName("phone_number")
    @Expose
    private String number;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("key")
    @Expose
    private int key;

    public ActivationBundle(String number, String password, int key) {
        this.number = number;
        this.password = password;
        this.key = key;
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

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
