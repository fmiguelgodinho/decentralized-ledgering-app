package unl.fct.fgodinho.dslapp;

import android.os.Bundle;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

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
//        String result = null;
//        HttpURLConnection urlConnection = null;
//
//        try {
//            URL requestedUrl = new URL(url);
//            urlConnection = (HttpURLConnection) requestedUrl.openConnection();
//            if(urlConnection instanceof HttpsURLConnection) {
//                ((HttpsURLConnection)urlConnection)
//                        .setSSLSocketFactory(sslContext.getSocketFactory());
//            }
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setConnectTimeout(1500);
//            urlConnection.setReadTimeout(1500);
//            lastResponseCode = urlConnection.getResponseCode();
//            result = IOUtil.readFully(urlConnection.getInputStream());
//            lastContentType = urlConnection.getContentType();
//        } catch(Exception ex) {
//            result = ex.toString();
//        } finally {
//            if(urlConnection != null) {
//                urlConnection.disconnect();
//            }
//        }
    }

}
