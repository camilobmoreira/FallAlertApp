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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.adapter.ContactAdapter;
import br.com.aimcol.fallalertapp.model.Caregiver;
import br.com.aimcol.fallalertapp.model.Contact;
import br.com.aimcol.fallalertapp.model.ContactType;

public class NewCaregiverActivity extends AppCompatActivity {

    private Caregiver caregiver;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_new_caregiver);

        List<Contact> contactsList = new ArrayList<>();

        this.caregiver = new Caregiver();
        this.caregiver.setContacts(contactsList);
        this.gson = new Gson();


        ListView contactsListView = super.findViewById(R.id.contacts_list_view);
        contactsListView.setAdapter(new ContactAdapter(contactsList, this));

        Button addNewContactButton = super.findViewById(R.id.add_contact_button);
        addNewContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactsList.add(new Contact(ContactType.SMS, ""));
                ((BaseAdapter) contactsListView.getAdapter()).notifyDataSetChanged();
            }
        });



        Button saveCaregiverButton = super.findViewById(R.id.save_caregiver_button);
        saveCaregiverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = NewCaregiverActivity.super.findViewById(R.id.name_edit_text);
                caregiver.setName(nameEditText.getText().toString());

                Intent resultIntent = new Intent();
                resultIntent.putExtra(Caregiver.CAREGIVER_JSON, gson.toJson(caregiver));
                NewCaregiverActivity.super.setResult(Activity.RESULT_OK, resultIntent);
                NewCaregiverActivity.super.finish();
            }
        });
    }


}
