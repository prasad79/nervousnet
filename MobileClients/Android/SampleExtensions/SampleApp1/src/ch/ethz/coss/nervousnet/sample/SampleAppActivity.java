package ch.ethz.coss.nervousnet.sample;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import ch.ethz.coss.nervousnet.sample.R;
import ch.ethz.coss.nervousnet.lib.SensorReading;

public class SampleAppActivity extends BaseSampleActivity {
	
	SampleAppPagerAdapter sapAdapter;
	ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample_app);
		
		sapAdapter = new SampleAppPagerAdapter(getSupportFragmentManager());

	        // Set up action bar.
	        final ActionBar actionBar = getActionBar();

	        // Specify that the Home button should show an "Up" caret, indicating that touching the
	        // button will take the user one step up in the application's hierarchy.
	        actionBar.setDisplayHomeAsUpEnabled(true);

	        // Set up the ViewPager, attaching the adapter.
	        viewPager = (ViewPager) findViewById(R.id.pager);
	        viewPager.setAdapter(sapAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sample_app, menu);
		return true;
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

	
	
    public static class SampleAppPagerAdapter extends FragmentStatePagerAdapter {

        public SampleAppPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
//            Fragment fragment = null;
            
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







	/* (non-Javadoc)
	 * @see ch.ethz.coss.nervousnet.sample.BaseSampleActivity#updateStatus()
	 */
	@Override
	protected void updateStatus(SensorReading reading) {
		 Log.d("AccelFragment", "Inside updateStatus");
		fragment.updateReadings(reading);
	}
	
	
	
	@Override
	public void onBackPressed() {
		finish();
	}

}
