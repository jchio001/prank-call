package bachmanity.prank_call.Misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jman0_000 on 8/14/2016.
 */
public class SPSingleton {
    SharedPreferences sp;
    private static SPSingleton instance = null;

    protected SPSingleton(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SPSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new SPSingleton(context);
        }

        return instance;
    }

    public SharedPreferences getSp() {
        return sp;
    }
}
