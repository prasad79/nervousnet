package ch.ethz.coss.nervousnet.vm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.Constants;
import ch.ethz.coss.nervousnet.sensors.AccelerometerSensor;
import ch.ethz.coss.nervousnet.sensors.BatterySensor;
import ch.ethz.coss.nervousnet.sensors.BatterySensor.BatterySensorListener;
import ch.ethz.coss.nervousnet.sensors.LightSensor;
import ch.ethz.coss.nervousnet.sensors.LocationSensor;
import ch.ethz.coss.nervousnet.sensors.LocationSensor.LocationSensorListener;

public class NervousnetVMService extends Service implements BatterySensorListener, LocationSensorListener, SensorEventListener {

	
//	private NervousnetVMServiceHandler serviceHandler;
	private static int SERVICE_STATE = 0; // 0 - NOT RUNNING, 1 - RUNNING
	
	private PowerManager.WakeLock wakeLock;

	private HandlerThread hthread;
	


	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final NervousnetRemote.Stub mBinder = new NervousnetRemote.Stub() {
		@Override
		public int getCounter() {
			return NervousnetVMServiceHandler.getInstance().counter;
		}

		@Override
		public BatteryReading getBatteryReading() {
			Log.d("NervousnetVMService", "Sending Battery Reading " + NervousnetVMServiceHandler.getInstance().sensorBattery.getReading());

			if (NervousnetVMServiceHandler.getInstance().sensorBattery == null)
				return null;

			return (BatteryReading) NervousnetVMServiceHandler.getInstance().sensorBattery.getReading();
		}

		@Override
		public float getBatteryPercent() {
			return ((BatteryReading) NervousnetVMServiceHandler.getInstance().sensorBattery.getReading()).getPercent();
		}

		@Override
		public LocationReading getLocationReading() throws RemoteException {

			return (LocationReading) NervousnetVMServiceHandler.getInstance().sensorLocation.getReading();
		}

		@Override
		public AccelerometerReading getAccelerometerReading() throws RemoteException {
			// TODO Auto-generated method stub
			return (AccelerometerReading) AccelerometerSensor.getInstance().getReading();
		}

	};

