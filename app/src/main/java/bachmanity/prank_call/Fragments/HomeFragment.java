package bachmanity.prank_call.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import bachmanity.prank_call.API.Models.CallBundle;
import bachmanity.prank_call.API.RetrofitSingleton;
import bachmanity.prank_call.API.Services.Callbacks.CallCallback;
import bachmanity.prank_call.Misc.Constants;
import bachmanity.prank_call.Misc.SnackbarHelper;
import bachmanity.prank_call.Misc.Utils;
import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {
    @Bind(R.id.phoneNumEditText) EditText numberToCall;
    @Bind(R.id.call) Button call;

    CoordinatorLayout parent;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {
        parent = (CoordinatorLayout) getActivity().findViewById(R.id.main_layout);
        View rootView = inflater.inflate(R.layout.fragment_home, parentViewGroup, false);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.call)
    public void onCall() {
        final String number = numberToCall.getText().toString();

        if (number.length() < 10) {
            SnackbarHelper.showSnackbar(getContext(), parent, Constants.INVALID_NUMBER);
            return;
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.calling));

        Utils.hideKeyboard(parent, getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.adview_for_dialog);

        AdView dialogAd = (AdView) progressDialog.findViewById(R.id.dialog_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        dialogAd.loadAd(adRequest);
        dialogAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                CallBundle callBundle = new CallBundle(number, Utils.getId(getContext()), Utils.getPassword(getContext()));
                RetrofitSingleton.getInstance().getMatchingService()
                        .call(callBundle).
                        enqueue(new CallCallback());
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
    public void onCall(String resp) {
        progressDialog.dismiss();
        SnackbarHelper.showSnackbar(getContext(), parent, resp);
    }

}
