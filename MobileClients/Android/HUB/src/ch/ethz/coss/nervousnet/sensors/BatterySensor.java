package ch.ethz.coss.nervousnet.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import ch.ethz.coss.nervousnet.vm.BatteryReading;

public class BatterySensor implements SensorStatusImplementation{
	
	public static final long SENSOR_ID = 0x0000000000000001L;

	private Context context;

	private BatteryReading reading;
	
	public BatterySensor(Context context) {
		this.context = context;
	}

	private List<BatteryListener> listenerList = new ArrayList<BatteryListener>();
	private Lock listenerMutex = new ReentrantLock();

	public interface BatteryListener {
		public void batterySensorDataReady(BatteryReading reading);
	}

	public void addListener(BatteryListener listener) {
		listenerMutex.lock();
		listenerList.add(listener);
		listenerMutex.unlock();
	}

	public void removeListener(BatteryListener listener) {
		listenerMutex.lock();
		listenerList.remove(listener);
		listenerMutex.unlock();
	}

	public void clearListeners() {
		listenerMutex.lock();
		listenerList.clear();
		listenerMutex.unlock();
	}

	public void dataReady(long timestamp, float batteryPercent, boolean isCharging, boolean isUsbCharge,
			boolean isAcCharge) {
		listenerMutex.lock();
		reading = new BatteryReading(timestamp, batteryPercent, isCharging, isUsbCharge, isAcCharge);
		
		for (BatteryListener listener : listenerList) {
			listener.batterySensorDataReady(reading);
		}
		listenerMutex.unlock();
	}

	public class BatteryTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			Intent batteryStatus = context.registerReceiver(null, ifilter);
			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
					|| status == BatteryManager.BATTERY_STATUS_FULL;
			int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
			boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
			boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
			float batteryPct = level / (float) scale;
			dataReady(System.currentTimeMillis(), batteryPct, isCharging, usbCharge, acCharge);
			return null;
		}

	}

	public void start() {
		new BatteryTask().execute();
	}

	/* (non-Javadoc)
	 * @see ch.ethz.coss.nervousnet.sensors.SensorStatusImplementation#doCollect()
	 */
	@Override
	public void doCollect() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	public BatteryReading getReading() {
		// TODO Auto-generated method stub
		return reading;
	}

}