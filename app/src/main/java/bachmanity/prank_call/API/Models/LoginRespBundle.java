package bachmanity.prank_call.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRespBundle {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("account__active")
    @Expose
    private boolean account_active;

    @SerializedName("account__subbed")
    @Expose
    private boolean account_subbed;

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

    public boolean isAccount_subbed() {
        return account_subbed;
    }

    public void setAccount_subbed(boolean account_subbed) {
        this.account_subbed = account_subbed;
    }
}
