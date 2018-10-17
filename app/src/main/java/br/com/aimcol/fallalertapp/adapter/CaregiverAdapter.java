package br.com.aimcol.fallalertapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.Caregiver;

public class CaregiverAdapter extends BaseAdapter implements ListAdapter {

    private List<Caregiver> list;
    private Context context;

    public CaregiverAdapter(List<Caregiver> list, Context context) {
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
            view = inflater.inflate(R.layout.layout_caregiver, null);
        }

        Caregiver caregiver = this.list.get(position);

        TextView caregiverNameEditText = view.findViewById(R.id.caregiver_name_text_view);
        caregiverNameEditText.setText(caregiver.getName());

        TextView contactEditText = view.findViewById(R.id.caregiver_contacts_text_view);
        contactEditText.setText(caregiver.getContacts().toString());

        Button deleteBtn = view.findViewById(R.id.delete_caregiver_button);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CaregiverAdapter.this.list.remove(position);
                CaregiverAdapter.super.notifyDataSetChanged();
            }
        });

        return view;
    }
}
