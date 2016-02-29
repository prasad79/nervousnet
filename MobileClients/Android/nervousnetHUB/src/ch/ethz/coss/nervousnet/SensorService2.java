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
 *  *     along with SwarmPulse. If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 *******************************************************************************/
package ch.ethz.coss.nervousnet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.ConnectivityReading;
import ch.ethz.coss.nervousnet.lib.GyroReading;
import ch.ethz.coss.nervousnet.lib.LightReading;
import ch.ethz.coss.nervousnet.lib.LocationReading;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;
import ch.ethz.coss.nervousnet.lib.NoiseReading;
import ch.ethz.coss.nervousnet.lib.ProximityReading;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import ch.ethz.coss.nervousnet.vm.model.AccelData;
import ch.ethz.coss.nervousnet.vm.model.ConnectivityData;
import ch.ethz.coss.nervousnet.vm.model.GyroData;
import ch.ethz.coss.nervousnet.vm.model.HumidityData;
import ch.ethz.coss.nervousnet.vm.model.LightData;
import ch.ethz.coss.nervousnet.vm.model.LocationData;
import ch.ethz.coss.nervousnet.vm.model.MagneticData;
import ch.ethz.coss.nervousnet.vm.model.PressureData;
import ch.ethz.coss.nervousnet.vm.model.ProximityData;
import ch.ethz.coss.nervousnet.vm.model.SensorDataImpl;
import ch.ethz.coss.nervousnet.vm.model.TemperatureData;
import ch.ethz.coss.nervousnet.sensors.BatterySensor;
import ch.ethz.coss.nervousnet.sensors.BatterySensor.BatterySensorListener;
import ch.ethz.coss.nervousnet.sensors.ConnectivitySensor;
import ch.ethz.coss.nervousnet.sensors.ConnectivitySensor.ConnectivitySensorListener;
import ch.ethz.coss.nervousnet.sensors.LocationSensor;
import ch.ethz.coss.nervousnet.sensors.LocationSensor.LocationSensorListener;
import ch.ethz.coss.nervousnet.sensors.NoiseSensor;
import ch.ethz.coss.nervousnet.sensors.NoiseSensor.NoiseListener;

