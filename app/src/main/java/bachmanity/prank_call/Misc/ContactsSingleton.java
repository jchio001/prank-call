package bachmanity.prank_call.Misc;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ContactsSingleton {
    private static ContactsSingleton instance;
    private ArrayList<ContactsBundle> contacts;

    public static ContactsSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new ContactsSingleton(context);
        }

        return instance;
    }

    private ContactsSingleton(Context context) {
        this.contacts = new ArrayList<ContactsBundle>();

        Cursor cursor = context.getContentResolver().query(ContactsContract.
                CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            HashSet<String> numberMap = new HashSet<>();
            cursor.moveToFirst();
            try {
                do {
                    String name = cursor.getString(nameIndex);
                    String number = cursor.getString(phoneNumberIndex).replaceAll("[^0-9.]", "");
                    int len = number.length();
                    if (len == 10) {
                        if (!numberMap.contains(number)) {
                            numberMap.add(number);
                            this.contacts.add(new ContactsBundle(name, number));
                        }
                    } else if (len == 11) {
                        if (!numberMap.contains(number.substring(1))) {
                            numberMap.add(number.substring(1));
                            this.contacts.add(new ContactsBundle(name, number.substring(1)));
                        }
                    }

                } while (cursor.moveToNext());
            } catch (Exception e) {
                Utils.printStackTrace(e);
            } finally {
                cursor.close();
            }
        }
    }

    public ContactsBundle getRandomContact() {
        int contactLen = this.contacts.size();
        System.out.println(contactLen);
        if (contactLen > 0) {
            Random random = new Random();
            return this.contacts.get(random.nextInt(contactLen));
        } else {
            return null;
        }
    }

    public ArrayList<ContactsBundle> getContacts() {
        return this.contacts;
    }
}
