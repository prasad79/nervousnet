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
 *  *     along with NervousNet. If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 *  * 	Contributors:
 *  * 	Prasad Pulikal - prasad.pulikal@gess.ethz.ch  -  Initial API and implementation
 *******************************************************************************/
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

	@Override
	public void doShare() {
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