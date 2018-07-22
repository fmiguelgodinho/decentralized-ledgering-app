package unl.fct.fgodinho.dslapp;

import android.os.Bundle;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

        // perform URL request to API
        String result = null;
        String responseContentType = null;
        int responseCode = -1;
        HttpURLConnection urlConnection = null;

        try {
            // setup https connection to smart hub
            URL requestedUrl = new URL(smartHubUrl);
            urlConnection = (HttpURLConnection) requestedUrl.openConnection();
            if(urlConnection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
            }

            // setup request
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(1500);


            responseCode = urlConnection.getResponseCode();
            result = IOUtil.readFully(urlConnection.getInputStream());
            responseContentType = urlConnection.getContentType();
        } catch(Exception ex) {
            result = ex.toString();
            Toast.makeText(getApplicationContext(), "Error performing HTTP request to " + smartHubUrl + ", " + result, Toast.LENGTH_LONG).show();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

}
