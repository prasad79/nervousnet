/**
 * 
 */
package ch.ethz.coss.nervousnet;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import ch.ethz.coss.nervousnet.vm.NervousnetVMService;
import ch.ethz.coss.nervousnet.vm.NervousnetVMServiceHandler;

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

	private void updateActionBar() {
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.ab_nn, null);

		ActionBar actionBar = getActionBar();

		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(v);

		mainSwitch = (Switch) findViewById(R.id.mainSwitch);
		if (NervousnetVMServiceHandler.getInstance().isNervousNetVMServiceRunning(BaseActivity.this,
				NervousnetVMService.class))
			mainSwitch.setChecked(true);

		mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				NervousnetVMServiceHandler.getInstance().startStopSensorService(isChecked, BaseActivity.this);
			}
		});

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
