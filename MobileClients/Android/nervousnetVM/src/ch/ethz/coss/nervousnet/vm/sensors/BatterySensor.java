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
package ch.ethz.coss.nervousnet.vm.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorEventListener;
import android.os.BatteryManager;
import android.util.Log;
import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.SensorReading;

public class BatterySensor implements SensorStatusImplementation {

	public static BatterySensor _instance;

	private Context context;

	private BatteryReading reading;

	private BatterySensor(Context context) {
		this.context = context;
	}

	public static BatterySensor getInstance(Context context) {

		if (_instance == null)
			_instance = new BatterySensor(context);

		return _instance;
	}

	private List<BatterySensorListener> listenerList = new ArrayList<BatterySensorListener>();
	private Lock listenerMutex = new ReentrantLock();

	public interface BatterySensorListener extends SensorEventListener {
		public void batterySensorDataReady(BatteryReading reading);
	}

	public void addListener(BatterySensorListener listener) {
		System.out.println("Adding listener " + listener.toString());
		listenerMutex.lock();
		listenerList.add(listener);
		listenerMutex.unlock();

		readBattery();
	}

	public void removeListener(BatterySensorListener listener) {
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

		reading = extractBatteryData(batteryStatus);
		dataReady();

	}

	BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		int scale = -1;
		int level = -1;
		int voltage = -1;
		int temp = -1;

		@Override
		public void onReceive(Context context, Intent batteryStatus) {
			reading = extractBatteryData(batteryStatus);
			dataReady();
			Log.d("BatterySensor", "Received braoadcast - " + (reading.getPercent()));
			Log.d("BatterySensor", "level is " + level + "/" + scale + ", temp is " + temp + ", voltage is " + voltage);
		}

	};

	private BatteryReading extractBatteryData(Intent batteryStatus) {
		int temp = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
		int volt = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
		byte health = (byte) batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
				|| status == BatteryManager.BATTERY_STATUS_FULL;
		int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
		boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
		float batteryPct = level / (float) scale;

		reading = new BatteryReading(System.currentTimeMillis(), batteryPct, isCharging, usbCharge,
				acCharge, temp, volt, health);
		return reading;
	}

	/**
	 * @param batteryReading
	 */
	private void dataReady() {
		Log.d("BatterySensor", "Data Ready called - " + reading.toString());

		listenerMutex.lock();
		for (BatterySensorListener listener : listenerList) {
			listener.batterySensorDataReady(reading);
		}
		listenerMutex.unlock();
	}

	public void start() {

		// readBattery(); // read initial values

		// Register to listen.
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		context.registerReceiver(batteryReceiver, filter);

	}

	void stop() {
		context.unregisterReceiver(batteryReceiver);
	}

	@Override
	public void doCollect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doShare() {
		// TODO Auto-generated method stub

	}

	@Override
	public SensorReading getReading() {

		return reading;

	}

}