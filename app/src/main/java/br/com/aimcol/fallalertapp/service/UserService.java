package br.com.aimcol.fallalertapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.model.Person;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.util.CrudAction;
import br.com.aimcol.fallalertapp.util.RuntimeTypeAdapterFactory;

public class UserService extends IntentService {

    public static final String USER_SERVICE_ACTION_LOAD = "UserService#load";
    private DatabaseReference mDatabase;
    private Gson gson;

    public UserService() {
        super(".UserService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UserService(String name) {
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

        RuntimeTypeAdapterFactory<Person> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Person.class, "type")
                .registerSubtype(Elderly.class, Elderly.class.getSimpleName());
        this.gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();

        String userJson = intent.getStringExtra(User.USER_JSON);
        User user = this.gson.fromJson(userJson, User.class);

        CrudAction action = (CrudAction) intent.getSerializableExtra("crudAction");
        switch (action) {
            case CREATE:
                user = this.save(user);
                break;
            case READ:
                this.load(user.getEmail());
                break;
            case UPDATE:
                user = this.update(user);
                break;
            case DELETE:
                this.delete(user);
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private User save(User user) {
        String key = this.mDatabase.child("user").push().getKey();
        user.setKey(key);

        return this.update(user);
    }

    private void load(String email) {
        this.mDatabase.child("user").orderByChild("email").equalTo(email).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object value = snapshot.getValue();
                    if (value != null) {
                        Intent loadResponseIntent =  new Intent();
                        loadResponseIntent.setAction(USER_SERVICE_ACTION_LOAD);
                        loadResponseIntent.putExtra(User.USER_JSON, value.toString());
                        UserService.this.sendBroadcast(loadResponseIntent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private User update(User user) {
        Task<Void> task = this.mDatabase.child("user").child(user.getKey()).setValue(user);

        // fixme add some event listener that has onComplete method because this is always returning false
        if (task.isSuccessful()) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
//            Log.e("update", task.getException().getMessage());
        }

        return user;
    }

    private void delete(User user) {
        Task<Void> task = this.mDatabase.child("user").child(user.getKey()).removeValue();

        if (task.isSuccessful()) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
            Log.e("update", task.getException().getMessage());
        }
    }

    public static User toUser(FirebaseUser currentUser) {
        User user = new User();
        if (currentUser == null) {
            return user;
        }
        user.setEmail(currentUser.getEmail());

        return user;
    }

    public static void startUserService(String userJson,
                                        CrudAction action,
                                        Context context) {

        Intent userServiceIntent = new Intent(context, UserService.class);
        userServiceIntent.putExtra(User.USER_JSON, userJson);
        userServiceIntent.putExtra("crudAction", action);
        context.startService(userServiceIntent);
    }
}
