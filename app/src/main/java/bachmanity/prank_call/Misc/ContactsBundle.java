package bachmanity.prank_call.Misc;

/**
 * Created by jman0_000 on 10/14/2016.
 */
public class ContactsBundle {
    private String contactName;
    private String contactNum;

    public ContactsBundle(String contactName, String contactNum) {
        this.contactName = contactName;
        this.contactNum = contactNum;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }
}