public class SensorService2 extends Service
		implements SensorEventListener, NoiseListener, BatterySensorListener, ConnectivitySensorListener, LocationSensorListener {

	private static final String LOG_TAG = SensorService2.class.getSimpleName();

	private SensorManager sensorManager = null;

	private PowerManager.WakeLock wakeLock;
	private HandlerThread hthread;
	private Handler handler;
	private Lock storeMutex;

//	private SensorConfiguration sensorConfiguration;
	private SensorService2 sensorListenerClass;

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
//	private BLESensor sensorBLEBeacon = null;
	private LocationSensor sensorLocation = null;

	// Those need to be reset on every collect call
//	private SensorCollectStatus scAccelerometer = null;
//	private SensorCollectStatus scBattery = null;
//	private SensorCollectStatus scLight = null;
//	private SensorCollectStatus scMagnet = null;
//	private SensorCollectStatus scProximity = null;
//	private SensorCollectStatus scGyroscope = null;
//	private SensorCollectStatus scTemperature = null;
//	private SensorCollectStatus scHumidity = null;
//	private SensorCollectStatus scPressure = null;
//	private SensorCollectStatus scNoise = null;
//	private SensorCollectStatus scBLEBeacon = null;
//	private SensorCollectStatus scConnectivity = null;
//	private SensorCollectStatus scLocation = null;

	// Threadsafe because handling can get called from different threads
//	private ConcurrentHashMap<Long, SensorCollectStatus> sensorCollected;


	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final NervousnetRemote.Stub mBinder = new NervousnetRemote.Stub() {

		@Override
		public BatteryReading getBatteryReading() throws RemoteException {
			Log.d("SensorService", "Battery reading requested ");
			
			return null;
		}

		@Override
		public LocationReading getLocationReading() throws RemoteException {
			Log.d("SensorService", "Location reading requested ");
			
			return null;
		}

		@Override
		public AccelerometerReading getAccelerometerReading() throws RemoteException {
			Log.d("SensorService", "Accelerometer reading requested ");
			
			return null;
		}

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		storeMutex = new ReentrantLock();
//
//		// Reference for inner runnable
//		sensorListenerClass = this;
//		sensorConfiguration = SensorConfiguration.getInstance(getApplicationContext());
//
//		// Initialize sensor manager
//		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//
//		// Hash map to register sensor collect status references
//		sensorCollected = new ConcurrentHashMap<Long, SensorCollectStatus>();
//
//		// Initialize sensor collect status from configuration
//		scAccelerometer = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_ACCELEROMETER);
//		scBattery = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_BATTERY);
//		scLight = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_LIGHT);
//		scMagnet = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_MAGNETIC);
//		scProximity = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_PROXIMITY);
//		scGyroscope = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_GYROSCOPE);
//		scTemperature = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_TEMPERATURE);
//		scHumidity = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_HUMIDITY);
//		scPressure = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_PRESSURE);
//		scNoise = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_NOISE);
//		scBLEBeacon = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_BLEBEACON);
//		scConnectivity = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_CONNECTIVITY);
//		scLocation = sensorConfiguration.getInitialSensorCollectStatus(Constants.SENSOR_LOCATION);
//
//		// Get references to android default sensors
//		sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//		sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//		sensorMagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//		sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//		sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//		sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
//		sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
//		sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
//		
//		// Custom sensors
//		sensorBattery = new BatterySensor(getApplicationContext());
//		sensorConnectivity = new ConnectivitySensor(getApplicationContext());
//		sensorBLEBeacon = new BLESensor(getApplicationContext());
//		sensorNoise = new NoiseSensor();
//		sensorLocation = new LocationSensor(getApplicationContext());
//
//		// Schedule all sensors (initially)
//		scheduleSensor(Constants.SENSOR_LOCATION);
//		scheduleSensor(Constants.SENSOR_ACCELEROMETER);
//		scheduleSensor(Constants.SENSOR_BATTERY);
//		scheduleSensor(Constants.SENSOR_MAGNETIC);
//		scheduleSensor(Constants.SENSOR_PROXIMITY);
//		scheduleSensor(Constants.SENSOR_GYROSCOPE);
//		scheduleSensor(Constants.SENSOR_TEMPERATURE);
//		scheduleSensor(Constants.SENSOR_HUMIDITY);
//		scheduleSensor(Constants.SENSOR_PRESSURE);
//		scheduleSensor(Constants.SENSOR_NOISE);
//		scheduleSensor(Constants.SENSOR_BLEBEACON);
//		scheduleSensor(Constants.SENSOR_CONNECTIVITY);
//
		Log.d(LOG_TAG, "Service execution started");
		Toast.makeText(SensorService2.this, "Service Started", Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}

//	private void scheduleSensor(final long sensorId) {
//		Log.d(LOG_TAG, "scheduleSensor called.");
//		handler = new Handler(hthread.getLooper());
//		final Runnable run = new Runnable() {
//			@Override
//			public void run() {
//
//				boolean doCollect = false;
//				SensorCollectStatus sensorCollectStatus = null;
//				long startTime = System.currentTimeMillis();
//
//				if (sensorId == Constants.SENSOR_ACCELEROMETER) {
//					scAccelerometer.setMeasureStart(startTime);
//					doCollect = scAccelerometer.isCollect();
//					doCollect = doCollect ? sensorManager.registerListener(sensorListenerClass, sensorAccelerometer,
//							SensorManager.SENSOR_DELAY_NORMAL) : false;
//					sensorCollectStatus = scAccelerometer;
//				} else if (sensorId == Constants.SENSOR_PRESSURE) {
//					scPressure.setMeasureStart(startTime);
//					doCollect = scPressure.isCollect();
//					doCollect = doCollect ? sensorManager.registerListener(sensorListenerClass, sensorPressure,
//							SensorManager.SENSOR_DELAY_NORMAL) : false;
//					sensorCollectStatus = scPressure;
//				} else if (sensorId == Constants.SENSOR_GYROSCOPE) {
//					doCollect = scGyroscope.isCollect();
//					doCollect = doCollect ? sensorManager.registerListener(sensorListenerClass, sensorGyroscope,
//							SensorManager.SENSOR_DELAY_NORMAL) : false;
//					sensorCollectStatus = scGyroscope;
//				} else if (sensorId == Constants.SENSOR_HUMIDITY) {
//					scHumidity.setMeasureStart(startTime);
//					doCollect = scHumidity.isCollect();
//					doCollect = doCollect ? sensorManager.registerListener(sensorListenerClass, sensorHumidity,
//							SensorManager.SENSOR_DELAY_NORMAL) : false;
//					sensorCollectStatus = scHumidity;
//				} else if (sensorId == Constants.SENSOR_LIGHT) {
//					scLight.setMeasureStart(startTime);
//					doCollect = scLight.isCollect();
//					doCollect = doCollect ? sensorManager.registerListener(sensorListenerClass, sensorLight,
//							SensorManager.SENSOR_DELAY_NORMAL) : false;
//					sensorCollectStatus = scLight;
//				} else if (sensorId == Constants.SENSOR_MAGNETIC) {
//					scMagnet.setMeasureStart(startTime);
//					doCollect = scMagnet.isCollect();
//					doCollect = doCollect ? sensorManager.registerListener(sensorListenerClass, sensorMagnet,
//							SensorManager.SENSOR_DELAY_NORMAL) : false;
//					sensorCollectStatus = scMagnet;
//				} else if (sensorId == Constants.SENSOR_PROXIMITY) {
//					scProximity.setMeasureStart(startTime);
//					doCollect = scProximity.isCollect();
//					doCollect = doCollect ? sensorManager.registerListener(sensorListenerClass, sensorProximity,
//							SensorManager.SENSOR_DELAY_NORMAL) : false;
//					sensorCollectStatus = scProximity;
//				} else if (sensorId == Constants.SENSOR_TEMPERATURE) {
//					scTemperature.setMeasureStart(startTime);
//					doCollect = scTemperature.isCollect();
//					doCollect = doCollect ? sensorManager.registerListener(sensorListenerClass, sensorTemperature,
//							SensorManager.SENSOR_DELAY_NORMAL) : false;
//					sensorCollectStatus = scTemperature;
//				} else if (sensorId == Constants.SENSOR_BATTERY) {
//					scBattery.setMeasureStart(startTime);
//					doCollect = scBattery.isCollect();
//					if (doCollect) {
//						sensorBattery.clearListeners();
//						sensorBattery.addListener(sensorListenerClass);
//						sensorBattery.start();
//					}
//					sensorCollectStatus = scBattery;
//				} else if (sensorId == Constants.SENSOR_CONNECTIVITY) {
//					scConnectivity.setMeasureStart(startTime);
//					doCollect = scConnectivity.isCollect();
//					if (doCollect) {
//						sensorConnectivity.clearListeners();
//						sensorConnectivity.addListener(sensorListenerClass);
//						sensorConnectivity.start();
//					}
//					sensorCollectStatus = scConnectivity;
//				} else if (sensorId == Constants.SENSOR_BLEBEACON) {
//					scBLEBeacon.setMeasureStart(startTime);
//					doCollect = scBLEBeacon.isCollect();
//					if (doCollect) {
//						sensorBLEBeacon.clearListeners();
//						sensorBLEBeacon.addListener(sensorListenerClass);
//						// Update this variable if the BLE sensor is currently
//						// unavailable
//						doCollect = sensorBLEBeacon.startScanning(Math.max(scBLEBeacon.getMeasureDuration(), 2000));
//					}
//					sensorCollectStatus = scBLEBeacon;
//				} else if (sensorId == Constants.SENSOR_NOISE) {
//					scNoise.setMeasureStart(startTime);
//					doCollect = scNoise.isCollect();
//					if (doCollect) {
//						sensorNoise.clearListeners();
//						sensorNoise.addListener(sensorListenerClass);
//						sensorNoise.startRecording(Math.max(scNoise.getMeasureDuration(), 500));
//					}
//					sensorCollectStatus = scNoise;
//				} else if (sensorId == Constants.SENSOR_LOCATION) {
//					scLocation.setMeasureStart(startTime);
//					doCollect = scLocation.isCollect();
//					if (doCollect) {
//						sensorLocation.clearListeners();
//						sensorLocation.addListener(sensorListenerClass);
//						sensorLocation.startLocationCollection();
//					}
//					sensorCollectStatus = scLocation;
//					
//				}
//
//				if (doCollect && sensorCollectStatus != null) {
//					sensorCollected.put(sensorId, sensorCollectStatus);
//				}
//
//				if (sensorCollectStatus != null) {
//					long interval = sensorCollectStatus.getMeasureInterval();
//					Log.d(LOG_TAG, "Logging sensor " + String.valueOf(sensorId) + " started with interval "
//							+ String.valueOf(interval) + " ms");
//					handler.postDelayed(this, interval);
//				}
//
//			}
//		};
//		
//		// 10 seconds initial delay
//		handler.postDelayed(run, 10000);
//	}

	// @Override
	// public IBinder onBind(Intent intent) {
	// return mBinder;
	// }

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
	}

	@Override
	public void onDestroy() {
		// Release the wakelock here, just to be safe, in order something went
		// wrong
		if (wakeLock.isHeld()) {
			wakeLock.release();
		}
		sensorManager.unregisterListener(this);
		hthread.quit();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do nothing

	}
	
	
	private LocationReading locReading = null;
	private AccelerometerReading accReading = null;
	private BatteryReading batteryReading = null;
	private ConnectivityReading connReading = null;
	private GyroReading gyroReading = null;
	private NoiseReading noiseReading = null;
	private LightReading lightReading = null;

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
//		case Sensor.TYPE_AMBIENT_TEMPERATURE:
//			sensorReading = new TemperatureReading(timestamp, event.values[0]);
//			Log.d(LOG_TAG, "Temperature data collected");
//			break;
		case Sensor.TYPE_RELATIVE_HUMIDITY:
//			sensorReading = new HumidityData(timestamp, event.values[0]);
			Log.d(LOG_TAG, "Humidity data collected");
			break;
		case Sensor.TYPE_PRESSURE:
//			sensorReading = new PressureData(timestamp, event.values[0]);
			Log.d(LOG_TAG, "Pressure data collected");
			break;
		}
		
	
	}

	
	
	public void connectivitySensorDataReady(ConnectivityReading reading) {
		store(reading);
	};
	
