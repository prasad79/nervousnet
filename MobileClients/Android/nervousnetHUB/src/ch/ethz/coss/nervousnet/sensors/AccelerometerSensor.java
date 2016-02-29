package ch.ethz.coss.nervousnet.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import android.location.Location;
import android.location.LocationListener;

public class AccelerometerSensor implements SensorStatusImplementation {

	public static AccelerometerSensor _instance = null;
	public static final long SENSOR_ID = 0x0000000000000003L;

	private List<AccelerometerSensorListener> listenerList = new ArrayList<AccelerometerSensorListener>();
	private Lock listenerMutex = new ReentrantLock();

	private AccelerometerReading reading;

	private AccelerometerSensor() {
	}

	public static AccelerometerSensor getInstance() {
		Log.d("AccelerometerSensor", "getInstance called ");

		if (_instance == null) {
			Log.d("AccelerometerSensor", "instance is null ");
			_instance = new AccelerometerSensor();
		}

		return _instance;
	}

	public interface AccelerometerSensorListener {
		public void accelSensorDataReady(AccelerometerReading reading);
	}

	public void addListener(AccelerometerSensorListener listener) {

		listenerMutex.lock();
		listenerList.add(listener);
		listenerMutex.unlock();
		// Log.d("AccelerometerSensor", "Listener added "+listenerList.size());
	}

	public void removeListener(AccelerometerSensorListener listener) {
		// Log.d("AccelerometerSensor", "Listener remove "+listenerList.size());

		listenerMutex.lock();
		listenerList.remove(listener);
		listenerMutex.unlock();
	}

	public void clearListeners() {
		listenerMutex.lock();
		listenerList.clear();
		listenerMutex.unlock();
	}

	public void start() {
	}

	public void stop() {
	}

	/**
	 * @param batteryReading
	 */
	public void dataReady(AccelerometerReading reading) {
		// Log.d("AccelerometerSensor", "dataReady called
		// "+listenerList.size());

		this.reading = reading;
		listenerMutex.lock();

		for (AccelerometerSensorListener listener : listenerList) {
			// Log.d("AccelerometerSensor", "listener.accelSensorDataReady
			// calling ");

			listener.accelSensorDataReady(reading);
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

		return reading;
	}

}