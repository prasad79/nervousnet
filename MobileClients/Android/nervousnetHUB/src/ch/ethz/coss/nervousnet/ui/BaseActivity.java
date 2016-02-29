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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateActionBar();

	}

	protected void updateActionBar() {
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.ab_nn, null);

		ActionBar actionBar = getActionBar();

		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(v);

		 mainSwitch = (Switch) findViewById(R.id.mainSwitch);
		
		
		 mainSwitch.setOnCheckedChangeListener(new
		 CompoundButton.OnCheckedChangeListener() {
		 @Override
		 public void onCheckedChanged(CompoundButton buttonView, boolean
		 isChecked) {
		 startStopSensorService(isChecked);
		 }
		 });

	}
	
	public void startStopSensorService(boolean on) {
		if (on) {
			SensorService.startService(this);
//			UploadService.startService(this);
//			serviceRunning = true;
//
//			// If the user wants to collect BT/BLE data, ask to enable bluetooth
//			// if disabled
//			SensorConfiguration sc = SensorConfiguration.getInstance(getApplicationContext());
//			SensorCollectStatus scs = sc.getInitialSensorCollectStatus(Constants.SENSOR_BLEBEACON);
//			if (scs.isCollect()) {
//				// This will only work on API level 18 or higher
//				initializeBluetooth();
//			}

		} else {
			SensorService.stopService(this);
//			UploadService.stopService(this);
//			serviceRunning = false;
		}
//		updateServiceInfo();
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