//	@Override
//	public void connectivitySensorDataReady(long timestamp, boolean isConnected, int networkType, boolean isRoaming,
//			String wifiHashId, int wifiStrength, String mobileHashId) {
//		
//		SensorReading connReading = new ConnectivityReading(timestamp, isConnected, networkType, isRoaming, wifiHashId, wifiStrength, mobileHashId);
//		Log.d(LOG_TAG, "Connectivity data collected");
//	
//		store(connReading);
//	}

	@Override
	public void noiseSensorDataReady(long timestamp, float rms, float spl, float[] bands) {
//		SensorReading Reading = new NoiseRea(timestamp, rms, spl, bands);
		Log.d(LOG_TAG, "Noise data collected");
//		store(sensorDesc.getSensorId(), sensorDescs);
	}



	@Override
	public void batterySensorDataReady(BatteryReading reading) {
		
		Log.d(LOG_TAG, "Battery data collected");
		store(reading);
	}


	
	
	@Override
	public void locationSensorDataReady(LocationReading reading) {
		Log.d(LOG_TAG, "inside locationSensorDataReady()");
		store(reading);
	}

	private synchronized void store(SensorReading sensorReading) {
		if(sensorReading == null)
			return;
		storeMutex.lock();
		
		SensorDataImpl sensorData = convertSensorReadingToSensorData(sensorReading);
		if(sensorData != null)
			new StoreTask(getApplicationContext()).execute(sensorData);
		else
			Log.d(LOG_TAG, "sensorData is null");
					
		storeMutex.unlock();
	}

	private synchronized SensorDataImpl convertSensorReadingToSensorData(SensorReading reading) {
		Log.d(LOG_TAG, "convertSensorReadingToSensorData reading Type = "+reading.type);
		SensorDataImpl sensorData;
		
		switch(reading.type) {
		
		case Constants.SENSOR_ACCELEROMETER:
			AccelerometerReading areading = (AccelerometerReading) reading;
			sensorData = new AccelData(reading.timestamp, areading.getX(),  areading.getY(), areading.getZ(), 0l, true);
			sensorData.setType(Constants.SENSOR_ACCELEROMETER);
			return sensorData;		
			
		case Constants.SENSOR_LOCATION:
			LocationReading locReading = (LocationReading) reading;
			sensorData = new LocationData(reading.timestamp, locReading.getLatnLong()[0], locReading.getLatnLong()[1], locReading.getAltitude(), 0l, true);
			sensorData.setType(Constants.SENSOR_LOCATION);
			return sensorData;
		
			
		default:
			return null;
			
		}
	}
	
