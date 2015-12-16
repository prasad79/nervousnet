package ch.ethz.coss.nervousnet.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.util.Log;
import ch.ethz.coss.nervousnet.vm.BatteryReading;

public class BatterySensor implements SensorStatusImplementation {

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

	public void readBattery() {
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

		reading = new BatteryReading(System.currentTimeMillis(), batteryPct, isCharging, usbCharge, acCharge);
		dataReady();

	}

	/**
	 * @param batteryReading
	 */
	private void dataReady() {
		listenerMutex.lock();
		for (BatteryListener listener : listenerList) {
			listener.batterySensorDataReady(reading);
		}
		listenerMutex.unlock();
	}

	Handler m_handler = new Handler();

	public void start() {

		batterySensorRunnable.run();

	}

	void stop() {
		m_handler.removeCallbacks(batterySensorRunnable);
	}

	Runnable batterySensorRunnable = new Runnable() {
		@Override
		public void run() {
			Log.d("BatterySensor", "Running read Battery thread ");

			readBattery();
			if (m_handler != null)
				m_handler.postDelayed(batterySensorRunnable, m_interval);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.ethz.coss.nervousnet.sensors.SensorStatusImplementation#doCollect()
	 */
	@Override
	public void doCollect() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return
	 */
	@Override
	public BatteryReading getReading() {
		// TODO Auto-generated method stub
		if (reading == null)
			return new BatteryReading(System.currentTimeMillis(), (float) 0.0, true, true, true);

		return reading;
	}

	private int m_interval = 1000; // 1 seconds by default, can be changed later

}