package bachmanity.prank_call.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import bachmanity.prank_call.API.Models.ActivationBundle;
import bachmanity.prank_call.API.Models.CallBundle;
import bachmanity.prank_call.API.RetrofitSingleton;
import bachmanity.prank_call.API.Services.Callbacks.ActivationCallback;
import bachmanity.prank_call.API.Services.Callbacks.CallCallback;
import bachmanity.prank_call.Misc.Constants;
import bachmanity.prank_call.Misc.ContactsBundle;
import bachmanity.prank_call.Misc.ContactsSingleton;
import bachmanity.prank_call.Misc.HistorySingleton;
import bachmanity.prank_call.Misc.SnackbarHelper;
import bachmanity.prank_call.Misc.Utils;
import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {
    @Bind(R.id.phoneNumEditText) EditText numberToCall;
    @Bind(R.id.call) Button call;
    @Bind(R.id.verify_text) TextView verifyText;

    CoordinatorLayout parent;
    ProgressDialog progressDialog;
    AlertDialog.Builder verifyDialogBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {
        parent = (CoordinatorLayout) getActivity().findViewById(R.id.main_layout);
        View rootView = inflater.inflate(R.layout.fragment_home, parentViewGroup, false);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, rootView);
        verifyText.setVisibility(
                ((Utils.getAccountStatus(getContext()) &&
                        Utils.getId(getContext()) != -1) ||
                        Utils.getId(getContext()) == -1) ? View.GONE : View.VISIBLE);
        return rootView;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == Constants.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fillEditTextWithRandom();
            } else {
                SnackbarHelper.showSnackbar(getContext(), parent, getString(R.string.permission_denied));
            }
        }
    }

    @OnClick(R.id.random)
    public void setRandom() {
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage(getString(R.string.calling));
        Utils.hideKeyboard(parent, getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, Constants.PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            fillEditTextWithRandom();
        }
        //progressDialog.show();
        //call(ContactsSingleton.getInstance(getContext()).getRandomContact().getContactNum());
    }

    public void fillEditTextWithRandom() {
        ContactsBundle randomContact = ContactsSingleton.getInstance(getContext()).getRandomContact();
        if (randomContact != null) {
            String randomNumber = randomContact.getContactNum();
            numberToCall.setText(randomNumber);
            numberToCall.setSelection(randomNumber.length());
        } else {
            SnackbarHelper.showSnackbar(getContext(), parent, getString(R.string.no_contacts));
        }
    }

    @OnClick(R.id.call)
    public void onCall() {
        final String number = numberToCall.getText().toString();

        if (number.length() < 10) {
            SnackbarHelper.showSnackbar(getContext(), parent, Constants.INVALID_NUMBER);
            return;
        }

        numberToCall.getText().clear();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.calling));
        Utils.hideKeyboard(parent, getContext());
        progressDialog.show();
        call(number);
    }

    public void call(final String number) {
        HistorySingleton.getInstance().setLoadFromServer(true);
        if (!Utils.getAccountSubStatus(getContext())) {
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
                            CallBundle callBundle = new CallBundle(number, Utils.getId(getContext()), Utils.getPassword(getContext()));
                            RetrofitSingleton.getInstance().getMatchingService()
                                    .call(callBundle).
                                    enqueue(new CallCallback());
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
        } else {
            CallBundle callBundle = new CallBundle(number, Utils.getId(getContext()), Utils.getPassword(getContext()));
            RetrofitSingleton.getInstance().getMatchingService()
                    .call(callBundle).
                    enqueue(new CallCallback());
        }
    }

    @OnClick(R.id.verify_text)
    public void onVerifyTextClick() {
        Utils.hideKeyboard(parent, getContext());
        verifyDialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.verify_view, null);
        verifyDialogBuilder.setView(dialogView);
        verifyDialogBuilder.setTitle(getString(R.string.verify_text));
        verifyDialogBuilder.setPositiveButton(
                R.string.verify_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String code = ((TextView) dialogView.findViewById(R.id.verify_edit_text))
                                .getText().toString();

                        if (code.length() < 4) {
                            SnackbarHelper.showSnackbar(getContext(), parent, getString(R.string.code_too_short));
                            return;
                        }

                        dialogInterface.dismiss();
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage(getString(R.string.loading));
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
                                        RetrofitSingleton.getInstance().getUserService()
                                                .activateAccount(
                                                        new ActivationBundle(Utils.getPhoneNumber(getContext()),
                                                                Utils.getPassword(getContext()),
                                                                Integer.parseInt(code))
                                                ).enqueue(new ActivationCallback());
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

                });
        verifyDialogBuilder.setNegativeButton(
                getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        verifyDialogBuilder.show();
    }

    @Subscribe
    public void onResp(String resp) {
        if (resp.equals(Constants.MADE_CALL)) {
            progressDialog.dismiss();
            SnackbarHelper.showSnackbar(getContext(), parent, getString(R.string.call_made));
            HistorySingleton.getInstance().setLoadFromServer(true);
        } else if (resp.equals(Constants.LOGIN_EVENT)) {
            verifyText.setVisibility((Utils.getAccountStatus(getContext()) &&
                    Utils.getId(getContext()) != -1) ? View.GONE : View.VISIBLE);
        } else if (resp.equals(Constants.LOGOUT_EVENT)) {
            verifyText.setVisibility(View.GONE);
        } else {
            progressDialog.dismiss();
            SnackbarHelper.showSnackbar(getContext(), parent, getString(R.string.call_not_made));
        }

    }

    @Subscribe
    public void onActivation(Boolean status) {
        progressDialog.dismiss();
        if (status.equals(Boolean.TRUE)) {
            SnackbarHelper.showSnackbar(getContext(), parent, getString(R.string.account_activated));
            Utils.saveAccountStatus(true, getContext());
            verifyText.setVisibility(View.GONE);
        } else {
            SnackbarHelper.showSnackbar(getContext(), parent, getString(R.string.activation_failed));
        }
    }

}
