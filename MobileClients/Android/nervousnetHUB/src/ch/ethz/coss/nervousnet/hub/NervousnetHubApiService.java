/*******************************************************************************
 *
 *  *     Nervousnet - a distributed middleware software for social sensing. 
 *  *      It is responsible for collecting and managing data in a fully de-centralised fashion
 *  *
 *  *     Copyright (C) 2016 ETH Zürich, COSS
 *  *
 *  *     This file is part of Nervousnet Framework
 *  *
 *  *     Nervousnet is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     Nervousnet is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with NervousNet. If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 *******************************************************************************/
package ch.ethz.coss.nervousnet.hub;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.hub.R;
import ch.ethz.coss.nervousnet.hub.ui.StartUpActivity;
import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.ConnectivityReading;
import ch.ethz.coss.nervousnet.lib.GyroReading;
import ch.ethz.coss.nervousnet.lib.LibConstants;
import ch.ethz.coss.nervousnet.lib.LightReading;
import ch.ethz.coss.nervousnet.lib.LocationReading;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;
import ch.ethz.coss.nervousnet.lib.NoiseReading;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import ch.ethz.coss.nervousnet.vm.sensors.BatterySensor;
import ch.ethz.coss.nervousnet.vm.sensors.BatterySensor.BatterySensorListener;
import ch.ethz.coss.nervousnet.vm.sensors.ConnectivitySensor;
import ch.ethz.coss.nervousnet.vm.sensors.ConnectivitySensor.ConnectivitySensorListener;
import ch.ethz.coss.nervousnet.vm.sensors.LocationSensor.LocationSensorListener;
import ch.ethz.coss.nervousnet.vm.sensors.NoiseSensor.NoiseSensorListener;
import ch.ethz.coss.nervousnet.vm.sensors.LocationSensor;
import ch.ethz.coss.nervousnet.vm.sensors.NoiseSensor;
import ch.ethz.coss.nervousnet.vm.storage.AccelData;
import ch.ethz.coss.nervousnet.vm.storage.ConnectivityData;
import ch.ethz.coss.nervousnet.vm.storage.GyroData;
import ch.ethz.coss.nervousnet.vm.storage.LightData;
import ch.ethz.coss.nervousnet.vm.storage.LocationData;
import ch.ethz.coss.nervousnet.vm.storage.SensorDataImpl;
import ch.ethz.coss.nervousnet.vm.storage.StoreTask;

