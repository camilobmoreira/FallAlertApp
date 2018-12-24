package br.com.aimcol.fallalertapp.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.activity.FallNotificationActivity;
import br.com.aimcol.fallalertapp.model.Configuration;
import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.model.Person;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.util.AccelerometerAxis;
import br.com.aimcol.fallalertapp.util.RuntimeTypeAdapterFactory;

public class FallDetectionService extends IntentService implements SensorEventListener {


    private static final int ACCELEROMETER_SAMPLING_PERIOD = 1000000;
    private static final double CSV_THRESHOLD = 23;
    private static final double CAV_THRESHOLD = 18;
    private static final double CCA_THRESHOLD = 65.5;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private User user;
    private Gson gson;

    private Long lastSentInMillis;
    private Long minTimeToNotifyAgain = 30000L;

    private List<Map<AccelerometerAxis, Double>> accelerometerValues = new ArrayList<>();


    public FallDetectionService() {
        super(".FallDetectionService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FallDetectionService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor,
                                        int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Axis of the rotation sample, not normalized yet.
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        if (this.isFallDetected(x, y, z)) {
            if (this.isOkayToNotifyAgain()) {
                this.lastSentInMillis = System.currentTimeMillis();
                Toast.makeText(this, super.getString(R.string.fall_happened), Toast.LENGTH_LONG).show();
                FallNotificationActivity.startFallNotificationActivity(this, this.gson.toJson(this.user));
            }
        }
    }

    private boolean isFallDetected(double x,
                                double y,
                                double z) {

        double acceleration = this.calculateSumVector(x, y, z);
        this.addAccelerometerValuesToList(x, y, z, acceleration);

        StringBuilder msg = new StringBuilder("x: ").append(x)
                .append(" y: ").append(y)
                .append(" z: ").append(z)
                .append(" acc: ").append(acceleration);
        Log.d("FDS-Acc-Values", msg.toString());

        if (acceleration > CSV_THRESHOLD) {
            double angleVariation = this.calculateAngleVariation();
            if (angleVariation > CAV_THRESHOLD) {
                double changeInAngle = this.calculateChangeInAngle();
                if (changeInAngle > CCA_THRESHOLD) {
                    msg.append(System.currentTimeMillis());
                    Log.d("FDS-Fall-Happened", msg.toString());
                    return true;
                }
            }

        }
        return false;
    }

    private void addAccelerometerValuesToList(double x,
                                              double y,
                                              double z,
                                              double acceleration) {
        if(this.accelerometerValues.size() >= 4) {
            this.accelerometerValues.remove(0);
        }
        Map<AccelerometerAxis, Double> map = new HashMap<>();
        map.put(AccelerometerAxis.X, x);
        map.put(AccelerometerAxis.Y, y);
        map.put(AccelerometerAxis.Z, z);
        map.put(AccelerometerAxis.ACCELERATION, acceleration);
        this.accelerometerValues.add(map);
    }

    private double calculateSumVector(double x,
                                         double y,
                                         double z) {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    private double calculateAngleVariation() {
        int size = this.accelerometerValues.size();
        if (size < 2){
            return -1;
        }

        Map<AccelerometerAxis, Double> minusTwo = this.accelerometerValues.get(size - 2);
        Map<AccelerometerAxis, Double> minusOne = this.accelerometerValues.get(size - 1);

        double anX = minusTwo.get(AccelerometerAxis.X) * minusOne.get(AccelerometerAxis.X);
        double anY = minusTwo.get(AccelerometerAxis.Y) * minusOne.get(AccelerometerAxis.Y);
        double anZ = minusTwo.get(AccelerometerAxis.Z) * minusOne.get(AccelerometerAxis.Z);
        double an = anX + anY + anZ;

        double anX0 = Math.pow(minusTwo.get(AccelerometerAxis.X), 2);
        double anY0 = Math.pow(minusTwo.get(AccelerometerAxis.Y), 2);
        double anZ0 = Math.pow(minusTwo.get(AccelerometerAxis.Z), 2);
        double an0 = Math.sqrt(anX0 + anY0 + anZ0);

        double anX1 = Math.pow(minusOne.get(AccelerometerAxis.X), 2);
        double anY1 = Math.pow(minusOne.get(AccelerometerAxis.Y), 2);
        double anZ1 = Math.pow(minusOne.get(AccelerometerAxis.Z), 2);
        double an1 = Math.sqrt(anX1 + anY1 + anZ1);

        double a = an / (an0 * an1);

        return Math.acos(a) * (180 / Math.PI);
    }

    private double calculateChangeInAngle() {
        int size = this.accelerometerValues.size();
        if (size < 4){
            return -1;
        }
        Map<AccelerometerAxis, Double> first = this.accelerometerValues.get(0);
        Map<AccelerometerAxis, Double> third = this.accelerometerValues.get(3);

        double aX = first.get(AccelerometerAxis.X) * third.get(AccelerometerAxis.X);
        double aY = first.get(AccelerometerAxis.Y) * third.get(AccelerometerAxis.Y);
        double aZ = first.get(AccelerometerAxis.Z) * third.get(AccelerometerAxis.Z);

        double a0 = aX + aY + aZ;

        aX = Math.pow(aX, 2);
        aY = Math.pow(aY, 2);
        aZ = Math.pow(aZ, 2);
        double a1 = (Math.sqrt(aX) + Math.sqrt(aY) + Math.sqrt(aZ));

        return Math.acos(a0 / a1) * (180 / Math.PI);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (this.user == null) {
            String userJson = intent.getStringExtra(User.USER_JSON);
            this.user = this.gson.fromJson(userJson, User.class);
            Long minTime = (Long) this.user.getConfigurations().get(Configuration.MIN_TIME_TO_NOTIFY_AGAIN);
            this.minTimeToNotifyAgain = minTime != null ? minTime : this.minTimeToNotifyAgain;

        }
    }

    @Override
    public int onStartCommand(Intent intent,
                              int flags,
                              int startId) {

        if (this.gson == null) {
            RuntimeTypeAdapterFactory<Person> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                    .of(Person.class, "type")
                    .registerSubtype(Elderly.class, Elderly.class.getSimpleName());
            this.gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
        }

        if (this.user == null) {
            String userJson = intent.getStringExtra(User.USER_JSON);
            this.user = this.gson.fromJson(userJson, User.class);
        }

        this.mSensorManager = (SensorManager) super.getSystemService(Context.SENSOR_SERVICE);
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (this.mAccelerometer == null) {
            throw new RuntimeException(super.getString(R.string.accelerometer_not_found));
        }

        this.mSensorManager.registerListener(this, this.mAccelerometer, ACCELEROMETER_SAMPLING_PERIOD);

        return Service.START_STICKY;
    }

    private boolean isOkayToNotifyAgain() {
        return this.lastSentInMillis == null || (this.lastSentInMillis + this.minTimeToNotifyAgain) < System.currentTimeMillis();
    }

    public static void startFallDetectionService(String userJson,
                                                 Context context) {

        Intent fallDetectionServiceIntent = new Intent(context, FallDetectionService.class);
        fallDetectionServiceIntent.putExtra(User.USER_JSON, userJson);
        context.startService(fallDetectionServiceIntent);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected boolean testFallDetection(List<Map<AccelerometerAxis, Double>> values) {
        for (Map<AccelerometerAxis, Double> value : values) {
            if (this.isFallDetected(
                    value.get(AccelerometerAxis.X),
                    value.get(AccelerometerAxis.Y),
                    value.get(AccelerometerAxis.Z))) {

                return true;
            }
        }
        return false;
    }
}

