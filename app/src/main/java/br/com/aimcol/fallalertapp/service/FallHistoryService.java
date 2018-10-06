package br.com.aimcol.fallalertapp.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
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
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.aimcol.fallalertapp.dto.ElderlyDTO;
import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.model.Fall;
import br.com.aimcol.fallalertapp.model.FallHistory;
import br.com.aimcol.fallalertapp.model.Person;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.util.BroadcastReceiverUtils;
import br.com.aimcol.fallalertapp.util.RuntimeTypeAdapterFactory;

public class FallHistoryService extends IntentService {

    public static final String FALL_HISTORY_ACTION_SAVE = "FallHistory#save";
    public static final String FALL_HISTORY_ACTION_DELETE = "FallHistory#delete";
    public static final String FALL_HISTORY_ACTION_UPDATE = "FallHistory#update";
    public static final String FALL_HISTORY_ACTION_LOAD_BY_KEY = "FallHistory#loadByKey";
    public static final String FALL_HISTORY_ACTION_LOAD_BY_ELDERLY_KEY = "FallHistory#loadByElderlyUserKey";
    public static final String FALL_HISTORY_ACTION_REGISTER_NEW_FALL = "FallHistory#registerNewFall";

    private BroadcastReceiver mBroadcastReceiver;
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

        RuntimeTypeAdapterFactory<Person> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Person.class, "type")
                .registerSubtype(Elderly.class, Elderly.class.getSimpleName());
        this.gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();

        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        String userJson = intent.getStringExtra(User.USER_JSON);
        String action = intent.getStringExtra("action");
        User user = this.gson.fromJson(userJson, User.class);


        //CrudAction action = (CrudAction) intent.getSerializableExtra("crudAction");
        switch (action) {
            case FALL_HISTORY_ACTION_REGISTER_NEW_FALL:
                this.registerNewFall(user);
                break;
//            case FALL_HISTORY_ACTION_SAVE:
//                this.save(user);
//                break;
//            case FALL_HISTORY_ACTION_LOAD_BY_KEY:
//                this.loadByKey(user.getKey());
//                break;
//            case FALL_HISTORY_ACTION_LOAD_BY_ELDERLY_KEY:
//                this.loadByElderlyUserKey(user.getElderly().getKey());
//                break;
//            case FALL_HISTORY_ACTION_UPDATE:
//                this.update(user);
//                break;
//            case FALL_HISTORY_ACTION_DELETE:
//                this.delete(user);
//                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void registerNewFall(User user) {

        this.mBroadcastReceiver = new BroadcastReceiver() {
            //fixme it's being called a lot of times
            @Override
            public void onReceive(Context context, Intent intent) {
                //fixme change to a better location
                String fallHistoryJson = intent.getStringExtra(FallHistory.FALL_HISTORY_JSON);
                FallHistory fallHistory = null;
                if (!fallHistoryJson.equals("null")) {
                    fallHistory = FallHistoryService.this.gson.fromJson(fallHistoryJson, FallHistory.class);
                }

                if (fallHistory == null) {
                    fallHistory = FallHistoryService.this.newFallHistory(user);
                    FallHistoryService.this.save(fallHistory);
                } else {
                    List<Fall> falls = fallHistory.getFalls();
                    falls.add(FallHistoryService.this.newFall());
                    fallHistory.setFalls(falls);
                    FallHistoryService.this.update(fallHistory);
                }
            }
        };

        BroadcastReceiverUtils.registerBroadcastReceiver(this, this.mBroadcastReceiver, FallHistoryService.FALL_HISTORY_ACTION_LOAD_BY_ELDERLY_KEY);
        this.loadByElderlyUserKey(user.getKey());
    }

    @NonNull
    private Fall newFall() {
        Fall fall = new Fall();
//        fall.setDate(new Date());
        fall.setLatitude(0.0);
        fall.setLongitude(0.0);
        return fall;
    }

    private FallHistory newFallHistory(User user) {
        FallHistory fallHistory;
        fallHistory = new FallHistory();
        fallHistory.setElderly(new ElderlyDTO(user.getKey(), user.getPerson().getName()));
        Fall fall = this.newFall();
        List<Fall> falls = new ArrayList<>();
        falls.add(fall);
        fallHistory.setFalls(falls);

        return fallHistory;
    }

    private void loadByElderlyUserKey(String key) {
        //fixme it's being called a lot of times
        this.mDatabase.child("fall_history").orderByChild("elderly/key").equalTo(key).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent loadResponseIntent = new Intent();
                loadResponseIntent.setAction(FallHistoryService.FALL_HISTORY_ACTION_LOAD_BY_ELDERLY_KEY);
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Object value = snapshot.getValue();
                        if (value != null) {
                            loadResponseIntent.putExtra(FallHistory.FALL_HISTORY_JSON, value.toString());
                        }
                    }
                } else {
                    loadResponseIntent.putExtra(FallHistory.FALL_HISTORY_JSON, "null");
                }
                FallHistoryService.this.sendBroadcast(loadResponseIntent);
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

    public static void startFallHistoryService(String userJson,
                                        String action,
                                        Context context) {

        Intent userServiceIntent = new Intent(context, FallHistoryService.class);
        userServiceIntent.putExtra(User.USER_JSON, userJson);
        userServiceIntent.putExtra("action", action);
        context.startService(userServiceIntent);
    }
}
