package bachmanity.prank_call.Misc;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Jonathan Chiou on 8/25/2016.
 */
public class Utils {
    public static void hideKeyboard(View v, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static String getMD5Hash(String input) {
        String hashtext = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes());
            byte[] digest = messageDigest.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hashtext;
    }

    public static int getId(Context context) {
        return SPSingleton.getInstance(context).getSp().getInt(Constants.ID, -1);
    }

    public static void saveId(int id, Context context) {
        SPSingleton.getInstance(context).getSp().edit().putInt(Constants.ID, id).commit();
    }
}
