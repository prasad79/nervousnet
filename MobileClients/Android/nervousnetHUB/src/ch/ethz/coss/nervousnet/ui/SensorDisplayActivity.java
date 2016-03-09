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

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import ch.ethz.coss.nervousnet.Constants;
import ch.ethz.coss.nervousnet.NervousnetManager;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.SensorService;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import ch.ethz.coss.nervousnet.ui.fragments.AccelFragment;
import ch.ethz.coss.nervousnet.ui.fragments.BaseFragment;
import ch.ethz.coss.nervousnet.ui.fragments.BatteryFragment;
import ch.ethz.coss.nervousnet.ui.fragments.BeaconsFragment;
import ch.ethz.coss.nervousnet.ui.fragments.ConnectivityFragment;
import ch.ethz.coss.nervousnet.ui.fragments.DummyFragment;
import ch.ethz.coss.nervousnet.ui.fragments.GyroFragment;
import ch.ethz.coss.nervousnet.ui.fragments.HumidFragment;

public class SensorDisplayActivity extends FragmentActivity {

	private SensorDisplayPagerAdapter sapAdapter;
	private ViewPager viewPager;
	private static BaseFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateActionBar();
		setContentView(R.layout.activity_sensor_display);

		sapAdapter = new SensorDisplayPagerAdapter(getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sapAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	ActionBar actionBar;
	protected static Switch mainSwitch;

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
		Log.d("BaseActivity", "state = " + state);
		mainSwitch.setChecked(state == 0 ? false : true);

		mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				startStopSensorService(isChecked);
			}
		});
		// });

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

		NervousnetManager.getInstance().setState(this, on ? (byte) 1 : (byte) 0);
		// updateServiceInfo();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class SensorDisplayPagerAdapter extends FragmentStatePagerAdapter {

		public SensorDisplayPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {

			switch (i) {
			case 0:
				fragment = new AccelFragment(0);
				break;
			case 1:
				fragment = new BatteryFragment(1);
				break;
			case 2:
				fragment = new BeaconsFragment(2);
				break;
			case 3:
				fragment = new ConnectivityFragment(3);
				break;
			case 4:
				fragment = new GyroFragment(4);
				break;
			case 5:
				fragment = new HumidFragment(5);
				break;

			default:
				fragment = new DummyFragment(-1);
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return Constants.sensor_labels.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return Constants.sensor_labels[position];
		}
	}

	protected void updateStatus(SensorReading reading) {
		Log.d("SensorDisplayActivity", "Inside updateStatus");
		fragment.updateReadings(reading);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
