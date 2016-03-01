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
            case 6:
            	fragment = new LocationFragment(6);
            	break; 	
            case 7:
            	fragment = new LightFragment(7);
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
		 if(reading != null)
			 fragment.updateReadings(reading);
		 else
			 fragment.handleError("Reading is null");
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		doBindService();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		doUnbindService();
	}
	
	@Override
	public void onBackPressed() {
		doUnbindService();
		finish();
		System.exit(0);
	}

}
