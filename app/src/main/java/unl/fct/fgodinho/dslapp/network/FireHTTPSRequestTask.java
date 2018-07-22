package unl.fct.fgodinho.dslapp.network;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import unl.fct.fgodinho.dslapp.util.IOUtil;

public class FireHTTPSRequestTask extends AsyncTask<Object, Void, HTTPSResult> {

    private Exception ex = null;

    @Override
    protected HTTPSResult doInBackground(Object... params) {

        SSLContext sslContext = (SSLContext) params[0];
        String smartHubUrl = (String) params[1];

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
            return new HTTPSResult(responseCode, result, responseContentType);
        } catch(Exception e) {
            ex = e;
            //Toast.makeText(getApplicationContext(), "Error performing HTTP request to " + smartHubUrl + ", " + result, Toast.LENGTH_LONG).show();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(HTTPSResult result) {
        if (ex != null) {
            ex.printStackTrace();
        }
    }

}
