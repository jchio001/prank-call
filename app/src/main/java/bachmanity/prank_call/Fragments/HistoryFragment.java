package bachmanity.prank_call.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import bachmanity.prank_call.API.Models.History;
import bachmanity.prank_call.API.RetrofitSingleton;
import bachmanity.prank_call.API.Services.Callbacks.HistoryCallback;
import bachmanity.prank_call.Adapters.HistoryAdapter;
import bachmanity.prank_call.Misc.Constants;
import bachmanity.prank_call.Misc.HistorySingleton;
import bachmanity.prank_call.Misc.SnackbarHelper;
import bachmanity.prank_call.Misc.Utils;
import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryFragment extends Fragment {

    @Bind(R.id.history_list_view) ListView historyListView;
    @Bind(R.id.history_text_view) TextView historyTextView;
    @Bind(R.id.history_main) RelativeLayout historyMainLayout;

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

        AdView ad = (AdView) getActivity().findViewById(R.id.adView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) historyMainLayout.getLayoutParams();
        params.setMargins(0, 0, 0, ad.getHeight());
        //params.setMar = parent.getHeight() - ad.getHeight();
        historyMainLayout.setLayoutParams(params);
        historyMainLayout.requestLayout();

        if (Utils.getId(getContext()) != -1) {
            boolean load = HistorySingleton.getInstance().isLoad();
            if (load) {
                makeAPICall();
            } else {
                loadHistory();
            }
        }
        else {
            historyTextView.setText(getString(R.string.login_for_history));
            historyTextView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void makeAPICall() {
        final String from, to;
        from = Utils.getPhoneNumber(getActivity());
        to = from;

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.adview_for_dialog);

        AdView dialogAd = (AdView) progressDialog.findViewById(R.id.dialog_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        dialogAd.loadAd(adRequest);
        dialogAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        RetrofitSingleton.getInstance().getMatchingService()
                                .getHistory(from, to).enqueue(new HistoryCallback());
                    }
                }, 2000);
            }

            @Override
            public void onAdFailedToLoad(int errorcode) {
                progressDialog.dismiss();
                SnackbarHelper.showSnackbar(getContext(), parent, getString(R.string.call_not_made));
                super.onAdFailedToLoad(errorcode);
            }
        });
    }

    @Subscribe
    public void retrieveHistory(List<History> historyPage) {
        HistorySingleton.getInstance().setLoad(false);
        List<History> history = HistorySingleton.getInstance().refreshHistory(historyPage);
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
        if (msg.equals(Constants.HISTORY_FAILED)) {
            SnackbarHelper.showSnackbar(getContext(), parent, getString(R.string.get_history_failed));
            historyTextView.setText(getString(R.string.get_history_failed));
        }
    }


    public void loadHistory() {
        historyAdapter = new HistoryAdapter(HistorySingleton.getInstance().getHistoryList(),
                getContext());
        historyListView.setAdapter(historyAdapter);
        historyListView.setVisibility(View.VISIBLE);
    }

}
