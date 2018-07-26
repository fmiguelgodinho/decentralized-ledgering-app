package unl.fct.fgodinho.dslapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

        String invocationUrl = smartHubUrl + "/api/" + channelName + "/contract/" + contractId;

        // fire an http request to the sh
        new ApiHttpsRequest(new ApiHttpsRequest.ApiHttpsRequestDelegate() {
            @Override
            public void processFinish(ApiHttpsRequestResult output, Exception ex) {
                if (ex != null) {
                    Toast.makeText(getApplicationContext(), "Error performing HTTPS request to " + invocationUrl + ": " + ex.toString(), Toast.LENGTH_LONG).show();
                } else {

                    try {

                        // parse contract
                        JSONObject contractRootJson = new JSONObject(output.getContent());
                        JSONObject contractJson = new JSONObject(contractRootJson.getString("contract"));
                        String contractHash = contractRootJson.getString("hash");
                        boolean contractIsSigned = contractRootJson.getBoolean("is-signed");

                        // display contract on view
                        TextView contractView = findViewById(R.id.contract_view);
                        TextView contractHashView = findViewById(R.id.contract_hash);
                        contractView.setText(contractJson.toString(2));
                        contractHashView.setText(contractHash);

                        if (contractIsSigned) {

                        }

                    } catch (JSONException je) {
                        Toast.makeText(getApplicationContext(), "Error interpreting JSON response: " + je.toString(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
        ).execute(sslContext, invocationUrl);
    }

}
