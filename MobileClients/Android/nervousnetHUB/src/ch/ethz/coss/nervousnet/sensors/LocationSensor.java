package ch.ethz.coss.nervousnet.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.LocationReading;
import ch.ethz.coss.nervousnet.vm.Constants;
import ch.ethz.coss.nervousnet.vm.model.BatteryData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

public class LocationSensor implements LocationListener{

	private static final String LOG_TAG = LocationSensor.class.getSimpleName();
	
	private Context context;
	private LocationReading lastLocReading;

	public LocationSensor(Context context) {
		this.context = context;
	}

	private List<LocationDataReadyListener> listenerList = new ArrayList<LocationDataReadyListener>();
	private Lock listenerMutex = new ReentrantLock();


	public interface LocationDataReadyListener {
		public void locationSensorDataReady(LocationReading reading);
	}

	public void addListener(LocationDataReadyListener listener) {
		listenerMutex.lock();
		listenerList.add(listener);
		listenerMutex.unlock();
	}
	
	public void removeListener(LocationDataReadyListener listener) {
		listenerMutex.lock();
		listenerList.remove(listener);
		listenerMutex.unlock();
	}
	
	public void clearListeners() {
		listenerMutex.lock();
		listenerList.clear();
		listenerMutex.unlock();
	}

	public void dataReady(LocationReading reading) {
		Log.d(LOG_TAG, "LocationSensor - inside dataReady()");
		
	
		listenerMutex.lock();
		for (LocationDataReadyListener listener : listenerList) {
			listener.locationSensorDataReady(reading);
		}
		listenerMutex.unlock();
	}


	public LocationReading getLocationReading() {
		return lastLocReading;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(LOG_TAG, "LocationSensor - inside onLocationChanged()");
		if(location == null)
			return;
		
		lastLocReading = new LocationReading(Constants.SENSOR_LOCATION, System.currentTimeMillis(), new double[]{location.getLatitude(), location.getLongitude()}, location.getAltitude());
		Log.d(LOG_TAG, "LocationSensor - inside onLocationChanged() location reading = "+location.getLatitude()+", "+location.getLongitude()+", "+location.getAltitude());
		
		dataReady(lastLocReading);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	
	LocationManager locationManager;
	Criteria criteria;
	public void startLocationCollection() {
		Log.d(LOG_TAG, "LocationSensor - inside startLocationCollection()");
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setCostAllowed(false);
		String provider = locationManager.getBestProvider(criteria, false);
		onLocationChanged(locationManager.getLastKnownLocation(provider));
		locationManager.requestLocationUpdates(provider, 1000, 100, this);

		
	}

}