//	private void unregisterSensor(long sensorId) {
//		if (sensorId == SensorDescAccelerometer.SENSOR_ID) {
//			sensorManager.unregisterListener(this, sensorAccelerometer);
//		} else if (sensorId == SensorDescPressure.SENSOR_ID) {
//			sensorManager.unregisterListener(this, sensorPressure);
//		} else if (sensorId == SensorDescGyroscope.SENSOR_ID) {
//			sensorManager.unregisterListener(this, sensorGyroscope);
//		} else if (sensorId == SensorDescHumidity.SENSOR_ID) {
//			sensorManager.unregisterListener(this, sensorHumidity);
//		} else if (sensorId == SensorDescLight.SENSOR_ID) {
//			sensorManager.unregisterListener(this, sensorLight);
//		} else if (sensorId == SensorDescMagnetic.SENSOR_ID) {
//			sensorManager.unregisterListener(this, sensorMagnet);
//		} else if (sensorId == SensorDescProximity.SENSOR_ID) {
//			sensorManager.unregisterListener(this, sensorProximity);
//		} else if (sensorId == SensorDescTemperature.SENSOR_ID) {
//			sensorManager.unregisterListener(this, sensorTemperature);
//		} else if (sensorId == SensorDescNoise.SENSOR_ID) {
//			sensorNoise.removeListener(this);
//		} else if (sensorId == SensorDescBattery.SENSOR_ID) {
//			sensorBattery.removeListener(this);
//		} else if (sensorId == SensorDescBLEBeacon.SENSOR_ID) {
//			sensorBLEBeacon.removeListener(this);
//		} else if (sensorId == SensorDescConnectivity.SENSOR_ID) {
//			sensorConnectivity.removeListener(this);
//		}
//	}

	public static boolean isServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (SensorService2.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static void startService(Context context) {
		Intent sensorIntent = new Intent(context, SensorService2.class);
		context.startService(sensorIntent);
	}

	public static void stopService(Context context) {
		Intent sensorIntent = new Intent(context, SensorService2.class);
		context.stopService(sensorIntent);
	}

	

	
	
}
