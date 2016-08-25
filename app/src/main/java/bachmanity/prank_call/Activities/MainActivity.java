package bachmanity.prank_call.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import bachmanity.prank_call.Adapters.NavDrawerAdapter;
import bachmanity.prank_call.Misc.Constants;
import bachmanity.prank_call.Misc.SPSingleton;
import bachmanity.prank_call.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.main_list_view) ListView mainList;

    View lastSelectedView = null;
    NavDrawerAdapter adapter;

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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (!SPSingleton.getInstance(this).getSp().getBoolean(Constants.FIRST_TIME, false)) {
            SPSingleton.getInstance(this).getSp().edit().putBoolean(Constants.FIRST_TIME, true).commit();
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra(Constants.FIRST_TIME, true);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        if (lastSelectedView == null) {
            parent.getChildAt(0).setBackgroundColor(this.getResources().getColor(R.color.white));
        }
        else {
            lastSelectedView.setBackgroundColor(this.getResources().getColor(R.color.white));
        }

        lastSelectedView = view;
        view.setBackgroundColor(this.getResources().getColor(R.color.light_gray));
        drawer.closeDrawer(GravityCompat.START);

        Toast.makeText(this, Integer.toString(position), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.login_text)
    public void onClick() {
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        //Need noanim translation since there's a default animation
        //overridePendingTransition(R.anim.slideleftin, R.anim.noanim);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
