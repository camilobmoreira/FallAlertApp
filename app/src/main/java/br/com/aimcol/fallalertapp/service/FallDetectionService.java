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
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.aimcol.fallalertapp.model.Elderly;
import br.com.aimcol.fallalertapp.util.AccelerometerAxis;

public class FallDetectionService extends IntentService implements SensorEventListener {


    private static final int ACCELEROMETER_SAMPLING_PERIOD = 1000000;
    public static final double CSV_THRESHOLD = 23;
    public static final double CAV_THRESHOLD = 18;
    public static final double CCA_THRESHOLD = 0;//65.5;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Elderly elderly;
    private Gson gson = new Gson();

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

        this.isFallDetected(x, y, z);
    }

    private void isFallDetected(double x, double y, double z) {
        double acceleration = this.calculateAcceleration(x, y, z);// - SensorManager.GRAVITY_EARTH;
        this.addAccelerometerValuesToList(x, y, z, acceleration);

        if (acceleration > CSV_THRESHOLD) {
            double angleVariation = this.calculateAngleVariation();
            if (angleVariation > CAV_THRESHOLD) {
                double changeInAngle = this.calculateChangeInAngle();
                if (changeInAngle > CCA_THRESHOLD) {
                    Toast.makeText(this, "Queda", Toast.LENGTH_LONG).show();

                    Intent fallNotificationServiceIntent = new Intent(this, FallNotificationService.class);
                    fallNotificationServiceIntent.putExtra(Elderly.ELDERLY_JSON, this.gson.toJson(this.elderly));
                    this.getBaseContext().startService(fallNotificationServiceIntent);
                }
            }
        }
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

    private double calculateAcceleration(double x,
                                         double y,
                                         double z) {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    private double calculateAngleVariation() {
        int size = this.accelerometerValues.size();
        if (size < 2){
            return -1;
        }

//        double anX = this.accelerometerValues.get(3).get(AccelerometerAxis.X) * this.accelerometerValues.get(4).get(AccelerometerAxis.X);
//        double anY = this.accelerometerValues.get(3).get(AccelerometerAxis.Y) * this.accelerometerValues.get(4).get(AccelerometerAxis.Y);
//        double anZ = this.accelerometerValues.get(3).get(AccelerometerAxis.Z) * this.accelerometerValues.get(4).get(AccelerometerAxis.Z);
//        double an = anX + anY + anZ;

        double an = this.accelerometerValues.get(size -2).get(AccelerometerAxis.ACCELERATION) * this.accelerometerValues.get(size -1).get(AccelerometerAxis.ACCELERATION);

        double anX0 = Math.pow(this.accelerometerValues.get(size -2).get(AccelerometerAxis.X), 2);
        double anY0 = Math.pow(this.accelerometerValues.get(size -2).get(AccelerometerAxis.Y), 2);
        double anZ0 = Math.pow(this.accelerometerValues.get(size -2).get(AccelerometerAxis.Z), 2);
        double an0 = Math.sqrt(anX0 + anY0 + anZ0);

        double anX1 = Math.pow(this.accelerometerValues.get(size -1).get(AccelerometerAxis.X), 2);
        double anY1 = Math.pow(this.accelerometerValues.get(size -1).get(AccelerometerAxis.Y), 2);
        double anZ1 = Math.pow(this.accelerometerValues.get(size -1).get(AccelerometerAxis.Z), 2);
        double an1 = Math.sqrt(anX1 + anY1 + anZ1);

        double a = an / (an0 * an1);

        return (Math.pow(Math.cos(a), -1)) * (180 / Math.PI);
    }

    private double calculateChangeInAngle() {
        int size = this.accelerometerValues.size();
        if (size < 4){
            return -1;
        }
        double aX = this.accelerometerValues.get(0).get(AccelerometerAxis.X) * this.accelerometerValues.get(3).get(AccelerometerAxis.X);
        double aY = this.accelerometerValues.get(0).get(AccelerometerAxis.Y) * this.accelerometerValues.get(3).get(AccelerometerAxis.Y);
        double aZ = this.accelerometerValues.get(0).get(AccelerometerAxis.Z) * this.accelerometerValues.get(3).get(AccelerometerAxis.Z);

        double a0 = aX + aY + aZ;

        double a1 = (Math.sqrt(Math.pow(aX, 2)) + Math.sqrt(Math.pow(aY, 2)) + Math.sqrt(Math.pow(aZ, 2)));

        return (Math.pow(Math.cos(a0 / a1), -1)) * (180 / Math.PI);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (this.elderly == null) {
            String elderlyJson = intent.getStringExtra(Elderly.ELDERLY_JSON);
            this.elderly = this.gson.fromJson(elderlyJson, Elderly.class);
        }
    }

    @Override
    public int onStartCommand(Intent intent,
                              int flags,
                              int startId) {
        if (this.elderly == null) {
            String elderlyJson = intent.getStringExtra(Elderly.ELDERLY_JSON);
            this.elderly = this.gson.fromJson(elderlyJson, Elderly.class);
        }

        this.mSensorManager = (SensorManager) super.getSystemService(Context.SENSOR_SERVICE);
        //this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        //if (this.mAccelerometer == null) {
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //}
        if (this.mAccelerometer == null) {
            throw new RuntimeException("Acelerometro não encontrado");
        }

        this.mSensorManager.registerListener(this, this.mAccelerometer, ACCELEROMETER_SAMPLING_PERIOD);

        return Service.START_STICKY;
    }
}
