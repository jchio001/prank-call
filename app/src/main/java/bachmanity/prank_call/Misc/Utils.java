package bachmanity.prank_call.Misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;

import bachmanity.prank_call.R;

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

    public static String getIPAddr(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        if (ip.equals("0.0.0.0")) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface
                        .getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            return ipaddress;
                        }
                    }
                }
            } catch (SocketException ex) {
                Log.e("GetIP", "Exception in Get IP Address: " + ex.toString());
            }
            return null;
        }

        return ip;
    }

    public static String getLocalTimeString(String timestamp) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date utcDate = format.parse(timestamp);
            format = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
            format.setTimeZone(Calendar.getInstance().getTimeZone());
            return format.format(utcDate);
        }
        catch (ParseException e) {
            return null;
        }
    }

    public static String getDevicePhoneNumber(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getLine1Number();
    }

    public static void logout(Context context) {
        SharedPreferences sp = SPSingleton.getInstance(context).getSp();
        sp.edit().remove(Constants.ID).commit();
        sp.edit().remove(Constants.NUMBER).commit();
        sp.edit().remove(Constants.PASSWORD).commit();
        HistorySingleton history = HistorySingleton.getInstance();
        history.setLoad(true);
        history.deleteHistory();
    }

    public static int getId(Context context) {
        return SPSingleton.getInstance(context).getSp().getInt(Constants.ID, -1);
    }

    public static void saveId(int id, Context context) {
        SPSingleton.getInstance(context).getSp().edit().putInt(Constants.ID, id).commit();
    }

    public static String getPassword(Context context) {
        return SPSingleton.getInstance(context).getSp().getString(Constants.PASSWORD, "");
    }

    public static void savePassword(String password, Context context) {
        SPSingleton.getInstance(context).getSp().edit().putString(Constants.PASSWORD, password).commit();
    }

    public static String getPhoneNumber(Context context) {
        return SPSingleton.getInstance(context).getSp().getString(Constants.NUMBER, "");
    }

    public static void savePhoneNumber(String number, Context context) {
        SPSingleton.getInstance(context).getSp().edit().putString(Constants.NUMBER, number).commit();
    }

    public static void saveAccountStatus(boolean isActive, Context context) {
        SPSingleton.getInstance(context).getSp().edit().putBoolean(Constants.ACCOUNT_ACTIVE, isActive).commit();
    }

    public static boolean getAccountStatus(Context context) {
        return SPSingleton.getInstance(context).getSp().getBoolean(Constants.ACCOUNT_ACTIVE, false);
    }
}
