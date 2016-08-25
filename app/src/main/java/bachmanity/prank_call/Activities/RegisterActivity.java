package bachmanity.prank_call.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import bachmanity.prank_call.Misc.Constants;
import bachmanity.prank_call.Misc.SnackbarHelper;
import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static bachmanity.prank_call.Misc.Utils.getMD5Hash;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.parent) View parent;
    @Bind(R.id.password) TextView password;

    boolean isFirstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = this.getIntent();
        isFirstTime = intent.getBooleanExtra(Constants.FIRST_TIME, false);
        if (isFirstTime) {
            SnackbarHelper.showSnackbar(this, parent, Constants.FIRST_TIME_MSG);
        }
    }

    @OnClick(R.id.register_button1)
    public void onClick() {
        String enteredPass = password.getText().toString();
        if (enteredPass.length() < 8) {
            SnackbarHelper.showSnackbar(this, parent, Constants.TOO_SHORT);
        }
        else {
            Toast.makeText(this, getMD5Hash(enteredPass), Toast.LENGTH_SHORT).show();
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
