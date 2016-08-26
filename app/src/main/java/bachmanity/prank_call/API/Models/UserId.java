package bachmanity.prank_call.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jonathan Chiou on 8/26/2016.
 */
public class UserId {
    @SerializedName("id")
    @Expose
    private long userId;

    public long getUserId() {
        return userId;
    }
}
