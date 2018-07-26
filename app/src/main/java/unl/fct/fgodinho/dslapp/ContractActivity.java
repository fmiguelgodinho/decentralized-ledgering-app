package unl.fct.fgodinho.dslapp;

import android.os.Bundle;
import android.widget.Toast;

import unl.fct.fgodinho.dslapp.network.ApiHttpsRequest;
import unl.fct.fgodinho.dslapp.network.ApiHttpsRequestResult;

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

        String invocationUrl = smartHubUrl + "/" + channelName + "/contract/" + contractId;

        // fire an http request to the sh
        new ApiHttpsRequest(new ApiHttpsRequest.ApiHttpsRequestDelegate() {
            @Override
            public void processFinish(ApiHttpsRequestResult output, Exception ex) {
                if (ex != null) {
                    Toast.makeText(getApplicationContext(), "Error performing HTTPS request to " + invocationUrl + ": " + ex.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }
        ).execute(sslContext, invocationUrl);
    }

}
