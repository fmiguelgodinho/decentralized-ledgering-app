package unl.fct.fgodinho.dslapp;

import android.os.Bundle;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import unl.fct.fgodinho.dslapp.network.FireHTTPSRequestTask;
import unl.fct.fgodinho.dslapp.util.IOUtil;

public class ContractActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    int getContentViewId() {
        return R.layout.activity_contract;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_contract;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // fire an http request to the sh
        new FireHTTPSRequestTask().execute(sslContext, smartHubUrl);
    }

}
