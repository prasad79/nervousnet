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
 *******************************************************************************/
package ch.ethz.coss.nervousnet;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.LocationReading;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;

public class SensorService extends Service {
	
	private static final String LOG_TAG = SensorService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final NervousnetRemote.Stub mBinder = new NervousnetRemote.Stub() {

		@Override
		public BatteryReading getBatteryReading() throws RemoteException {
			Log.d(LOG_TAG, "Battery reading requested ");
			
			return null;
		}

		@Override
		public LocationReading getLocationReading() throws RemoteException {
			Log.d(LOG_TAG, "Location reading requested ");
			
			return null;
		}

		@Override
		public AccelerometerReading getAccelerometerReading() throws RemoteException {
			Log.d(LOG_TAG, "Accelerometer reading requested ");
			
			return null;
		}

	};
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "Service execution started");
		Toast.makeText(SensorService.this, "Service Started", Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}
	
	
	public static void startService(Context context) {
		Log.d(LOG_TAG, "inside startService");
		Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show();
		Intent sensorIntent = new Intent(context, SensorService.class);
		context.startService(sensorIntent);
	}

	public static void stopService(Context context) {
		Log.d(LOG_TAG, "inside stopService");
		Toast.makeText(context, "Service Stopped", Toast.LENGTH_SHORT).show();
		Intent sensorIntent = new Intent(context, SensorService.class);
		context.stopService(sensorIntent);
	}
}
