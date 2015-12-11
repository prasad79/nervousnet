/**
 * 
 */
package ch.ethz.coss.nervousnet.vm;

import java.util.Hashtable;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Vibrator;
import android.widget.ListView;
import ch.ethz.coss.nervousnet.Application;
import ch.ethz.coss.nervousnet.BaseActivity;
import ch.ethz.coss.nervousnet.Constants;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.sensors.SensorDescBLEBeacon;

/**
 * @author prasad
 *
 */
public class NervousnetVMServiceHandler {
	
	private static NervousnetVMServiceHandler _instance = null;
	private static SensorManager sensorManager;
	private static List<Sensor> sensor;
	private static Hashtable<Integer, Sensor> hSensors;
	
	protected static Vibrator vibrator;
	
	
	
	private NervousnetVMServiceHandler(){
		
	}
	
	public static NervousnetVMServiceHandler getInstance() {
		
		
		if(_instance == null){
			_instance = new NervousnetVMServiceHandler();
			
		}
		
			
			return _instance;
	}
	
	public void startStopSensorService(boolean on, Context context) {
		if (on) {
			NervousnetVMService.startService(context);
			
			// If the user wants to collect BT/BLE data, ask to enable bluetooth
			// if disabled
			SensorConfiguration sc = SensorConfiguration
					.getInstance(context);
			SensorCollectStatus scs = sc
					.getInitialSensorCollectStatus(SensorDescBLEBeacon.SENSOR_ID);
			if (scs.isCollect()) {
				// This will only work on API level 18 or higher
				initializeBluetooth(context);
			}
			vibrator.vibrate(Constants.VIBRATION_DURATION * 10);

		} else {
			NervousnetVMService.stopService(context);
			vibrator.vibrate(Constants.VIBRATION_DURATION);
		}
//		updateServiceInfo();
	}
	
	
	@TargetApi(18)
	private void initializeBluetooth(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
			BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

			if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
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
		
		sensorManager = (SensorManager)   context.getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
		hSensors = new Hashtable<Integer, Sensor>();
		 for(int i=0; i<sensor.size(); i++){
			 System.out.println("Sensor "+sensor.get(i).getType()+", Name = "+sensor.get(i).getName());
			 
			 hSensors.put(sensor.get(i).getType(), sensor.get(i));
		  }
		
	}
	
	
	public static boolean isSensorSupported(int type){
		if(hSensors == null)
			return false;
		
		
		if(hSensors.containsKey(type))
			return true;
		else 
			return false;
	}
}
