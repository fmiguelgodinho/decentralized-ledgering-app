package unl.fct.fgodinho.dslapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import unl.fct.fgodinho.dslapp.network.ApiHttpsRequest;
import unl.fct.fgodinho.dslapp.network.ApiHttpsRequestResult;
import unl.fct.fgodinho.dslapp.util.SigningUtil;

public class ContractActivity extends BaseActivity {

    private TextView contractView, contractHashView, acceptSignMsg;
    private Button acceptSignBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // get UI refs
        contractView = findViewById(R.id.contract_view);
        contractHashView = findViewById(R.id.contract_hash);
        acceptSignBtn = findViewById(R.id.accept_sign);
        acceptSignMsg = findViewById(R.id.accept_sign_msg);

        // set a listener to the btn
        acceptSignBtn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {


                progressBar.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(contractView.getText()) || TextUtils.isEmpty(contractHashView.getText())) {
                    Toast.makeText(getApplicationContext(), "No contract to accept!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String contractHashSignature = null;
                try {
                    contractHashSignature = new String(SigningUtil.sign(
                            contractHashView.getText().toString().trim().getBytes(),
                            privKey.getEncoded()
                    ), "UTF-8");

                    contractHashSignature = URLEncoder.encode(contractHashSignature, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(getApplicationContext(), "Error while signing contract!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                // send sign req
                String invocationUrl = "https://" + smartHubHostname + "/api/" + channelName + "/contract/" + contractId + "/sign";
                // fire an http request to the sh
                new ApiHttpsRequest(new ApiHttpsRequest.ApiHttpsRequestDelegate() {
                    @Override
                    public void processFinish(ApiHttpsRequestResult output, Exception ex) {
                        if (ex != null) {
                            Toast.makeText(getApplicationContext(), "Error performing HTTPS request to " + invocationUrl + ": " + ex.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                // parse sign result
                                JSONObject signResultRootJson = new JSONObject(output.getContent());
                                boolean signResult = signResultRootJson.getBoolean("result");

                                // act upon the result
                                if (signResult) {
                                    acceptSignBtn.setEnabled(false);
                                    acceptSignMsg.setText("Contract is signed.");
                                    acceptSignMsg.setTextColor(getResources().getColor(R.color.colorGreen));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error signing contract!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException je) {
                                Toast.makeText(getApplicationContext(), "Error interpreting JSON response: " + je.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                        progressBar.setVisibility(View.GONE);

                    }
                }).execute(sslContext, invocationUrl, "POST", "signature=" + contractHashSignature);
            }
        });
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


        progressBar.setVisibility(View.VISIBLE);
        String invocationUrl = "https://" + smartHubHostname + "/api/" + channelName + "/contract/" + contractId;

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

                        // display contract on view and enable accept button if unsigned
                        contractView.setText(contractJson.toString(2));
                        contractHashView.setText(contractHash);

                        if (contractIsSigned) {
                            acceptSignBtn.setEnabled(false);
                            acceptSignMsg.setText("Contract is signed.");
                            acceptSignMsg.setTextColor(getResources().getColor(R.color.colorGreen));
                        }

                    } catch (JSONException je) {
                        Toast.makeText(getApplicationContext(), "Error interpreting JSON response: " + je.toString(), Toast.LENGTH_LONG).show();
                    }

                    progressBar.setVisibility(View.GONE);
                }

            }
        }).execute(sslContext, invocationUrl, "GET");
    }

}
