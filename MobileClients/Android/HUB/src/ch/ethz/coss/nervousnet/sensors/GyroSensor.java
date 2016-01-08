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
import android.location.Location;
import android.location.LocationListener;
import ch.ethz.coss.nervousnet.vm.AccelerometerReading;
import ch.ethz.coss.nervousnet.vm.GyroReading;
import ch.ethz.coss.nervousnet.vm.SensorReading;

public class GyroSensor implements SensorStatusImplementation {

	public static GyroSensor _instance = null;
	public static final long SENSOR_ID = 0x0000000000000005L;

	private List<GyroSensorListener> listenerList = new ArrayList<GyroSensorListener>();
	private Lock listenerMutex = new ReentrantLock();

	private GyroReading reading;

	private GyroSensor() {
	}

	public static GyroSensor getInstance() {
		Log.d("GyroSensor", "getInstance called ");

		if (_instance == null) {
			Log.d("GyroSensor", "instance is null ");
			_instance = new GyroSensor();
		}

		return _instance;
	}

	public interface GyroSensorListener {
		public void gyroSensorDataReady(GyroReading reading);
	}

	public void addListener(GyroSensorListener listener) {

		listenerMutex.lock();
		listenerList.add(listener);
		listenerMutex.unlock();
		// Log.d("AccelerometerSensor", "Listener added "+listenerList.size());
	}

	public void removeListener(GyroSensorListener listener) {
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
	public void dataReady(GyroReading reading) {
		Log.d("GyroSensor", "dataReady called " + listenerList.size());
		//
		this.reading = reading;
		listenerMutex.lock();
		for (GyroSensorListener listener : listenerList) {
			// Log.d("AccelerometerSensor", "listener.accelSensorDataReady
			// calling ");
			listener.gyroSensorDataReady(reading);
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