	@Override
	public void onCreate() {
		Log.d("NervousnetVMService", "oncreate - Service started");
		super.onCreate();
		SERVICE_STATE = 1;
		
	
		// Prepare the wakelock
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);

		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.LOG_TAG);
		hthread = new HandlerThread("HandlerThread");
		hthread.start();

		// Acquire wakelock, some sensors on some phones need this
		if (!wakeLock.isHeld()) {
			wakeLock.acquire();
		}
		
		 

		NervousnetVMServiceHandler.getInstance().scheduleSensor(BatterySensor.SENSOR_ID, NervousnetVMService.this, this);
		NervousnetVMServiceHandler.getInstance().scheduleSensor(Constants.SENSOR_LOCATION, NervousnetVMService.this, this);
		NervousnetVMServiceHandler.getInstance().scheduleSensor(Constants.SENSOR_LIGHT, NervousnetVMService.this, this);
		NervousnetVMServiceHandler.getInstance().scheduleSensor(Constants.SENSOR_ACCELEROMETER, NervousnetVMService.this, this);

		if (Constants.DEBUG)
			Toast.makeText(NervousnetVMService.this, "Service started", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onDestroy() {
		Log.d("NervousnetVMService", "onDestroy - Service destroyed");

	
		SERVICE_STATE = 0;

		NervousnetVMServiceHandler.getInstance().cleanup();
		// Release the wakelock here, just to be safe, in order something went
		// wrong
		if (wakeLock.isHeld()) {
			wakeLock.release();
		}
		// sensorManager.unregisterListener(this);
		hthread.quit();

		if (Constants.DEBUG)
			Toast.makeText(NervousnetVMService.this, "Service destroyed", Toast.LENGTH_LONG).show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		Log.d("NervousnetVMService", "onStartCommand called");
		if (Constants.DEBUG)
			Toast.makeText(NervousnetVMService.this, "onStartCommand called!", Toast.LENGTH_LONG).show();

		return START_STICKY;
	}

	public static boolean isServiceRunning() {

		if (SERVICE_STATE != 0)
			return true;
		else
			return false;
	}

	public static void startService(Context context) {
		Intent sensorIntent = new Intent(context, NervousnetVMService.class);
		context.startService(sensorIntent);
	}

	public static void stopService(Context context) {
		Intent sensorIntent = new Intent(context, NervousnetVMService.class);
		context.stopService(sensorIntent);
	}


	// private void scheduleSensor(final long sensorId) {
	// handler = new Handler(hthread.getLooper());
	// final Runnable run = new Runnable() {
	// @Override
	// public void run() {
	// boolean doCollect = false;
	// SensorCollectStatus sensorCollectStatus = null;
	// long startTime = System.currentTimeMillis();
	//
	// if (sensorId == SensorDescAccelerometer.SENSOR_ID) {
	// scAccelerometer.setMeasureStart(startTime);
	// doCollect = scAccelerometer.isCollect();
	// doCollect = doCollect ?
	// sensorManager.registerListener(sensorListenerClass, sensorAccelerometer,
	// SensorManager.SENSOR_DELAY_NORMAL) : false;
	// sensorCollectStatus = scAccelerometer;
	// } else if (sensorId == SensorDescPressure.SENSOR_ID) {
	// scPressure.setMeasureStart(startTime);
	// doCollect = scPressure.isCollect();
	// doCollect = doCollect ?
	// sensorManager.registerListener(sensorListenerClass, sensorPressure,
	// SensorManager.SENSOR_DELAY_NORMAL) : false;
	// sensorCollectStatus = scPressure;
	// } else if (sensorId == SensorDescGyroscope.SENSOR_ID) {
	// doCollect = scGyroscope.isCollect();
	// doCollect = doCollect ?
	// sensorManager.registerListener(sensorListenerClass, sensorGyroscope,
	// SensorManager.SENSOR_DELAY_NORMAL) : false;
	// sensorCollectStatus = scGyroscope;
	// } else if (sensorId == SensorDescHumidity.SENSOR_ID) {
	// scHumidity.setMeasureStart(startTime);
	// doCollect = scHumidity.isCollect();
	// doCollect = doCollect ?
	// sensorManager.registerListener(sensorListenerClass, sensorHumidity,
	// SensorManager.SENSOR_DELAY_NORMAL) : false;
	// sensorCollectStatus = scHumidity;
	// } else if (sensorId == SensorDescLight.SENSOR_ID) {
	// scLight.setMeasureStart(startTime);
	// doCollect = scLight.isCollect();
	// doCollect = doCollect ?
	// sensorManager.registerListener(sensorListenerClass, sensorLight,
	// SensorManager.SENSOR_DELAY_NORMAL) : false;
	// sensorCollectStatus = scLight;
	// } else if (sensorId == SensorDescMagnetic.SENSOR_ID) {
	// scMagnet.setMeasureStart(startTime);
	// doCollect = scMagnet.isCollect();
	// doCollect = doCollect ?
	// sensorManager.registerListener(sensorListenerClass, sensorMagnet,
	// SensorManager.SENSOR_DELAY_NORMAL) : false;
	// sensorCollectStatus = scMagnet;
	// } else if (sensorId == SensorDescProximity.SENSOR_ID) {
	// scProximity.setMeasureStart(startTime);
	// doCollect = scProximity.isCollect();
	// doCollect = doCollect ?
	// sensorManager.registerListener(sensorListenerClass, sensorProximity,
	// SensorManager.SENSOR_DELAY_NORMAL) : false;
	// sensorCollectStatus = scProximity;
	// } else if (sensorId == SensorDescTemperature.SENSOR_ID) {
	// scTemperature.setMeasureStart(startTime);
	// doCollect = scTemperature.isCollect();
	// doCollect = doCollect ?
	// sensorManager.registerListener(sensorListenerClass, sensorTemperature,
	// SensorManager.SENSOR_DELAY_NORMAL) : false;
	// sensorCollectStatus = scTemperature;
	// } else if (sensorId == SensorDescBattery.SENSOR_ID) {
	// scBattery.setMeasureStart(startTime);
	// doCollect = scBattery.isCollect();
	// if (doCollect) {
	// sensorBattery.clearListeners();
	// sensorBattery.addListener(sensorListenerClass);
	// sensorBattery.start();
	// }
	// sensorCollectStatus = scBattery;
	// } else if (sensorId == SensorDescConnectivity.SENSOR_ID) {
	// scConnectivity.setMeasureStart(startTime);
	// doCollect = scConnectivity.isCollect();
	// if (doCollect) {
	// sensorConnectivity.clearListeners();
	// sensorConnectivity.addListener(sensorListenerClass);
	// sensorConnectivity.start();
	// }
	// sensorCollectStatus = scConnectivity;
	// } else if (sensorId == SensorDescBLEBeacon.SENSOR_ID) {
	// scBLEBeacon.setMeasureStart(startTime);
	// doCollect = scBLEBeacon.isCollect();
	// if (doCollect) {
	// sensorBLEBeacon.clearListeners();
	// sensorBLEBeacon.addListener(sensorListenerClass);
	// // Update this variable if the BLE sensor is currently unavailable
	// doCollect =
	// sensorBLEBeacon.startScanning(Math.max(scBLEBeacon.getMeasureDuration(),
	// 2000));
	// }
	// // TODO Fix for now, agressive BLE scanning
	//// scBLEBeacon.setMeasureInterval(3000);
	// sensorCollectStatus = scBLEBeacon;
	// } else if (sensorId == SensorDescNoise.SENSOR_ID) {
	// scNoise.setMeasureStart(startTime);
	// doCollect = scNoise.isCollect();
	// if (doCollect) {
	// sensorNoise.clearListeners();
	// sensorNoise.addListener(sensorListenerClass);
	// // Noise sensor doesn't really make sense with less than 500ms
	// sensorNoise.startRecording(Math.max(scNoise.getMeasureDuration(), 500));
	// }
	// sensorCollectStatus = scNoise;
	// }
	//
	// if (doCollect && sensorCollectStatus != null) {
	// sensorCollected.put(sensorId, sensorCollectStatus);
	// }
	//
	// if (sensorCollectStatus != null) {
	// long interval = sensorCollectStatus.getMeasureInterval();
	// Log.d(LOG_TAG, "Logging sensor " + String.valueOf(sensorId) + " started
	// with interval " + String.valueOf(interval) + " ms");
	// handler.postDelayed(this, interval);
	// }
	//
	// }
	// };
	// // 10 seconds initial delay
	// handler.postDelayed(run, 10000);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ethz.coss.nervousnet.sensors.BatterySensor.BatteryListener#
	 * batterySensorDataReady(long, float, boolean, boolean, boolean)
	 */
	// @Override
	// public void batterySensorDataReady(BatteryReading reading) {
	// Log.d("NervousnetVMService", reading.toString());
	//
	// }

	@Override
	public void locSensorDataReady(LocationReading reading) {
		Log.d("NervousnetVMService", "locSensorDataReady received - " + reading.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ethz.coss.nervousnet.sensors.BatterySensor.BatterySensorListener#
	 * batterySensorDataReady(ch.ethz.coss.nervousnet.vm.BatteryReading)
	 */
	@Override
	public void batterySensorDataReady(BatteryReading reading) {
		Log.d("NervousnetVMService", reading.toString());

	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d("NervousnetVMService", "onAccuracyChanged called");
		
	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.d("NervousnetVMService", "onSensorChanged called");
		
		int timestamp = (int) (System.currentTimeMillis() / 1000);
		Sensor sensor = event.sensor;
		SensorReading reading = null;

		switch (sensor.getType()) {
		case Sensor.TYPE_LIGHT:
			reading = new LightReading((int) (event.timestamp/1000), event.values);
			LightSensor.getInstance().dataReady((LightReading)reading);
			Log.d("NervousnetVMService", "Light data collected");
			break;
		case Sensor.TYPE_PROXIMITY:
//			reading = new SensorDescProximity(timestamp, event.values[0]);
			Log.d("NervousnetVMService", "Proximity data collected");
			break;
		case Sensor.TYPE_ACCELEROMETER:
			reading = new AccelerometerReading((int) (event.timestamp/1000), event.values);
			AccelerometerSensor.getInstance().dataReady((AccelerometerReading)reading);
			Log.d("NervousnetVMService", "Accelerometer data collected");
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
//			reading = new SensorDescMagnetic(timestamp, event.values[0], event.values[1], event.values[2]);
			Log.d("NervousnetVMService", "Magnetic data collected");
			break;
		case Sensor.TYPE_GYROSCOPE:
//			reading = new SensorDescGyroscope(timestamp, event.values[0], event.values[1], event.values[2]);
			Log.d("NervousnetVMService", "Gyroscope data collected");
			break;
		case Sensor.TYPE_AMBIENT_TEMPERATURE:
//			reading = new SensorDescTemperature(timestamp, event.values[0]);
			Log.d("NervousnetVMService", "Temperature data collected");
			break;
		case Sensor.TYPE_RELATIVE_HUMIDITY:
//			reading = new SensorDescHumidity(timestamp, event.values[0]);
			Log.d("NervousnetVMService", "Humidity data collected");
			break;
		case Sensor.TYPE_PRESSURE:
//			reading = new SensorDescPressure(timestamp, event.values[0]);
			Log.d("NervousnetVMService", "Pressure data collected");
			break;
		}
		
	}

}