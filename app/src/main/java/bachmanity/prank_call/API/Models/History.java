package bachmanity.prank_call.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History {
    @SerializedName("history__id")
    @Expose
    private int id;

    @SerializedName("history__from")
    @Expose
    private String from;

    @SerializedName("history__to")
    @Expose
    private String to;

    @SerializedName("history__timestamp")
    @Expose
    private String timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
