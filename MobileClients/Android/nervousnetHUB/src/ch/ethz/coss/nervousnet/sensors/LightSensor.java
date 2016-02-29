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
import ch.ethz.coss.nervousnet.lib.LightReading;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import android.location.Location;
import android.location.LocationListener;

public class LightSensor implements SensorStatusImplementation {

	public static LightSensor _instance = null;
	public static final long SENSOR_ID = 0x0000000000000004L;

	private List<LightSensorListener> listenerList = new ArrayList<LightSensorListener>();
	private Lock listenerMutex = new ReentrantLock();

	private LightReading reading;

	private LightSensor() {
	}

	public static LightSensor getInstance() {
		Log.d("LightSensor", "getInstance called ");

		if (_instance == null) {
			Log.d("LightSensor", "instance is null ");
			_instance = new LightSensor();
		}

		return _instance;
	}

	public interface LightSensorListener {
		public void lightSensorDataReady(LightReading reading);
	}

	public void addListener(LightSensorListener listener) {

		listenerMutex.lock();
		listenerList.add(listener);
		listenerMutex.unlock();
	}

	public void removeListener(LightSensorListener listener) {
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
	public void dataReady(LightReading reading) {
		// Log.d("AccelerometerSensor", "dataReady called
		// "+listenerList.size());
		//
		this.reading = reading;
		listenerMutex.lock();
		for (LightSensorListener listener : listenerList) {
			// Log.d("AccelerometerSensor", "listener.accelSensorDataReady
			// calling ");
			listener.lightSensorDataReady(reading);
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