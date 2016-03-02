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
 *  *     along with SwarmPulse. If not, see <http://www.gnu.org/licenses/>.
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
import ch.ethz.coss.nervousnet.lib.LocationReading;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import android.location.Location;
import android.location.LocationListener;

public class LocationSensor implements SensorStatusImplementation, LocationListener {

	public static LocationSensor _instance = null;
	public static final long SENSOR_ID = 0x0000000000000002L;
	private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
	private static final long MIN_TIME_BW_UPDATES = 1;

	private Context context;
	private boolean isGPSEnabled = false;

	// flag for network status
	private boolean isNetworkEnabled = false;

	private boolean canGetLocation = false;

	private LocationReading reading;
	private Location location;

	private LocationSensor(Context context) {
		this.context = context;
	}

	public static LocationSensor getInstance(Context context) {

		if (_instance == null)
			_instance = new LocationSensor(context);

		return _instance;
	}

	private List<LocationSensorListener> listenerList = new ArrayList<LocationSensorListener>();
	private Lock listenerMutex = new ReentrantLock();

	public interface LocationSensorListener extends SensorEventListener {
		public void locationSensorDataReady(LocationReading reading);
	}

	public void addListener(LocationSensorListener listener) {
		listenerMutex.lock();
		listenerList.add(listener);
		listenerMutex.unlock();

		if (reading != null)
			dataReady();
	}

	public void removeListener(LocationSensorListener listener) {
		listenerMutex.lock();
		listenerList.remove(listener);
		listenerMutex.unlock();
	}

	public void clearListeners() {
		listenerMutex.lock();
		listenerList.clear();
		listenerMutex.unlock();
	}

	LocationManager locationManager;

	public void start() {

		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		// getting GPS status
		isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// getting network status
		isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled && !isNetworkEnabled) {
			// no network provider is enabled
			Toast.makeText(context, "Location settings disabled", Toast.LENGTH_LONG).show();
		} else {
			this.canGetLocation = true;
			// First get location from Network Provider
			if (isNetworkEnabled) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d("Network", "Network");
				if (locationManager != null) {
					location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {

						reading = new LocationReading((System.currentTimeMillis() / 1000),
								new double[] { location.getLatitude(), location.getLongitude() },
								location.getAltitude());
					}
				}
			}
			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				if (location == null) {
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("GPS Enabled", "GPS Enabled");
					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							reading = new LocationReading((System.currentTimeMillis() / 1000),
									new double[] { location.getLatitude(), location.getLongitude() },
									location.getAltitude());
						}
					}
				}
			}
			dataReady();
		}

	}

	void stop() {
		locationManager.removeUpdates(LocationSensor.this);
	}

	/**
	 * @param batteryReading
	 */
	private void dataReady() {
		if (reading != null) {
			listenerMutex.lock();
			for (LocationSensorListener listener : listenerList) {
				System.out.println("Sending Location Reading");

				listener.locationSensorDataReady(reading);
			}
			listenerMutex.unlock();
		}

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
		if (reading == null) {
			Log.d("NervousnetVMService", "Location reading is null " + reading);

			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			// TODO null value can be returned here.

			return new LocationReading((System.currentTimeMillis() / 1000),
					new double[] { location.getLatitude(), location.getLongitude() }, location.getAltitude());

		}
		return reading;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onLocationChanged(android.location.
	 * Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		reading = new LocationReading((int) (System.currentTimeMillis() / 1000),
				new double[] { location.getLatitude(), location.getLongitude() }, location.getAltitude());

		dataReady();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String,
	 * int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}