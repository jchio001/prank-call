package bachmanity.prank_call.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jonathan Chiou on 8/26/2016.
 */
public class LoginRespBundle {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("account__active")
    @Expose
    private boolean account_active;

    public LoginRespBundle(int id, boolean account_active) {
        this.id = id;
        this.account_active = account_active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAccount_active() {
        return account_active;
    }

    public void setAccount_active(boolean account_active) {
        this.account_active = account_active;
    }
}
