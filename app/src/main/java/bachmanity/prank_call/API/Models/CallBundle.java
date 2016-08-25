package bachmanity.prank_call.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jman0_000 on 8/21/2016.
 */
public class CallBundle {
    @SerializedName("number")
    @Expose
    private String receiverNumber;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("id")
    @Expose
    private String id;

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
