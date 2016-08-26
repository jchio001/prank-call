package bachmanity.prank_call.Activities;

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
import bachmanity.prank_call.API.RetrofitSingleton;
import bachmanity.prank_call.API.Services.Callbacks.CreateAccountCallback;
import bachmanity.prank_call.Misc.Constants;
import bachmanity.prank_call.Misc.SnackbarHelper;
import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.parent) View parent;
    @Bind(R.id.password) TextView password;
    @Bind(R.id.phoneNumber) TextView phoneNumber;

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
            SnackbarHelper.showSnackbar(this, parent, Constants.FIRST_TIME_MSG);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.register_button1)
    public void onClick() {
        String enteredPass = password.getText().toString();
        if (enteredPass.length() < 8) {
            SnackbarHelper.showSnackbar(this, parent, Constants.TOO_SHORT);
        }
        else {
            //Toast.makeText(this, getMD5Hash(enteredPass), Toast.LENGTH_SHORT).show();
            Callback callback = new CreateAccountCallback();
            RetrofitSingleton.getInstance().getUserService().
                    createAccount(new AccountBundle(phoneNumber.getText().toString(), enteredPass))
                    .enqueue(callback);
        }

    }

    @Subscribe
    public void onEvent(Integer id) {
        if (id > 0) {
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
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
