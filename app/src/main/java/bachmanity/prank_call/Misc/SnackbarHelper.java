package bachmanity.prank_call.Misc;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import bachmanity.prank_call.R;

/**
 * Created by jman0_000 on 8/14/2016.
 */
public class SnackbarHelper {
    public static Snackbar showSnackbar(Context context, View parent, String message) {
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        View rootView = snackbar.getView();
        snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = (TextView) rootView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(context.getResources().getColor(R.color.white));
        snackbar.show();
        return snackbar;
    }
}
