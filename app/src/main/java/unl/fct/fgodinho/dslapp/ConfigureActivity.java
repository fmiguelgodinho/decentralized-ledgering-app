package unl.fct.fgodinho.dslapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigureActivity extends BottomNavigationBaseActivity {

    private Button saveConfigBtn;
    private EditText editSmartHub, editChannelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editSmartHub = findViewById(R.id.smart_hub_address);
        editChannelName = findViewById(R.id.channel_name);
        saveConfigBtn = findViewById(R.id.save_configuration);

        // set a listener to the btn
        saveConfigBtn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {

                ;

                if(TextUtils.isEmpty(editSmartHub.getText())) {
                    //editSmartHub.setError("First name is required!");
                    Toast.makeText(getApplicationContext(), "Smart Hub address is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editChannelName.getText())) {
                    //editSmartHub.setError("First name is required!");
                    Toast.makeText(getApplicationContext(), "Channel name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("smartHubUrl", editSmartHub.getText().toString());
                editor.putString("channelName", editChannelName.getText().toString());
                editor.apply();
            }
        });

        // fetch possibly stored values from shared prefs
        String smartHubUrl = prefs.getString("smartHubUrl", null);
        String channelName = prefs.getString("channelName", null);

        // if content exists, put in inputs and block
        if (smartHubUrl != null && channelName != null) {
            editSmartHub.setText(smartHubUrl);
            editChannelName.setText(channelName);
        }
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
