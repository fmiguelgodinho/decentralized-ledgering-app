package unl.fct.fgodinho.dslapp.network;

import android.os.AsyncTask;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import unl.fct.fgodinho.dslapp.util.IOUtil;

public class ApiHttpsRequest extends AsyncTask<Object, Void, ApiHttpsRequestResult> {

    private Exception ex = null;
    public ApiHttpsRequestDelegate delegate = null;

    public interface ApiHttpsRequestDelegate {
        void processFinish(ApiHttpsRequestResult output, Exception ex);
    }

    public ApiHttpsRequest(ApiHttpsRequestDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected ApiHttpsRequestResult doInBackground(Object... params) {

        SSLContext sslContext = (SSLContext) params[0];
        String smartHubUrl = (String) params[1];
        String requestMethod = (String) params[2];                 // GET/POST/...
        String requestParameters = null;
        if (params.length > 3) {
            requestParameters = (String) params[3];
        }

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
                ((HttpsURLConnection)urlConnection).setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        // do not verify hostnames, we will deploy this in IP addresses....
                        return true;
                    }
                });
                ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
            }

            // setup request
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setConnectTimeout(1500);
            urlConnection.setReadTimeout(15000);

            // body parameters
            if (requestParameters != null && !requestParameters.isEmpty() && requestMethod.equals("POST")) {
                byte[] requestParametersBytes = requestParameters.getBytes("UTF-8");
                OutputStream os = urlConnection.getOutputStream();
                os.write(requestParametersBytes);
                os.close();
            }

            responseCode = urlConnection.getResponseCode();
            result = IOUtil.readFully(urlConnection.getInputStream());
            responseContentType = urlConnection.getContentType();
            return new ApiHttpsRequestResult(responseCode, result, responseContentType);
        } catch(Exception e) {
            ex = e;
         } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(ApiHttpsRequestResult result) {
        if (ex != null) {
            ex.printStackTrace();
        }
        delegate.processFinish(result, ex);
    }

}
