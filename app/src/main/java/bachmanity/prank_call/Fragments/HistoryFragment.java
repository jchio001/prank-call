package bachmanity.prank_call.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import bachmanity.prank_call.API.Models.History;
import bachmanity.prank_call.API.RetrofitSingleton;
import bachmanity.prank_call.API.Services.Callbacks.HistoryCallback;
import bachmanity.prank_call.Adapters.HistoryAdapter;
import bachmanity.prank_call.Misc.HistorySingleton;
import bachmanity.prank_call.Misc.SnackbarHelper;
import bachmanity.prank_call.Misc.Utils;
import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryFragment extends Fragment {

    @Bind(R.id.history_list_view) ListView historyListView;
    @Bind(R.id.history_text_view) TextView historyTextView;

    CoordinatorLayout parent;
    ProgressDialog progressDialog;
    HistoryAdapter historyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        parent = (CoordinatorLayout) getActivity().findViewById(R.id.main_layout);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, rootView);

        boolean initialLoad = HistorySingleton.getInstance().isInitialLoad();
        if (initialLoad) {
            makeAPICall();
        } else {
            loadHistory();
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void makeAPICall() {
        String from, to;
        from = Utils.getPhoneNumber(getActivity());
        to = from;

        if (from.isEmpty()) {
            from = Utils.getIPAddr(getActivity());
            to = Utils.getDevicePhoneNumber(getActivity());
        }

        if (from == null) {
            SnackbarHelper.showSnackbar(getActivity(), parent, getString(R.string.get_history_failed));
            historyTextView.setText(getString(R.string.get_history_failed));
            historyTextView.setVisibility(View.VISIBLE);
            return;
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        RetrofitSingleton.getInstance().getMatchingService()
                .getHistory(from, to).enqueue(new HistoryCallback());

    }

    @Subscribe
    public void retrieveHistory(List<History> historyPage) {
        HistorySingleton.getInstance().setInitialLoad(false);
        List<History> history = HistorySingleton.getInstance().addHistoryPage(historyPage);
        if (history.size() == 0) {
            historyTextView.setText(getString(R.string.no_history));
            historyTextView.setVisibility(View.VISIBLE);
        }
        else {
            progressDialog.dismiss();
            loadHistory();
        }
    }

    @Subscribe
    public void onError(String msg) {
        progressDialog.dismiss();
        SnackbarHelper.showSnackbar(getContext(), parent, msg);
    }


    public void loadHistory() {
        historyAdapter = new HistoryAdapter(HistorySingleton.getInstance().getHistoryList(),
                getContext());
        historyListView.setAdapter(historyAdapter);
        historyListView.setVisibility(View.VISIBLE);
    }

}
