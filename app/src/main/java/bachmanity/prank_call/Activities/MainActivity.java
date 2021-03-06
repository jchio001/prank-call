package bachmanity.prank_call.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import bachmanity.prank_call.Adapters.NavDrawerAdapter;
import bachmanity.prank_call.Fragments.HistoryFragment;
import bachmanity.prank_call.Fragments.HomeFragment;
import bachmanity.prank_call.Misc.Constants;
import bachmanity.prank_call.Misc.SPSingleton;
import bachmanity.prank_call.Misc.Utils;
import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.main_list_view) ListView mainList;
    @Bind(R.id.main_layout) CoordinatorLayout mainLayout;
    @Bind(R.id.login_text) TextView loginText;
    @Bind(R.id.adView) AdView adView;

    View lastSelectedView = null;
    NavDrawerAdapter adapter;
    ProgressDialog progressDialog;

    View firstListElem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter = new NavDrawerAdapter(getResources().getStringArray(R.array.main_icon_array),
                getResources().getStringArray(R.array.main_array), this);
        mainList.setAdapter(adapter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.home));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerStateChanged(int state) {
                Utils.hideKeyboard(mainLayout, getApplicationContext());
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment(),
                Constants.HOME_TAG).commit();


        final Context context = this;
        if (!Utils.getAccountSubStatus(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
            adView.resume();
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    progressDialog.dismiss();
                    if (!SPSingleton.getInstance(context).getSp().getBoolean(Constants.FIRST_TIME, false)) {
                        SPSingleton.getInstance(context).getSp().edit().putBoolean(Constants.FIRST_TIME, true).commit();
                        Intent intent = new Intent(context, RegisterActivity.class);
                        intent.putExtra(Constants.FIRST_TIME, true);
                        startActivityForResult(intent, 1);
                    }
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    progressDialog.dismiss();
                    super.onAdFailedToLoad(errorCode);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false);
                    builder.setTitle(getString(R.string.ad_dialog_title))
                            .setMessage(getString(R.string.ad_dailog_msg));

                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

                    builder.show();
                }

            });
        } else {
            //adViewDisabled = true;
            adView.setVisibility(View.GONE);
        }

        if (Utils.getId(this) != -1) {
            loginText.setText(getString(R.string.logout));
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.removeAllViews();
            adView.setAdListener(null);
            adView.destroy();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().findFragmentByTag(Constants.HOME_TAG) != null) {
            finish();
        } else {
            returnToHomeFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @OnItemClick(R.id.main_list_view)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        firstListElem = parent.getChildAt(0);
        if (lastSelectedView == null) {
            parent.getChildAt(0).setBackgroundColor(this.getResources().getColor(R.color.white));
        } else {
            lastSelectedView.setBackgroundColor(this.getResources().getColor(R.color.white));
        }

        lastSelectedView = view;
        view.setBackgroundColor(this.getResources().getColor(R.color.light_gray));
        if (id == 0) {
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag(Constants.HOME_TAG);
            if (myFragment != null) {
                drawer.closeDrawer(GravityCompat.START);
                return;
            } else {
                getSupportActionBar().setTitle(getString(R.string.home));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment(),
                        Constants.HOME_TAG).commit();
            }
        } else if (id == 1) {
            Fragment myFragment = getSupportFragmentManager().findFragmentByTag(Constants.HISTORY_TAG);
            if (myFragment != null) {
                drawer.closeDrawer(GravityCompat.START);
                return;
            } else {
                getSupportActionBar().setTitle(getString(R.string.history));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HistoryFragment(),
                        Constants.HISTORY_TAG).commit();
            }
        }

        drawer.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.login_text)
    public void onClick(TextView v) {
        drawer.closeDrawer(GravityCompat.START);
        if (v.getText().toString().equals(getString(R.string.login))) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, 1);
        } else if (v.getText().toString().equals(getString(R.string.logout))) {
            Utils.logout(this);
            v.setText(getString(R.string.login));
            returnToHomeFragment();
            EventBus.getDefault().post(Constants.LOGOUT_EVENT);

            if (adView.getVisibility() == View.GONE) {
                adView.setVisibility(View.VISIBLE);

                final Context context = this;
                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        progressDialog.dismiss();
                        super.onAdFailedToLoad(errorCode);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false);
                        builder.setTitle(getString(R.string.ad_dialog_title))
                                .setMessage(getString(R.string.ad_dailog_msg));

                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });

                        builder.show();
                    }

                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int id = intent.getIntExtra(Constants.ID, -1);
                if (id != -1) {

                    Utils.saveId(id, this);
                    Utils.saveAccountStatus(intent.getBooleanExtra(Constants.ACCOUNT_ACTIVE, false), this);

                    boolean isSubbed = intent.getBooleanExtra(Constants.ACCOUNT_SUBBED, false);
                    Utils.saveAccountSubStatus(isSubbed, this);
                    adView.setVisibility(isSubbed ? View.GONE : View.VISIBLE);
                    if (isSubbed && adView != null) {
                        //adViewDisabled = true;
                        adView.setAdListener(null);
                        adView.pause();
                    }

                    Utils.savePhoneNumber(intent.getStringExtra(Constants.NUMBER), this);
                    Utils.savePassword(intent.getStringExtra(Constants.PASSWORD), this);
                    returnToHomeFragment();
                    loginText.setText(getString(R.string.logout));
                    EventBus.getDefault().post(Constants.LOGIN_EVENT);
                }
            }
        }
    }

    public List<String> cacheContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.
                CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        int nameId = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int phoneNumberId = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        return null;
    }

    public void returnToHomeFragment() {
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag(Constants.HOME_TAG);
        if (myFragment != null) {
            if (Utils.getAccountSubStatus(this)) {
                adView.setVisibility(View.GONE);
            }
        } else {
            if (lastSelectedView != null && firstListElem != null) {
                lastSelectedView.setBackgroundColor(this.getResources().getColor(R.color.white));
                firstListElem.setBackgroundColor(this.getResources().getColor(R.color.light_gray));
            }
            lastSelectedView = null;
            getSupportActionBar().setTitle(getString(R.string.home));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment(),
                    Constants.HOME_TAG).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
