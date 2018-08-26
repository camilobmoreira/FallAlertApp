package br.com.aimcol.fallalertapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Caregiver;
import br.com.aimcol.fallalertapp.model.ContactType;
import br.com.aimcol.fallalertapp.service.ElderlyService;

public class NewCaregiverActivity extends AppCompatActivity {

    private Caregiver caregiver;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_caregiver);

        this.caregiver = new Caregiver();
        this.gson = new Gson();




        Button saveElderlyButton = (Button) super.findViewById(R.id.save_elderly_button);
        saveElderlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
                caregiver.setName(nameEditText.getText().toString());

                Map<ContactType, String> contacts = new HashMap<>();

                caregiver.setContacts(contacts);
            }
        });
    }


}
