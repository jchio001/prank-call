package bachmanity.prank_call.Misc;

import android.view.View;
import android.widget.TextView;

import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jman0_000 on 9/23/2016.
 */
public class HistoryViewHolder {
    @Bind(R.id.history_icon) public TextView historyIcon;
    @Bind(R.id.history_text) public TextView historyText;
    @Bind(R.id.history_date_text) public TextView historyDateText;

    public HistoryViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
