package bachmanity.prank_call.Misc.ViewHolders;

import android.view.View;
import android.widget.TextView;

import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleViewHolder {
    @Bind(R.id.simple_textview) public TextView textView;

    public SimpleViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

}
