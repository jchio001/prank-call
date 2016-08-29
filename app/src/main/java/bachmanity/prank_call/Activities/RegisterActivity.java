package bachmanity.prank_call.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import bachmanity.prank_call.API.Models.AccountBundle;
import bachmanity.prank_call.API.Models.LoginRespBundle;
import bachmanity.prank_call.API.RetrofitSingleton;
import bachmanity.prank_call.API.Services.Callbacks.CreateAccountCallback;
import bachmanity.prank_call.API.Services.Callbacks.LoginCallback;
import bachmanity.prank_call.Misc.Constants;
import bachmanity.prank_call.Misc.SnackbarHelper;
import bachmanity.prank_call.Misc.Utils;
import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.parent) View parent;
    @Bind(R.id.password) TextView password;
    @Bind(R.id.phoneNumber) TextView phoneNumber;

    ProgressDialog  progressDialog;
    boolean isFirstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventBus.getDefault().register(this);
        Intent intent = this.getIntent();
        isFirstTime = intent.getBooleanExtra(Constants.FIRST_TIME, false);
        if (isFirstTime) {
            getSupportActionBar().setTitle(getString(R.string.register));
            SnackbarHelper.showSnackbar(this, parent, Constants.FIRST_TIME_MSG);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.register_button1, R.id.login_button})
    public void onClick(View v) {
        String number = phoneNumber.getText().toString();
        String enteredPass = password.getText().toString();
        if (number.length() < 10) {
            SnackbarHelper.showSnackbar(this, parent, Constants.NUMBER_INVALID);
            return;
        }
        if (enteredPass.length() < 8) {
            SnackbarHelper.showSnackbar(this, parent, Constants.PASS_TOO_SHORT);
            return;
        }
        else {
            Utils.hideKeyboard(parent, this);
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            if (v.getId() == R.id.register_button1) {
                progressDialog.setMessage(Constants.REGISTERING);
                progressDialog.show();
                Callback callback = new CreateAccountCallback();
                RetrofitSingleton.getInstance().getUserService()
                        .createAccount(new AccountBundle(number, Utils.getMD5Hash(enteredPass)))
                        .enqueue(callback);
            }
            else if (v.getId() == R.id.login_button) {
                progressDialog.setMessage(Constants.LOGGING_IN);
                progressDialog.show();
                Callback callback = new LoginCallback();
                RetrofitSingleton.getInstance().getUserService()
                        .login(new AccountBundle(number, Utils.getMD5Hash(enteredPass)))
                        .enqueue(callback);
            }
        }

    }

    @Subscribe
    public void onEvent(Integer id) {
        progressDialog.dismiss();
        if (id > 0) {
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        }
        else {
            SnackbarHelper.showSnackbar(this, parent, Constants.REGISTER_FAILURE);
        }

    }

    @Subscribe
    public void loginResp(LoginRespBundle resp) {
        progressDialog.dismiss();
        int id = resp.getId();
        if (id > 0) {
            Intent intent = new Intent();
            intent.putExtra(Constants.ID, id);
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            SnackbarHelper.showSnackbar(this, parent, Constants.LOGIN_FAILURE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
