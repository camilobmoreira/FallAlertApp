package br.com.aimcol.fallalertapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.adapter.CaregiverAdapter;
import br.com.aimcol.fallalertapp.model.Caregiver;
import br.com.aimcol.fallalertapp.model.Elderly;

public class NewElderlyActivity extends AppCompatActivity {

    private Elderly elderly;
    private Gson gson;
    private ListView caregiverListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_new_elderly);

        // Initiate variables
        this.gson = new Gson();
        Intent intent = super.getIntent();
        if (this.elderly == null) {
            String elderlyJson = intent.getStringExtra(Elderly.ELDERLY_JSON);
            this.elderly = this.gson.fromJson(elderlyJson, Elderly.class);
        }

        EditText nameEditText = super.findViewById(R.id.name_edit_text);
        nameEditText.setText(this.elderly.getName());

//        EditText birthEditText = super.findViewById(R.id.birth_edit_text);
//        birthEditText.setText(this.elderly.getBirthDate());

        Button addNewCaregiverButton = super.findViewById(R.id.add_caregiver_button);
        addNewCaregiverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newCaregiverIntent = new Intent(NewElderlyActivity.this, NewCaregiverActivity.class);
                NewElderlyActivity.super.startActivityForResult(newCaregiverIntent, 1);
            }
        });

        Button saveElderlyButton = super.findViewById(R.id.save_elderly_button);
        saveElderlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Elderly.ELDERLY_JSON, NewElderlyActivity.this.gson.toJson(NewElderlyActivity.this.elderly));
                NewElderlyActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                NewElderlyActivity.this.finish();
            }
        });

        List<Caregiver> caregiverList = this.elderly.getCaregivers();
        if (CollectionUtils.isEmpty(caregiverList)) {
            caregiverList = new ArrayList<>();
            this.elderly.setCaregivers(caregiverList);
        }
        this.caregiverListView = super.findViewById(R.id.caregiver_list_view);
        this.caregiverListView.setAdapter(new CaregiverAdapter(caregiverList, this));
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            String caregiverJson = data.getStringExtra(Caregiver.CAREGIVER_JSON);
            this.elderly.getCaregivers().add(this.gson.fromJson(caregiverJson, Caregiver.class));
            ((BaseAdapter) this.caregiverListView.getAdapter()).notifyDataSetChanged();
        }
    }
    
}
