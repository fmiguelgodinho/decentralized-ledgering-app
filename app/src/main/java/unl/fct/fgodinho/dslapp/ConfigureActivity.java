package unl.fct.fgodinho.dslapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigureActivity extends BaseActivity {

    private Button saveConfigBtn;
    private EditText editSmartHub, editChannelName, editContractId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editSmartHub = findViewById(R.id.smart_hub_address);
        editChannelName = findViewById(R.id.channel_name);
        editContractId = findViewById(R.id.contract_id);

        // if content exists, put in inputs and block
        if (smartHubUrl != null && channelName != null && contractId != null) {
            editSmartHub.setText(smartHubUrl);
            editChannelName.setText(channelName);
            editContractId.setText(contractId);
        }

        saveConfigBtn = findViewById(R.id.save_configuration);
        // set a listener to the btn
        saveConfigBtn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {

                if(TextUtils.isEmpty(editSmartHub.getText())) {
                    editSmartHub.setError("Fill in smart hub address!");
                    Toast.makeText(getApplicationContext(), "Smart Hub address is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editChannelName.getText())) {
                    editChannelName.setError("Fill in channel name!");
                    Toast.makeText(getApplicationContext(), "Channel name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(editContractId.getText())) {
                    editContractId.setError("Fill in contract ID!");
                    Toast.makeText(getApplicationContext(), "Contract ID is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // save configs
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("smartHubUrl", editSmartHub.getText().toString());
                editor.putString("channelName", editChannelName.getText().toString());
                editor.putString("contractId", editContractId.getText().toString());
                editor.apply();

                // enable tabs
                Menu menuNav = navigationView.getMenu();

                MenuItem menuItemNavContract = menuNav.findItem(R.id.navigation_contract);
                menuItemNavContract.setEnabled(true);

                MenuItem menuItemNavQueryInvoke = menuNav.findItem(R.id.navigation_query_invoke);
                menuItemNavQueryInvoke.setEnabled(true);

                // change to contract tab
                navigationView.setSelectedItemId(R.id.navigation_contract);
            }
        });
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_configure;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_configure;
    }

}
