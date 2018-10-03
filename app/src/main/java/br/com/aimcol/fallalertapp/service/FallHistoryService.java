package br.com.aimcol.fallalertapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import br.com.aimcol.fallalertapp.model.FallHistory;

public class FallHistoryService extends IntentService {

    public static final String FALL_HISTORY_ACTION_SAVE = "FallHistory#save";
    public static final String FALL_HISTORY_ACTION_DELETE = "FallHistory#delete";
    public static final String FALL_HISTORY_ACTION_UPDATE = "FallHistory#update";
    public static final String FALL_HISTORY_ACTION_LOAD_BY_KEY = "FallHistory#loadByKey";
    public static final String FALL_HISTORY_ACTION_LOAD_BY_ELDERLY_KEY = "FallHistory#loadByElderlyKey";

    private DatabaseReference mDatabase;
    private Gson gson;

    public FallHistoryService() {
        super(".FallHistoryService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FallHistoryService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(@Nullable Intent intent,
                              int flags,
                              int startId) {

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.gson = new Gson();

        String fallHistoryJson = intent.getStringExtra(FallHistory.FALL_HISTORY_JSON);
        String action = intent.getStringExtra("action");
        FallHistory fallHistory = this.gson.fromJson(fallHistoryJson, FallHistory.class);


        //CrudAction action = (CrudAction) intent.getSerializableExtra("crudAction");
        switch (action) {
            case FALL_HISTORY_ACTION_SAVE:
                this.save(fallHistory);
                break;
            case FALL_HISTORY_ACTION_LOAD_BY_KEY:
                this.loadByKey(fallHistory.getKey());
                break;
            case FALL_HISTORY_ACTION_LOAD_BY_ELDERLY_KEY:
                this.loadByElderlyKey(fallHistory.getElderly().getKey());
                break;
            case FALL_HISTORY_ACTION_UPDATE:
                this.update(fallHistory);
                break;
            case FALL_HISTORY_ACTION_DELETE:
                //this.delete(fallHistory);
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void loadByElderlyKey(String key) {
        this.mDatabase.child("fall_history").orderByChild("elderly").orderByChild("key").equalTo(key).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object value = snapshot.getValue();
                    if (value != null) {
                        Intent loadResponseIntent =  new Intent();
                        loadResponseIntent.setAction(FALL_HISTORY_ACTION_LOAD_BY_ELDERLY_KEY);
                        loadResponseIntent.putExtra(FallHistory.FALL_HISTORY_JSON, value.toString());
                        FallHistoryService.this.sendBroadcast(loadResponseIntent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadByKey(String key) {
        this.mDatabase.child("fall_history").orderByChild("key").equalTo(key).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object value = snapshot.getValue();
                    if (value != null) {
                        Intent loadResponseIntent =  new Intent();
                        loadResponseIntent.setAction(FALL_HISTORY_ACTION_LOAD_BY_KEY);
                        loadResponseIntent.putExtra(FallHistory.FALL_HISTORY_JSON, value.toString());
                        FallHistoryService.this.sendBroadcast(loadResponseIntent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void save(FallHistory fallHistory) {
        String key = this.mDatabase.child("fall_history").push().getKey();
        fallHistory.setKey(key);
        this.update(fallHistory);
    }

    private void update(FallHistory fallHistory) {
        Task<Void> task = this.mDatabase.child("fall_history").child(fallHistory.getKey()).setValue(fallHistory);

        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FallHistoryService.this, "Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FallHistoryService.this, "Failed", Toast.LENGTH_LONG).show();
                    Log.e("update", task.getException().getMessage());
                }
            }
        });

        Intent loadResponseIntent =  new Intent();
        loadResponseIntent.setAction(FALL_HISTORY_ACTION_UPDATE);
        loadResponseIntent.putExtra(FallHistory.FALL_HISTORY_JSON, this.gson.toJson(fallHistory));
        FallHistoryService.this.sendBroadcast(loadResponseIntent);
    }

    private void delete(FallHistory fallHistory) {
        Task<Void> task = this.mDatabase.child("fall_history").child(fallHistory.getKey()).removeValue();

        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FallHistoryService.this, "Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FallHistoryService.this, "Failed", Toast.LENGTH_LONG).show();
                    Log.e("update", task.getException().getMessage());
                }
            }
        });
    }

    public static void startFallHistoryService(String fallHistoryJson,
                                        String action,
                                        Context context) {

        Intent userServiceIntent = new Intent(context, FallHistory.class);
        userServiceIntent.putExtra(FallHistory.FALL_HISTORY_JSON, fallHistoryJson);
        userServiceIntent.putExtra("action", action);
        context.startService(userServiceIntent);
    }
}
