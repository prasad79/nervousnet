package ch.ethz.coss.nervousnet.vm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import ch.ethz.coss.nervousnet.sensors.BatterySensor;
import ch.ethz.coss.nervousnet.sensors.BatterySensor.BatteryListener;

public class NervousnetVMService extends Service implements BatteryListener, LocationListener {

	private static int SERVICE_STATE = 0; // 0 - NOT RUNNING, 1 - RUNNING
	private static int counter = 0;

	private PowerManager.WakeLock wakeLock;

	private HandlerThread hthread;
	private static Handler handler;
	private static Runnable runnable;
	private static Runnable run;
	private final int runTime = 1000;

	private BatterySensor sensorBattery = null;
	private LocationManager locationManager = null;

	private LocationReading locationReading = null;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final NervousnetRemote.Stub mBinder = new NervousnetRemote.Stub() {
		@Override
		public int getCounter() {
			return counter;
		}

		@Override
		public BatteryReading getBatteryReading() {
			Log.d("NervousnetVMService", "Sending Battery Reading " + sensorBattery.getReading());

			if (sensorBattery == null)
				return null;

			return sensorBattery.getReading();
		}

		@Override
		public float getBatteryPercent() {
			return sensorBattery.getReading().getPercent();
		}

		@Override
		public LocationReading getLocationReading() throws RemoteException {

			if (locationReading == null) {
				Log.d("NervousnetVMService", "Location reading is null " + sensorBattery.getReading());

				Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				return new LocationReading((int) (System.currentTimeMillis() / 1000), location.getLatitude(),
						location.getLongitude(), location.getAltitude());

			}

			return locationReading;
		}

	};

	@Override
	public void onCreate() {
		Log.d("NervousnetVMService", "oncreate - Service started");
		super.onCreate();
		SERVICE_STATE = 1;

		// Prepare the wakelock
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.LOG_TAG);
		hthread = new HandlerThread("HandlerThread");
		hthread.start();

		// Acquire wakelock, some sensors on some phones need this
		if (!wakeLock.isHeld()) {
			wakeLock.acquire();
		}

		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				counter++;
				Toast.makeText(NervousnetVMService.this, "" + counter, Toast.LENGTH_SHORT).show();
				if (handler != null)
					handler.postDelayed(runnable, runTime);
			}
		};
		handler.post(runnable);

		scheduleSensor(BatterySensor.SENSOR_ID);
		scheduleSensor(Constants.SENSOR_LOCATION);

		if (Constants.DEBUG)
			Toast.makeText(NervousnetVMService.this, "Service started", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onDestroy() {
		Log.d("NervousnetVMService", "onDestroy - Service destroyed");

		runnable = null;
		handler = null;
		SERVICE_STATE = 0;

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

	private void scheduleSensor(final long sensorId) {
		run = new Runnable() {
			@Override
			public void run() {

				if (sensorId == BatterySensor.SENSOR_ID) {
					startBatterySensor();
				} else if (sensorId == Constants.SENSOR_LOCATION) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
							NervousnetVMService.this);
				}

				Log.d("NervousnetVMService", "Running Schedule Sensor thread");
				Toast.makeText(NervousnetVMService.this, "" + counter, Toast.LENGTH_LONG).show();

			}
		};
		handler.postDelayed(run, 10000);
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
	@Override
	public void batterySensorDataReady(BatteryReading reading) {
		Log.d("NervousnetVMService", reading.toString());

	}

	private void startBatterySensor() {
		sensorBattery = BatterySensor.getInstance(NervousnetVMService.this);
		sensorBattery.addListener(NervousnetVMService.this);
		sensorBattery.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.hardware.SensorListener#onAccuracyChanged(int, int)
	 */
	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.hardware.SensorListener#onSensorChanged(int, float[])
	 */
	public void onSensorChanged(int sensor, float[] values) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onLocationChanged(android.location.
	 * Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		locationReading = new LocationReading((int) (System.currentTimeMillis() / 1000),
				new double[] { location.getLatitude(), location.getLongitude() }, location.getAltitude());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String,
	 * int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}