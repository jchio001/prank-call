package bachmanity.prank_call.Misc;

import android.view.View;
import android.widget.TextView;

import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;

//getting listview elem
public class NavDrawerViewHolder {
    @Bind(R.id.icon) public TextView icon;
    @Bind(R.id.text) public TextView text;

    public NavDrawerViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
