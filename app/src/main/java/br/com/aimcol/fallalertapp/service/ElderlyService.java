package br.com.aimcol.fallalertapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.util.CrudAction;

public class ElderlyService extends IntentService {

    private Gson gson;

    public ElderlyService() {
        super(".ElderlyService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ElderlyService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        if (this.elderly == null) {
//            String elderlyJson = intent.getStringExtra(Elderly.ELDERLY_JSON);
//            this.elderly = this.gson.fromJson(elderlyJson, Elderly.class);
//        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        this.gson = new Gson();
        String elderlyJson = intent.getStringExtra(Elderly.ELDERLY_JSON);
        Elderly elderly = this.gson.fromJson(elderlyJson, Elderly.class);

        CrudAction action = CrudAction.valueOf(intent.getStringExtra("crudAction"));
        switch (action) {
            case CREATE:
                elderly = this.save(elderly);
                break;
            case READ:
                break;
            case UPDATE:
                break;
            case DELETE:
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private Elderly save(Elderly elderly) {

        return elderly;
    }
}
