package bachmanity.prank_call.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import bachmanity.prank_call.API.Models.History;
import bachmanity.prank_call.Misc.HistoryViewHolder;
import bachmanity.prank_call.Misc.Utils;
import bachmanity.prank_call.R;

public class HistoryAdapter extends BaseAdapter {
    private List<History> histories;
    Context context;

    public HistoryAdapter(List<History> histories, Context context) {
        this.histories = histories;
        this.context = context;
    }

    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public History getItem(int position) { return histories.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        HistoryViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =  vi.inflate(R.layout.history_item, parent, false);
            holder = new HistoryViewHolder(view);
            view.setTag(holder);
        }
        else {
            holder = (HistoryViewHolder) view.getTag();
        }

        String from = histories.get(position).getFrom().equals("anonymous") ?
                "Someone" : "You";
        String to = histories.get(position).getTo().equals("anonymous") ?
                "someone" : "you";
        if (from.equals("You") && to.equals("you")) {
            to = "yourself";
        }

        holder.historyIcon.setTextColor(context.getResources().getColor(
                (from.equals("You") ? R.color.green : R.color.colorPrimaryDark)
        ));

        holder.historyText.setText((from + " called " + to)
        );
        holder.historyDateText.setText(
                Utils.getLocalTimeString(histories.get(position).getTimestamp()));

        return view;
    }

}
