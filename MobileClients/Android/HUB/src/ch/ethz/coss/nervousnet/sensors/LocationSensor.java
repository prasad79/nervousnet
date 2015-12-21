package ch.ethz.coss.nervousnet.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import ch.ethz.coss.nervousnet.vm.BatteryReading;
import ch.ethz.coss.nervousnet.vm.SensorReading;

public class LocationSensor implements SensorStatusImplementation {

	public static final long SENSOR_ID = 0x0000000000000002L;

	private Context context;

	private BatteryReading reading;

	public LocationSensor(Context context) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.ethz.coss.nervousnet.sensors.SensorStatusImplementation#getReading()
	 */
	@Override
	public SensorReading getReading() {
		// TODO Auto-generated method stub
		return null;
	}

}