package br.com.aimcol.fallalertapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.model.Person;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.util.CrudAction;
import br.com.aimcol.fallalertapp.util.RuntimeTypeAdapterFactory;

public class UserService extends IntentService {

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

        RuntimeTypeAdapterFactory<Person> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Person.class, "type")
                .registerSubtype(Elderly.class, "elderly");
        this.gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();

        String userJson = intent.getStringExtra(User.USER_JSON);
        User user = this.gson.fromJson(userJson, User.class);

        CrudAction action = (CrudAction) intent.getSerializableExtra("crudAction");
        switch (action) {
            case CREATE:
                user = this.save(user);
                break;
            case READ:
                break;
            case UPDATE:
                user = this.update(user);
                break;
            case DELETE:
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private User save(User user) {
        return user;
    }

    private User update(User user) {
        return user;
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
