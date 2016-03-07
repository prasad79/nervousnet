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
/**
 * 
 */
package ch.ethz.coss.nervousnet.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import ch.ethz.coss.nervousnet.NervousnetManager;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.R.layout;
import ch.ethz.coss.nervousnet.SensorService;

/**
 * @author prasad
 *
 */
public abstract class BaseActivity extends Activity {

	protected static Switch mainSwitch;

	protected View parentView;
	protected static ActionBar actionBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateActionBar();

	}

	protected void updateActionBar() {
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.ab_nn, null);

		if (actionBar == null) {

			actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setCustomView(v);
			mainSwitch = (Switch) findViewById(R.id.mainSwitch);

		}
		byte state = NervousnetManager.getInstance().getState(this);
		mainSwitch.setChecked(state == 0? false: true);
		mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				startStopSensorService(isChecked);
			}
		});

	}

	public void startStopSensorService(boolean on) {
		if (on) {
			SensorService.startService(this);
			
			// UploadService.startService(this);
			// serviceRunning = true;
			//
			// // If the user wants to collect BT/BLE data, ask to enable
			// bluetooth
			// // if disabled
			// SensorConfiguration sc =
			// SensorConfiguration.getInstance(getApplicationContext());
			// SensorCollectStatus scs =
			// sc.getInitialSensorCollectStatus(Constants.SENSOR_BLEBEACON);
			// if (scs.isCollect()) {
			// // This will only work on API level 18 or higher
			// initializeBluetooth();
			// }

		} else {
			SensorService.stopService(this);
			// UploadService.stopService(this);
			// serviceRunning = false;
		}
		
		NervousnetManager.getInstance().setState(this, on? (byte) 1: (byte) 0);
		// updateServiceInfo();
	}

	protected void startNextActivity(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
