package bachmanity.prank_call.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import bachmanity.prank_call.Misc.ViewHolders.NavDrawerViewHolder;
import bachmanity.prank_call.R;

public class NavDrawerAdapter extends BaseAdapter {
    private String[] icons;
    private String[] text;
    Context context;

    public NavDrawerAdapter(String[] icons, String[] text, Context context) {
        this.icons = icons;
        this.text = text;
        this.context = context;
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public String getItem(int position) {
        return text[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        NavDrawerViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.drawer_item, parent, false);
            holder = new NavDrawerViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (NavDrawerViewHolder) view.getTag();
        }

        holder.icon.setText(icons[position]);
        holder.text.setText(text[position]);

        if (position == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
        }

        return view;
    }

}
