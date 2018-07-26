package unl.fct.fgodinho.dslapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected SSLContext sslContext;
    protected SharedPreferences prefs;
    protected BottomNavigationView navigationView;
    protected ProgressBar progressBar;
    protected Key privKey;

    protected String smartHubHostname, channelName, contractId;

    public static final String CONFIGURATION_SETTINGS = "DSL_CONFIGURATION_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        // load share prefs
        prefs = getSharedPreferences(CONFIGURATION_SETTINGS, MODE_PRIVATE);

        // load client cert
        try {
            // fetch p12 keystore from assets and load it up (as a x509 cert)
            InputStream is = getApplicationContext().getAssets().open("clientkeystore.p12");
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(is, "sparkmeup".toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(keyStore, "sparkmeup".toCharArray());
            KeyManager[] keyManagers = kmf.getKeyManagers();

            // store priv key for later
            privKey = keyStore.getKey("client-cert", "sparkmeup".toCharArray());

            // load truststore from android storage
            is = getApplicationContext().getAssets().open("clienttruststore.p12");
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            trustStore.load(is, "sparkmeup".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(trustStore);
            TrustManager[] trustManagers = tmf.getTrustManagers();

            // setup ssl context
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());

        } catch (IOException | CertificateException | UnrecoverableKeyException
                | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {

            Toast.makeText(getApplicationContext(), "Error loading certificate!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // setup navigation
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        // fetch possibly stored values from shared prefs
        smartHubHostname = prefs.getString("smartHubHostname", null);
        channelName = prefs.getString("channelName", null);
        contractId = prefs.getString("contractId", null);

        // get progress bar ref
        progressBar = findViewById(R.id.progress_bar);

        // if any of these does not exist, disable other tabs (as they will make url requests)
        if (smartHubHostname == null || channelName == null || contractId == null) {

            Menu menuNav = navigationView.getMenu();

            MenuItem menuItemNavContract = menuNav.findItem(R.id.navigation_contract);
            menuItemNavContract.setEnabled(false);

            MenuItem menuItemNavQueryInvoke = menuNav.findItem(R.id.navigation_query_invoke);
            menuItemNavQueryInvoke.setEnabled(false);
        }
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

            progressBar.setVisibility(View.VISIBLE);
            if (itemId == R.id.navigation_configure) {
                startActivity(new Intent(this, ConfigureActivity.class));
            } else if (itemId == R.id.navigation_contract) {
                startActivity(new Intent(this, ContractActivity.class));
            } else if (itemId == R.id.navigation_query_invoke) {
                startActivity(new Intent(this, QueryInvokeActivity.class));
            }
            finish();
        }, 0);
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