public class NervousnetHubApiService extends Service implements SensorEventListener, BatterySensorListener,
		ConnectivitySensorListener, LocationSensorListener, NoiseSensorListener {

	private static final String LOG_TAG = NervousnetHubApiService.class.getSimpleName();

	private SensorManager sensorManager = null;

	private static NotificationManager mNM;
	private static int NOTIFICATION = R.string.local_service_started;

	private PowerManager.WakeLock wakeLock;
	private HandlerThread hthread;
	private Handler handler;
	private Lock storeMutex;

	// Only initialize these once
	private Sensor sensorAccelerometer = null;
	private BatterySensor sensorBattery = null;
	private ConnectivitySensor sensorConnectivity = null;
	private Sensor sensorLight = null;
	private Sensor sensorMagnet = null;
	private Sensor sensorProximity = null;
	private Sensor sensorGyroscope = null;
	private Sensor sensorTemperature = null;
	private Sensor sensorHumidity = null;
	private Sensor sensorPressure = null;
	private NoiseSensor sensorNoise = null;
	// private BLESensor sensorBLEBeacon = null;
	private LocationSensor sensorLocation = null;

	// latest Sensor Reading objects
	private LocationReading locReading = null;
	private AccelerometerReading accReading = null;
	private BatteryReading batteryReading = null;
	private ConnectivityReading connReading = null;
	private GyroReading gyroReading = null;
	private NoiseReading noiseReading = null;
	private LightReading lightReading = null;

	// Those need to be reset on every collect call
	private SensorCollectStatus scAccelerometer = null;
	private SensorCollectStatus scBattery = null;
	private SensorCollectStatus scLight = null;
	private SensorCollectStatus scMagnet = null;
	private SensorCollectStatus scProximity = null;
	private SensorCollectStatus scGyroscope = null;
	private SensorCollectStatus scTemperature = null;
	private SensorCollectStatus scHumidity = null;
	private SensorCollectStatus scPressure = null;
	private SensorCollectStatus scNoise = null;
	private SensorCollectStatus scBLEBeacon = null;
	private SensorCollectStatus scConnectivity = null;
	private SensorCollectStatus scLocation = null;

	// Threadsafe because handling can get called from different threads
	private ConcurrentHashMap<Long, SensorCollectStatus> sensorCollected;

	@Override
	public void onCreate() {
		// Prepare the wakelock
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOG_TAG);
		hthread = new HandlerThread("HandlerThread");
		hthread.start();
		// Acquire wakelock, some sensors on some phones need this
		if (!wakeLock.isHeld()) {
			wakeLock.acquire();
		}

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Display a notification about us starting. We put an icon in the
		// status bar.
		showNotification();
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = getText(R.string.local_service_started);

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, StartUpActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		Notification notification = new Notification.Builder(this).setSmallIcon(getNotificationIcon()) // the
																										// status
																										// icon
				.setTicker(text) // the status text
				.setWhen(System.currentTimeMillis()) // the time stamp
				.setContentTitle(getText(R.string.local_service_label)) // the
																		// label
																		// of
																		// the
																		// entry
				.setContentText(text) // the contents of the entry
				.setContentIntent(contentIntent) // The intent to send when the
													// entry is clicked
				.build();

		if (mNM != null)
			mNM.notify(NOTIFICATION, notification);
	}

	public LocationReading getLocReading() {
		return locReading;
	}

	public AccelerometerReading getAccReading() {
		return accReading;
	}

	public BatteryReading getBatteryReading() {
		return batteryReading;
	}

	public ConnectivityReading getConnReading() {
		return connReading;
	}

	public GyroReading getGyroReading() {
		return gyroReading;
	}

	public NoiseReading getNoiseReading() {
		return noiseReading;
	}

	public LightReading getLightReading() {
		return lightReading;
	}

	private int getNotificationIcon() {
		boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
		return useWhiteIcon ? R.drawable.ic_logo_white : R.drawable.ic_logo;
	}

	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "********SERVICE Destroyed ");
		removeNotification();
		// Release the wakelock here, just to be safe, in order something went
		// wrong
		if (wakeLock.isHeld()) {
			wakeLock.release();
		}
		sensorManager.unregisterListener(this);
		hthread.quit();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final NervousnetRemote.Stub mBinder = new NervousnetRemote.Stub() {

		@Override
		public LightReading getLightReading() throws RemoteException {
			Log.d(LOG_TAG, "Light reading requested ");
			return lightReading;
		}

		@Override
		public BatteryReading getBatteryReading() throws RemoteException {
			Log.d(LOG_TAG, "Battery reading requested ");
			return batteryReading;
		}

		@Override
		public LocationReading getLocationReading() throws RemoteException {
			Log.d(LOG_TAG, "Location reading requested ");
			return locReading;
		}

		@Override
		public AccelerometerReading getAccelerometerReading() throws RemoteException {
			Log.d(LOG_TAG, "Accelerometer reading requested ");
			return accReading;
		}

		@Override
		public GyroReading getGyroReading() throws RemoteException {
			Log.d(LOG_TAG, "Gyroscope reading requested ");
			return gyroReading;
		}

		@Override
		public ConnectivityReading getConnectivityReading() throws RemoteException {
			Log.d(LOG_TAG, "Connectivity reading requested ");
			return connReading;
		}

		@Override
		public NoiseReading getNoiseReading() throws RemoteException {
			Log.d(LOG_TAG, "Noise reading requested ");
			return noiseReading;
		}

		@Override
		public float getStdev(int type) {
			Log.d(LOG_TAG, "Standard Deviation requested ");
			
			return 0;
		}
		
		@Override
		public float getVar(int type) {
			Log.d(LOG_TAG, "Variation requested ");
			
			return 0;
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "Service execution started");
		Toast.makeText(NervousnetHubApiService.this, "Service Started", Toast.LENGTH_SHORT).show();
		initSensors();
		return START_STICKY;
	}

	public static void startService(Context context) {
		Log.d(LOG_TAG, "inside startService");
		Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show();
		Intent sensorIntent = new Intent(context, NervousnetHubApiService.class);
		context.startService(sensorIntent);
	}

	public static void stopService(Context context) {
		Log.d(LOG_TAG, "inside stopService");
		Toast.makeText(context, "Service Stopped", Toast.LENGTH_SHORT).show();
		Intent sensorIntent = new Intent(context, NervousnetHubApiService.class);
		context.stopService(sensorIntent);
		removeNotification();

	}

	private static void removeNotification() {
		if (mNM != null)
			mNM.cancel(NOTIFICATION);
	}

	SensorConfiguration sensorConfiguration;

	private void initSensors() {
		storeMutex = new ReentrantLock();
		sensorConfiguration = SensorConfiguration.getInstance(getApplicationContext());

		// Initialize sensor manager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Hash map to register sensor collect status references
		sensorCollected = new ConcurrentHashMap<Long, SensorCollectStatus>();

		// Initialize sensor collect status from configuration
		scAccelerometer = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_ACCELEROMETER);
		scBattery = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_BATTERY);
		scLight = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_LIGHT);
		scMagnet = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_MAGNETIC);
		scProximity = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_PROXIMITY);
		scGyroscope = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_GYROSCOPE);
		scTemperature = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_TEMPERATURE);
		scHumidity = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_HUMIDITY);
		scPressure = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_PRESSURE);
		scNoise = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_NOISE);
		scBLEBeacon = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_BLEBEACON);
		scConnectivity = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_CONNECTIVITY);
		scLocation = sensorConfiguration.getInitialSensorCollectStatus(LibConstants.SENSOR_LOCATION);

		// Get references to android default sensors
		sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
		sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		// Custom sensors
		sensorBattery = BatterySensor.getInstance(getApplicationContext());
		sensorConnectivity = ConnectivitySensor.getInstance(getApplicationContext());
		sensorNoise = NoiseSensor.getInstance();
		sensorLocation = LocationSensor.getInstance(getApplicationContext());

		// Schedule all sensors (initially)
		scheduleSensor(LibConstants.SENSOR_LOCATION);
		scheduleSensor(LibConstants.SENSOR_ACCELEROMETER);
		scheduleSensor(LibConstants.SENSOR_BATTERY);
		scheduleSensor(LibConstants.SENSOR_LIGHT);
		// scheduleSensor(LibConstants.SENSOR_MAGNETIC);
		// scheduleSensor(LibConstants.SENSOR_PROXIMITY);
		scheduleSensor(LibConstants.SENSOR_GYROSCOPE);
		// scheduleSensor(LibConstants.SENSOR_TEMPERATURE);
		scheduleSensor(LibConstants.SENSOR_HUMIDITY);
		// scheduleSensor(LibConstants.SENSOR_PRESSURE);
		scheduleSensor(LibConstants.SENSOR_NOISE);
		// scheduleSensor(LibConstants.SENSOR_BLEBEACON);
		scheduleSensor(LibConstants.SENSOR_CONNECTIVITY);
	}

	private void scheduleSensor(final long sensorId) {
		Log.d(LOG_TAG, "scheduleSensor called with id = " + sensorId);
		handler = new Handler(hthread.getLooper());
		final Runnable run = new Runnable() {
			@Override
			public void run() {

				boolean doCollect = false;
				SensorCollectStatus sensorCollectStatus = null;
				long startTime = System.currentTimeMillis();

				if (sensorId == LibConstants.SENSOR_ACCELEROMETER) {
					scAccelerometer.setMeasureStart(startTime);
					doCollect = scAccelerometer.isCollect();
					doCollect = doCollect ? sensorManager.registerListener(NervousnetHubApiService.this, sensorAccelerometer,
							SensorManager.SENSOR_DELAY_NORMAL) : false;
					sensorCollectStatus = scAccelerometer;
				} else if (sensorId == LibConstants.SENSOR_PRESSURE) {
					scPressure.setMeasureStart(startTime);
					doCollect = scPressure.isCollect();
					doCollect = doCollect ? sensorManager.registerListener(NervousnetHubApiService.this, sensorPressure,
							SensorManager.SENSOR_DELAY_NORMAL) : false;
					sensorCollectStatus = scPressure;
				} else if (sensorId == LibConstants.SENSOR_GYROSCOPE) {
					doCollect = scGyroscope.isCollect();
					doCollect = doCollect ? sensorManager.registerListener(NervousnetHubApiService.this, sensorGyroscope,
							SensorManager.SENSOR_DELAY_NORMAL) : false;
					sensorCollectStatus = scGyroscope;
				} else if (sensorId == LibConstants.SENSOR_HUMIDITY) {
					scHumidity.setMeasureStart(startTime);
					doCollect = scHumidity.isCollect();
					doCollect = doCollect ? sensorManager.registerListener(NervousnetHubApiService.this, sensorHumidity,
							SensorManager.SENSOR_DELAY_NORMAL) : false;
					sensorCollectStatus = scHumidity;
				} else if (sensorId == LibConstants.SENSOR_LIGHT) {
					scLight.setMeasureStart(startTime);
					doCollect = scLight.isCollect();
					doCollect = doCollect ? sensorManager.registerListener(NervousnetHubApiService.this, sensorLight,
							SensorManager.SENSOR_DELAY_NORMAL) : false;
					sensorCollectStatus = scLight;
				} else if (sensorId == LibConstants.SENSOR_MAGNETIC) {
					scMagnet.setMeasureStart(startTime);
					doCollect = scMagnet.isCollect();
					doCollect = doCollect ? sensorManager.registerListener(NervousnetHubApiService.this, sensorMagnet,
							SensorManager.SENSOR_DELAY_NORMAL) : false;
					sensorCollectStatus = scMagnet;
				} else if (sensorId == LibConstants.SENSOR_PROXIMITY) {
					scProximity.setMeasureStart(startTime);
					doCollect = scProximity.isCollect();
					doCollect = doCollect ? sensorManager.registerListener(NervousnetHubApiService.this, sensorProximity,
							SensorManager.SENSOR_DELAY_NORMAL) : false;
					sensorCollectStatus = scProximity;
				} else if (sensorId == LibConstants.SENSOR_TEMPERATURE) {
					scTemperature.setMeasureStart(startTime);
					doCollect = scTemperature.isCollect();
					doCollect = doCollect ? sensorManager.registerListener(NervousnetHubApiService.this, sensorTemperature,
							SensorManager.SENSOR_DELAY_NORMAL) : false;
					sensorCollectStatus = scTemperature;
				} else if (sensorId == LibConstants.SENSOR_BATTERY) {
					scBattery.setMeasureStart(startTime);
					doCollect = scBattery.isCollect();
					if (doCollect) {
						sensorBattery.clearListeners();
						sensorBattery.addListener(NervousnetHubApiService.this);
						sensorBattery.start();
					}
					sensorCollectStatus = scBattery;
				} else if (sensorId == LibConstants.SENSOR_CONNECTIVITY) {
					scConnectivity.setMeasureStart(startTime);
					doCollect = scConnectivity.isCollect();
					if (doCollect) {
						sensorConnectivity.clearListeners();
						sensorConnectivity.addListener(NervousnetHubApiService.this);
						sensorConnectivity.start();
					}
					sensorCollectStatus = scConnectivity;
				} else if (sensorId == LibConstants.SENSOR_BLEBEACON) {
					// scBLEBeacon.setMeasureStart(startTime);
					// doCollect = scBLEBeacon.isCollect();
					// if (doCollect) {
					// sensorBLEBeacon.clearListeners();
					// sensorBLEBeacon.addListener(SensorService.this);
					// // Update this variable if the BLE sensor is currently
					// // unavailable
					// doCollect =
					// sensorBLEBeacon.startScanning(Math.max(scBLEBeacon.getMeasureDuration(),
					// 2000));
					// }
					// sensorCollectStatus = scBLEBeacon;
				} else if (sensorId == LibConstants.SENSOR_NOISE) {
					scNoise.setMeasureStart(startTime);
					doCollect = scNoise.isCollect();
					if (doCollect) {
						sensorNoise.clearListeners();
						sensorNoise.addListener(NervousnetHubApiService.this);
						sensorNoise.startRecording(Math.max(scNoise.getMeasureDuration(), 500));
					}
					sensorCollectStatus = scNoise;
				} else if (sensorId == LibConstants.SENSOR_LOCATION) {
					scLocation.setMeasureStart(startTime);
					Log.d(LOG_TAG, "SENSOR_LOCATION 1");
					doCollect = scLocation.isCollect();
					Log.d(LOG_TAG, "SENSOR_LOCATION 2 " + doCollect);
					if (doCollect) {
						Log.d(LOG_TAG, "Inside doCollect ");
						sensorLocation.clearListeners();
						sensorLocation.addListener(NervousnetHubApiService.this);
						sensorLocation.startLocationCollection();
					}
					sensorCollectStatus = scLocation;

				}

				if (doCollect && sensorCollectStatus != null) {
					sensorCollected.put(sensorId, sensorCollectStatus);
				}

				if (sensorCollectStatus != null) {
					long interval = sensorCollectStatus.getMeasureInterval();
					Log.d(LOG_TAG, "Logging sensor " + String.valueOf(sensorId) + " started with interval "
							+ String.valueOf(interval) + " ms");
					handler.postDelayed(this, interval);
				}

			}
		};

		// 10 seconds initial delay
		handler.postDelayed(run, 10000);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		long timestamp = System.currentTimeMillis();
		Sensor sensor = event.sensor;

		switch (sensor.getType()) {
		case Sensor.TYPE_LIGHT:
			lightReading = new LightReading(timestamp, event.values[0]);
			store(lightReading);
			Log.d(LOG_TAG, "Light data collected");
			break;
		case Sensor.TYPE_PROXIMITY:
			Log.d(LOG_TAG, "Proximity data collected");

			break;
		case Sensor.TYPE_ACCELEROMETER:
			accReading = new AccelerometerReading(timestamp, event.values);
			store(accReading);
			Log.d(LOG_TAG, "Accelerometer data collected");
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			Log.d(LOG_TAG, "Magnetic data collected");

			break;
		case Sensor.TYPE_GYROSCOPE:
			gyroReading = new GyroReading(timestamp, event.values);
			store(gyroReading);
			Log.d(LOG_TAG, "Gyroscope data collected");
			break;
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public void connectivitySensorDataReady(ConnectivityReading reading) {
		Log.d(LOG_TAG, "Connectivity data collected");
		connReading = reading;
		store(reading);
	};

	@Override
	public void noiseSensorDataReady(NoiseReading reading) {
		noiseReading = reading;
		store(noiseReading);
		Log.d(LOG_TAG, "Noise data collected");
	}

	@Override
	public void batterySensorDataReady(BatteryReading reading) {
		Log.d(LOG_TAG, "Battery data collected");
		batteryReading = reading;
		store(reading);
	}

	@Override
	public void locationSensorDataReady(LocationReading reading) {
		Log.d(LOG_TAG, "Location Data Collected");
		locReading = reading;
		store(reading);
	}

	private synchronized void store(SensorReading sensorReading) {
		if (sensorReading == null)
			return;
		storeMutex.lock();

		SensorDataImpl sensorData = convertSensorReadingToSensorData(sensorReading);
		if (sensorData != null)
			new StoreTask(getApplicationContext()).execute(sensorData);
		else
			Log.d(LOG_TAG, "sensorData is null");

		storeMutex.unlock();
	}

	private synchronized SensorDataImpl convertSensorReadingToSensorData(SensorReading reading) {
		Log.d(LOG_TAG, "convertSensorReadingToSensorData reading Type = " + reading.type);
		SensorDataImpl sensorData;

		switch (reading.type) {
		case LibConstants.SENSOR_ACCELEROMETER:
			AccelerometerReading areading = (AccelerometerReading) reading;
			sensorData = new AccelData(null, reading.timestamp, areading.getX(), areading.getY(), areading.getZ(), 0l,
					true);
			sensorData.setType(LibConstants.SENSOR_ACCELEROMETER);
			return sensorData;

		case LibConstants.SENSOR_GYROSCOPE:
			GyroReading greading = (GyroReading) reading;
			sensorData = new GyroData(null, reading.timestamp, greading.getGyroX(), greading.getGyroY(),
					greading.getGyroZ(), 0l, true);
			sensorData.setType(LibConstants.SENSOR_GYROSCOPE);
			return sensorData;

		case LibConstants.SENSOR_CONNECTIVITY:
			ConnectivityReading connReading = (ConnectivityReading) reading;
			sensorData = new ConnectivityData(null, connReading.timestamp, connReading.isConnected(),
					connReading.getNetworkType(), connReading.isRoaming(), connReading.getWifiHashId(),
					connReading.getWifiStrength(), connReading.getMobileHashId(), connReading.volatility,
					connReading.isShare);
			sensorData.setType(LibConstants.SENSOR_CONNECTIVITY);
			return sensorData;

		case LibConstants.SENSOR_LIGHT:
			LightReading lreading = (LightReading) reading;
			sensorData = new LightData(null, lreading.timestamp, lreading.getLuxValue(), lreading.volatility,
					lreading.isShare);
			sensorData.setType(LibConstants.SENSOR_LIGHT);
			return sensorData;

		case LibConstants.SENSOR_LOCATION:
			LocationReading locReading = (LocationReading) reading;
			sensorData = new LocationData(null, reading.timestamp, locReading.getLatnLong()[0],
					locReading.getLatnLong()[1], locReading.getAltitude(), 0l, true);
			sensorData.setType(LibConstants.SENSOR_LOCATION);
			return sensorData;

		default:
			return null;

		}
	}

}
