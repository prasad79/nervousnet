/**
 * 
 */
package ch.ethz.coss.nervousnet.vm;

import java.util.Hashtable;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import ch.ethz.coss.nervousnet.BaseActivity;
import ch.ethz.coss.nervousnet.Constants;
import ch.ethz.coss.nervousnet.sensors.AccelerometerSensor;
import ch.ethz.coss.nervousnet.sensors.BatterySensor;
import ch.ethz.coss.nervousnet.sensors.BatterySensor.BatterySensorListener;
import ch.ethz.coss.nervousnet.sensors.ConnectivitySensor;
import ch.ethz.coss.nervousnet.sensors.ConnectivitySensor.ConnectivitySensorListener;
import ch.ethz.coss.nervousnet.sensors.LocationSensor;
import ch.ethz.coss.nervousnet.sensors.LocationSensor.LocationSensorListener;

/**
 * @author prasad
 *
 */
public class NervousnetVMServiceHandler {

	private static NervousnetVMServiceHandler _instance = null;
	


	private static Runnable run;
	private static Handler handler;
	private static Runnable runnable;
	private final int runTime = 1000;

	
	private static SensorManager sensorManager;
	private static List<Sensor> sensor;
	private static Hashtable<Integer, Sensor> hSensors;

	protected static Vibrator vibrator;
	

	protected BatterySensor sensorBattery = null;
	protected ConnectivitySensor sensorConnectivity= null;
	protected LocationSensor sensorLocation = null;
//	protected AccelerometerSensor sensorAccel = null;
//	protected LightSensor sensorLight;
	protected static int counter = 0;

	private NervousnetVMServiceHandler() {

		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				counter++;
//				Toast.makeText(NervousnetVMService.this, "" + counter, Toast.LENGTH_SHORT).show();
				if (handler != null)
					handler.postDelayed(runnable, runTime);
			}
		};
		handler.post(runnable);
	}

	public static NervousnetVMServiceHandler getInstance() {

		if (_instance == null) {
			_instance = new NervousnetVMServiceHandler();

		}

		return _instance;
	}
	 

	
	protected void scheduleSensor(final long sensorId, final Context context, final SensorEventListener listener) {
		Log.d("NervousnetVMServiceHandler", "scheduleSensor called");
		
		run = new Runnable() {
			@Override
			public void run() {
				Log.d("NervousnetVMServiceHandler", "Running Schedule Sensor thread");
				if (sensorId == BatterySensor.SENSOR_ID) {
					startBatterySensor(context, (BatterySensorListener)listener);
					Log.d("NervousnetVMServiceHandler", "starting battery sensor");
				} else if (sensorId == Constants.SENSOR_LOCATION) {
					startLocationSensor(context, (LocationSensorListener)listener);
					Log.d("NervousnetVMServiceHandler", "starting location sensor");
				}else if (sensorId == Constants.SENSOR_ACCELEROMETER) {
					
					registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
					Log.d("NervousnetVMServiceHandler", "Registered for Accelerometer sensor");
				}else if (sensorId == Constants.SENSOR_LIGHT) {
					registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
					Log.d("NervousnetVMServiceHandler", "Registered for Light sensor");
				}else if (sensorId == Constants.SENSOR_GYRO) {
					registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
					Log.d("NervousnetVMServiceHandler", "Registered for GYro sensor");
				} else if (sensorId == Constants.SENSOR_CONNECTIVITY) {
					startConnectivitySensor(context, (ConnectivitySensorListener)listener);
					Log.d("NervousnetVMServiceHandler", "starting connectivity sensor");
				}

				
//				Toast.makeText(NervousnetVMServiceHandler.this, "Finished Running Schedule Sensor thread", Toast.LENGTH_LONG).show();

			}
		};
		handler.postDelayed(run, 500);
	}


	public void startStopSensorService(boolean on, Context context) {
		if (on) {
			NervousnetVMService.startService(context);

			// If the user wants to collect BT/BLE data, ask to enable bluetooth
			// if disabled
			SensorConfiguration sc = SensorConfiguration.getInstance(context);
//			SensorCollectStatus scs = sc.getInitialSensorCollectStatus(SensorDescBLEBeacon.SENSOR_ID);
//			if (scs.isCollect()) {
//				// This will only work on API level 18 or higher
//				initializeBluetooth(context);
//			}
			vibrator.vibrate(Constants.VIBRATION_DURATION * 10);

		} else {
			NervousnetVMService.stopService(context);
			vibrator.vibrate(Constants.VIBRATION_DURATION);
		}
		// updateServiceInfo();
	}

	@TargetApi(18)
	private void initializeBluetooth(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
			BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

			if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				((BaseActivity) context).startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
			}
		}
	}

	public boolean isNervousNetVMServiceRunning(Context context, Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public void initAvailableSensors(Context context) {
		if (vibrator == null)
			vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
		hSensors = new Hashtable<Integer, Sensor>();
		for (int i = 0; i < sensor.size(); i++) {
			System.out.println("Sensor " + sensor.get(i).getType() + ", Name = " + sensor.get(i).getName());

			hSensors.put(sensor.get(i).getType(), sensor.get(i));
		}

	}

	public static boolean isSensorSupported(int type) {
		if (hSensors == null)
			return false;

		if (hSensors.containsKey(type))
			return true;
		else
			return false;
	}

	
	public void registerListener(SensorEventListener listener, Sensor sensor){
		 sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	

	private void startBatterySensor(Context context, BatterySensorListener listener) {
		sensorBattery = BatterySensor.getInstance(context);
		sensorBattery.addListener(listener);
		sensorBattery.start();
	}
	
	private void startConnectivitySensor(Context context, ConnectivitySensorListener listener) {
		sensorConnectivity = ConnectivitySensor.getInstance(context);
		sensorConnectivity.addListener(listener);
		sensorConnectivity.start();
	}

	private void startLocationSensor(Context context, LocationSensorListener listener) {
		sensorLocation = LocationSensor.getInstance( context);
		sensorLocation.addListener(listener);
		sensorLocation.start();

	}

	/**
	 * 
	 */
	public void cleanup() {
		runnable = null;
		handler = null;
		_instance = null;
	}

}
