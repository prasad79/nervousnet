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
package ch.ethz.coss.nervousnet.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.lib.ConnectivityReading;
import ch.ethz.coss.nervousnet.sensors.AccelerometerSensor;
import ch.ethz.coss.nervousnet.sensors.ConnectivitySensor;
import ch.ethz.coss.nervousnet.sensors.ConnectivitySensor.ConnectivitySensorListener;

public class ConnectivityActivity extends BaseSensorActivity implements ConnectivitySensorListener {

	TextView connectionStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conn_sensor);
		connectionStatus = (TextView) findViewById(R.id.statusTF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ethz.coss.nervousnet.sensors.ConnectivitySensor.
	 * ConnectivitySensorListener#connectivitySensorDataReady(ch.ethz.coss.
	 * nervousnet.vm.ConnectivityReading)
	 */
	@Override
	public void connectivitySensorDataReady(ConnectivityReading reading) {
		// TODO Auto-generated method stub
		update(reading);
	}

	@Override
	protected void onResume() {
		super.onResume();

		addListener();
		Log.d("ConnectivityActivity", "onResume() - addListener");
		// The activity has become visible (it is now "resumed").
	}

	@Override
	protected void onPause() {
		super.onPause();
		removeListener();

		Log.d("ConnectivityActivity", "onPause() - removeListener");
		// Another activity is taking focus (this activity is about to be
		// "paused").
	}

	@Override
	protected void onStop() {
		super.onStop();
		removeListener();
		Log.d("ConnectivityActivity", "onStop() - removeListener");
		// The activity is no longer visible (it is now "stopped")
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeListener();
		Log.d("ConnectivityActivity", "onDestroy() - removeListener");

		// The activity is about to be destroyed.
	}

	/**
	* 
	*/
	private void addListener() {
		// TODO Auto-generated method stub
		Log.d("ConnectivityActivity", "Inside ConnectivityActivity addListener ");

		ConnectivitySensor.getInstance(ConnectivityActivity.this).addListener(ConnectivityActivity.this);
	}

	/**
	 * 
	 */
	private void removeListener() {
		// TODO Auto-generated method stub
		Log.d("ConnectivityActivity", "Inside ConnectivityActivity removeListener ");
		ConnectivitySensor.getInstance(ConnectivityActivity.this).removeListener(ConnectivityActivity.this);

	}

	private void update(final ConnectivityReading reading) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				connectionStatus.setText("Connected = " + reading.isConnected() + "\nRoaming = " + reading.isRoaming()
						+ "\nWifi Strength = " + reading.getWifiStrength() + "\nMobile Hash ID = "
						+ reading.getMobileHashId());
			}
		});
	}

}
