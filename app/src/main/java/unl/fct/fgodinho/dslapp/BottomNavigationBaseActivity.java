package unl.fct.fgodinho.dslapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class BottomNavigationBaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;
    protected SharedPreferences prefs;

    protected String smartHubUrl, channelName, contractId;

    public static final String CONFIGURATION_SETTINGS = "DSL_CONFIGURATION_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        // load share prefs
        prefs = getSharedPreferences(CONFIGURATION_SETTINGS, MODE_PRIVATE);

        // fetch possibly stored values from shared prefs
        smartHubUrl = prefs.getString("smartHubUrl", null);
        channelName = prefs.getString("channelName", null);
        contractId = prefs.getString("contractId", null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_configure) {
                startActivity(new Intent(this, ConfigureActivity.class));
            } else if (itemId == R.id.navigation_contract) {
                startActivity(new Intent(this, ContractActivity.class));
            } else if (itemId == R.id.navigation_query_invoke) {
                startActivity(new Intent(this, QueryInvokeActivity.class));
            }
            finish();
        }, 300);
        return true;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

}