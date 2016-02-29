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
import ch.ethz.coss.nervousnet.Constants;
import ch.ethz.coss.nervousnet.R;
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
		setContentView(R.layout.activity_sensor_display);
		updateActionBar();
		sapAdapter = new SensorDisplayPagerAdapter(getSupportFragmentManager());

	        final ActionBar actionBar = getActionBar();
	        
	        actionBar.setDisplayHomeAsUpEnabled(true);

	        viewPager = (ViewPager) findViewById(R.id.pager);
	        viewPager.setAdapter(sapAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
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

		// mainSwitch = (Switch) findViewById(R.id.mainSwitch);
		// if
		// (NervousnetVMServiceHandler.getInstance().isNervousNetVMServiceRunning(BaseActivity.this,
		// NervousnetVMService.class))
		// mainSwitch.setChecked(true);
		//
		// mainSwitch.setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// NervousnetVMServiceHandler.getInstance().startStopSensorService(isChecked,
		// BaseActivity.this);
		// }
		// });

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
            
            switch(i){
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
