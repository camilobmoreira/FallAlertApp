package br.com.aimcol.fallalertapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Configuration;
import br.com.aimcol.fallalertapp.model.Contact;
import br.com.aimcol.fallalertapp.model.ContactType;
import br.com.aimcol.fallalertapp.model.UserConfiguration;

public class ConfigurationsAdapter extends BaseAdapter implements ListAdapter {

    private List<UserConfiguration> configurations;
    private Context context;

    public ConfigurationsAdapter(List<UserConfiguration> configurations, Context context) {
        this.configurations = configurations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.configurations.size();
    }

    @Override
    public Object getItem(int position) {
        return this.configurations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_configurations, null);
        }

        UserConfiguration userConfiguration = this.configurations.get(position);

        TextView configurationTextView = view.findViewById(R.id.configuration_name_text_view);
        configurationTextView.setText(userConfiguration.getConfiguration().toString());

        EditText configurationEditText = view.findViewById(R.id.configuration_edit_text_view);
        configurationEditText.setText(userConfiguration.getValue().toString());

        return view;
    }
}
