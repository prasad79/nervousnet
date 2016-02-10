/**
 * 
 */
package ch.ethz.coss.nervousnet.sample;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.LocationReading;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;
import ch.ethz.coss.nervousnet.lib.SensorReading;

/**
 * @author prasad
 *
 */
public abstract class BaseSampleActivity extends FragmentActivity {

	 NervousnetRemote mService;
	 ServiceConnection mServiceConnection;
	
	 int m_interval = 2000; // 1 seconds by default, can be changed later
	 Handler m_handler = new Handler();

	 protected static BaseFragment fragment;
	
	public BaseSampleActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(mServiceConnection == null)
		initConnection();
	}
	
	void initConnection() {

		 Log.d("BaseSampleActivity", "Inside initConnection");
		mServiceConnection = new ServiceConnection() {

			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				 Log.d("BaseSampleActivity", "Inside onServiceDisconnected 2");
				System.out.println("onServiceDisconnected");
				// TODO Auto-generated method stub
				mService = null;
				mServiceConnection = null;
				Toast.makeText(getApplicationContext(), "NervousnetRemote Service not connected", Toast.LENGTH_SHORT)
						.show();
				Log.d("NervousnetRemote", "Binding - Service disconnected");
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				System.out.println("onServiceConnected");

				 Log.d("BaseSampleActivity", "Inside onServiceConnected 2");
				// TODO Auto-generated method stub
				mService = NervousnetRemote.Stub.asInterface(service);
				System.out.println("onServiceConnected 1");

//				try {
//					count.setText(mService.getCounter() + "");
//				} catch (RemoteException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				// try {
				// BatteryReading reading = mService.getBatteryReading();
				// System.out.println("onServiceConnected 2");
				// if(reading != null)
				// counter.setText(reading.getBatteryPercent()+"");
				// else
				// counter.setText("Null object returned");
				// } catch (RemoteException e) {
				// // TODO Auto-generated catch block
				// System.out.println("Exception thrown here");
				// e.printStackTrace();
				// }
				// m_handler.post(m_statusChecker);

				startRepeatingTask();
				Toast.makeText(getApplicationContext(), "Nervousnet Remote Service Connected", Toast.LENGTH_SHORT)
						.show();
				Log.d("IRemote", "Binding is done - Service connected");
			}
		};
		if (mService == null) {
			Intent it = new Intent();
			// it.setPackage("ch.ethz.coss.nervousnet.service");
//			it.setClassName("ch.ethz.coss.nervousnet", "ch.ethz.coss.nervousnet.vm.NervousnetVMService");
			it.setClassName("ch.ethz.coss.nervousnet", "ch.ethz.coss.nervousnet.SensorService");
			// it.setAction("ch.ethz.nervousnet.VM");

			try {

				Boolean flag = bindService(it, mServiceConnection, 0);
				Log.d("DEBUG", flag.toString()); // will return "true"
				if (!flag)
					Toast.makeText(BaseSampleActivity.this,
							"Please check if the Nervousnet Remote Service is installed and running.",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(BaseSampleActivity.this, "Nervousnet Remote is running fine", Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("DEBUG", "not able to bind ! ");
			}

			// //binding to remote service
			// boolean flag = bindService(it, mServiceConnection,
			// Service.BIND_AUTO_CREATE);
			//
			//
		}
	}
	

	Runnable m_statusChecker = new Runnable() {
		@Override
		public void run() {

			 Log.d("BaseSampleActivity", "before updating");
			update(); // this function can change value of m_interval.

			m_handler.postDelayed(m_statusChecker, m_interval);
		}
	};

	void startRepeatingTask() {
		m_statusChecker.run();
	}

	void stopRepeatingTask() {
		m_handler.removeCallbacks(m_statusChecker);
	}
	
	protected abstract void updateStatus(SensorReading reading);
	
	BatteryReading breading;
	LocationReading lreading;
	AccelerometerReading aReading;
	protected void update() {
		try {
     Log.d("BaseSampleActivity", "Inside update : fragment type = "+fragment.type);
//			System.out.println("Get Battery Reading");
//
//			count.setText(mService.getCounter() + "");

			 breading = mService.getBatteryReading();
			 lreading = mService.getLocationReading();
			 aReading = mService.getAccelerometerReading();
			 
			 switch(fragment.type){
			 case 0:
				 updateStatus(aReading);
				 break;
			 case 1:
				 updateStatus(breading);
				 break;
			 case 5:
				 updateStatus(lreading);
				 break;
			 }
			
			 
			 
//			if (breading != null) {
//				System.out.println("Set Battery Reading");
//				battery_percent.setText("Charge Remaining = " + breading.getPercent() * 100 + " %");
//				battery_isCharging.setText("Charging: " + (breading.isCharging() ? "YES" : "NO"));
//				battery_isUSB.setText("USB Charging: " + (breading.getCharging_type() == 1 ? "YES" : "NO"));
//				battery_isAC.setText("AC Charging: " + (breading.getCharging_type() == 2 ? "YES" : "NO"));
//				temp.setText("Temperature: " + breading.getTemp() + " C");
//				volt.setText("Voltage: " + breading.getVolt() + " mV");
//				health.setText("Health Status: " + breading.getHealthString());
//
//			} else {
//				System.out.println("Set Location Reading");
//				battery_percent.setText("Battery sensor not responding.");
//			}
//
//			if (lreading != null) {
//
//				location_values.setText(lreading.toString());
//			}
//
//			if (aReading != null) {
//				accelX.setText("X: " + aReading.getX());
//				accelY.setText("Y: " + aReading.getY());
//				accelZ.setText("Z: " + aReading.getZ());
//
//			}
//			//

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
