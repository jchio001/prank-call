package bachmanity.prank_call.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bachmanity.prank_call.Misc.ContactsBundle;
import bachmanity.prank_call.Misc.ViewHolders.SimpleViewHolder;
import bachmanity.prank_call.R;

/**
 * Created by jman0_000 on 11/6/2016.
 */

public class AutoCompleteAdapter extends ArrayAdapter<ContactsBundle> {
    private Context context;
    private ArrayList<ContactsBundle> contacts;
    private ArrayList<ContactsBundle> suggestions;

    @SuppressWarnings("unchecked")
    public AutoCompleteAdapter(Context context, int viewResourceId, ArrayList<ContactsBundle> contacts) {
        super(context, viewResourceId, new ArrayList<ContactsBundle>());
        this.context = context;
        this.contacts = (ArrayList<ContactsBundle>) contacts.clone();
        this.suggestions = new ArrayList<>();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.simple_list_item, parent, false);
            holder = new SimpleViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SimpleViewHolder) convertView.getTag();
        }

        //USE BUILT IN GETITEM() METHOD
        holder.textView.setText(getItem(position).getContactNum());

        return convertView;
    }

    @Override
    public android.widget.Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        public String convertResultToString(Object resultValue) {
            return ((ContactsBundle) resultValue).getContactNum();
        }

        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint){
            if (constraint != null) {
                suggestions.clear();
                for (int i = 0, j = 0; i < contacts.size() && j < 5; i++) {
                    if (contacts.get(i).getContactNum().toLowerCase()
                            .contains(constraint.toString().toLowerCase())) {
                        suggestions.add(contacts.get(i));
                        j++;
                    }
                }

                FilterResults fr = new FilterResults();
                fr.values = suggestions;
                fr.count = suggestions.size();
                return fr;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            List<ContactsBundle> filteredList = (ArrayList<ContactsBundle>) results.values;
            if (results.count > 0) {
                for (ContactsBundle c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }

    };

}
