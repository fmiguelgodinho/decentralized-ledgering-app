package unl.fct.fgodinho.dslapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import unl.fct.fgodinho.dslapp.network.ApiHttpsRequest;
import unl.fct.fgodinho.dslapp.network.ApiHttpsRequestResult;

public class QueryInvokeActivity extends BaseActivity {

    private Spinner queryInvokeCombo;
    private TextView resultContainerView, signaturesContainerView;
    private EditText editFunctionId, editFunctionParameters;
    private Button queryInvokeBtn;

    private int selectedOpType;

    private static final String[] opTypes = {"query", "invoke"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get UI refs
        queryInvokeCombo = findViewById(R.id.op_type);
        editFunctionId = findViewById(R.id.function_id);
        editFunctionParameters = findViewById(R.id.function_parameters);
        resultContainerView = findViewById(R.id.result_container);
        signaturesContainerView = findViewById(R.id.signatures_container);
        queryInvokeBtn = findViewById(R.id.invoke);

        // setup combo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(QueryInvokeActivity.this, android.R.layout.simple_spinner_item, opTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        queryInvokeCombo.setAdapter(adapter);
        queryInvokeCombo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedOpType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedOpType = 0; // query by default
            }
        });

        // set a listener to the btn
        queryInvokeBtn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {

                if(TextUtils.isEmpty(editFunctionId.getText())) {
                    Toast.makeText(getApplicationContext(), "Function cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String fnId = editFunctionId.getText().toString();

                // get parameters if they exist
                String fnParameters = "";
                if (!TextUtils.isEmpty(editFunctionParameters.getText())) {
                    fnParameters = editFunctionParameters.getText().toString();
                }


                // send sign req
                String invocationUrl = "https://" + smartHubHostname + "/api/" + channelName + "/contract/" + contractId + "/"
                        + (selectedOpType == 0? "query" : "invoke");
                // fire an http request to the sh
                new ApiHttpsRequest(new ApiHttpsRequest.ApiHttpsRequestDelegate() {
                    @Override
                    public void processFinish(ApiHttpsRequestResult output, Exception ex) {
                        if (ex != null) {
                            Toast.makeText(getApplicationContext(), "Error performing HTTPS request to " + invocationUrl + ": " + ex.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                // parse operation result
                                JSONObject opResultRootJson = new JSONObject(output.getContent());

                                // act upon result
                                if (opResultRootJson.has("error")) {
                                    // an error occurred
                                    resultContainerView.setText("An error occurred: " + opResultRootJson.getString("error"));
                                } else {
                                    String opResult = opResultRootJson.getString("result");
                                    JSONArray signaturesResult = opResultRootJson.getJSONArray("signatures");
                                    resultContainerView.setText(opResult);
                                    signaturesContainerView.setText(signaturesResult.toString());
                                }
                            } catch (JSONException je) {
                                Toast.makeText(getApplicationContext(), "Error interpreting JSON response: " + je.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }).execute(sslContext, invocationUrl, "POST", "operationId=" + fnId + "&operationArgs=" + fnParameters);
            }
        });
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_query_invoke;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_query_invoke;
    }



}
