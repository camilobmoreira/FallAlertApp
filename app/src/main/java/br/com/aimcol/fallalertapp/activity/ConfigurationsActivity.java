package br.com.aimcol.fallalertapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.util.MapUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Configuration;
import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.model.Person;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.util.RuntimeTypeAdapterFactory;

public class ConfigurationsActivity extends Activity {

    public static final int REQUEST_CODE = ((Integer) ConfigurationsActivity.class.hashCode()).shortValue();

    private Gson gson;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_configurations);

        // Initiate variables
        RuntimeTypeAdapterFactory<Person> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Person.class, "type")
                .registerSubtype(Elderly.class, Elderly.class.getSimpleName());
        this.gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();

        Intent intent = super.getIntent();
        String userJson = null;
        if (this.user == null) {
            userJson = intent.getStringExtra(User.USER_JSON);
            this.user = this.gson.fromJson(userJson, User.class);
        }

        ListView configurationsList = super.findViewById(R.id.configurations_list);

        Map<Configuration, Object> userConfigurations = this.user.getConfigurations();

        for (Configuration configuration : Configuration.values()) {
            EditText editText = new EditText(this);
            editText.setId(configuration.hashCode());
            editText.setHint(configuration.getText());
            if (userConfigurations != null) {
                Object configValue = userConfigurations.get(configuration);
                editText.setText(configValue != null ? configValue.toString() : null);
            }
            //fixme Caused by: java.lang.UnsupportedOperationException: addView(View) is not supported in AdapterView
            configurationsList.addView(editText);
        }
    }
}
