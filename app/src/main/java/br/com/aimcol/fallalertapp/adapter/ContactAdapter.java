package br.com.aimcol.fallalertapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Contact;
import br.com.aimcol.fallalertapp.model.ContactType;

public class ContactAdapter extends BaseAdapter implements ListAdapter {

    private List<Contact> list;
    private Context context;

    public ContactAdapter(List<Contact> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int pos) {
        return this.list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_contact, null);
        }

        Spinner contactTypeSpinner = (Spinner) view.findViewById(R.id.contact_type_spinner);
        List<ContactType> contactTypes = new ArrayList<>();
        for (ContactType ct: ContactType.values()) {
            contactTypes.add(ct);
        }
        ArrayAdapter dataAdapter = new ArrayAdapter(this.context, R.layout.support_simple_spinner_dropdown_item, contactTypes);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        contactTypeSpinner.setAdapter(dataAdapter);

        Contact contact = list.get(position);
        contact.setType((ContactType) contactTypeSpinner.getSelectedItem());

        EditText contactEditText = (EditText) view.findViewById(R.id.contact_edit_text);
        contactEditText.setText(contact.getContact());



        Button deleteBtn = (Button)view.findViewById(R.id.delete_contact_button);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
