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
package ch.ethz.coss.nervousnet.sample;


import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.sample.R;
import ch.ethz.coss.nervousnet.lib.NervousnetRemote;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import ch.ethz.coss.nervousnet.lib.Utils;

public class SampleAppActivity extends FragmentActivity {
	 	
	SampleAppPagerAdapter sapAdapter;
	ViewPager vPager;
	private static BaseFragment fragment;
	
	 protected NervousnetRemote mService;
	 private ServiceConnection mServiceConnection;
	 private Boolean bindFlag;

	 int m_interval = 100; // 1 seconds by default, can be changed later
	 Handler m_handler = new Handler();
	 Runnable m_statusChecker = new Runnable() {
		@Override
		public void run() {

		    Log.d("SampleAppActivity", "before updating");
		    if(mService != null)
		    	update(); // this function can change value of m_interval.
		    else 
		    	 Log.d("SampleAppActivity", "mService is null");

			m_handler.postDelayed(m_statusChecker, m_interval);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample_app);
		
		
	        // Set up action bar.
	        final ActionBar actionBar = getActionBar();

	        // Specify that the Home button should show an "Up" caret, indicating that touching the
	        // button will take the user one step up in the application's hierarchy.
	        actionBar.setDisplayHomeAsUpEnabled(true);

	        sapAdapter = new SampleAppPagerAdapter(getSupportFragmentManager());

	       
	        // Set up the ViewPager, attaching the adapter.
	        vPager = (ViewPager) findViewById(R.id.pager);
	        vPager.setAdapter(sapAdapter);
	        
	        if(mServiceConnection == null){
				initConnection();		
			}
							
			if(mService == null) {
				if (mService == null) {
					try {

						doBindService();
						Log.d("SampleAppActivity", bindFlag.toString()); // will return "true"
						if (!bindFlag){

							Utils.displayAlert(SampleAppActivity.this, "Alert",
									"Nervousnet HUB application is required to be running to use this app. Please download it from the App Store.",
									"Download Now", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											startActivity(new Intent(Intent.ACTION_VIEW,
													Uri.parse("https://play.google.com/store/apps/details?id=ch.ethz.soms.nervousnet")));
										}
									}, "Exit", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											System.exit(0);
										}
									});
							Toast.makeText(SampleAppActivity.this,
									"Please check if the Nervousnet Remote Service is installed and running.",
									Toast.LENGTH_SHORT).show();
						} else{
							startRepeatingTask();
							Toast.makeText(SampleAppActivity.this, "Nervousnet Remote is running fine and startRepeatingTask() called", Toast.LENGTH_SHORT).show();

						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.e("SensorDisplayActivity", "not able to bind ! ");
					}

					// //binding to remote service
					// boolean flag = bindService(it, mServiceConnection,
					// Service.BIND_AUTO_CREATE);
					//
					//
				}
			}
	}
	
	void initConnection() {

		Log.d("SensorDisplayActivity", "Inside initConnection");
		mServiceConnection = new ServiceConnection() {

			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				 Log.d("SensorDisplayActivity", "Inside onServiceDisconnected 2");
				System.out.println("onServiceDisconnected");
				// TODO Auto-generated method stub
				mService = null;
				mServiceConnection = null;
				Toast.makeText(getApplicationContext(), "NervousnetRemote Service not connected", Toast.LENGTH_SHORT)
						.show();
				Log.d("SensorDisplayActivity", "Binding - Service disconnected");
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				Log.d("SensorDisplayActivity", "onServiceConnected");
				Log.d("SensorDisplayActivity", "Inside onServiceConnected 2");
				 
				mService = NervousnetRemote.Stub.asInterface(service);

//				try {
//					count.setText(mService.getCounter() + "");
//				} catch (RemoteException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				// try {
				// BatteryReading reading = mService.getBatteryReading();
				// System.out.println("onServiceConnected 2");
				// if(reading != null)
				// counter.setText(reading.getBatteryPercent()+"");
				// else
				// counter.setText("Null object returned");
				// } catch (RemoteException e) {
				// // TODO Auto-generated catch block
				// System.out.println("Exception thrown here");
				// e.printStackTrace();
				// }
				// m_handler.post(m_statusChecker);

				startRepeatingTask();
				Toast.makeText(getApplicationContext(), "Nervousnet Remote Service Connected", Toast.LENGTH_SHORT)
						.show();
				Log.d("SensorDisplayActivity", "Binding is done - Service connected");
			}
		};
		

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
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
        
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        
        
        @SuppressWarnings("unchecked")
	    public Fragment getFragment(int position) {
	        try {
	            Field f = FragmentStatePagerAdapter.class.getDeclaredField("mFragments");
	            f.setAccessible(true);
	            ArrayList<Fragment> fragments = (ArrayList<Fragment>) f.get(this);
	            if (fragments.size() > position) {
	                return fragments.get(position);
	            }
	            return null;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
        
    }





	protected void updateStatus(SensorReading reading, int index) {

		 BaseFragment fragment =  (BaseFragment) sapAdapter.getFragment(index);
		
		 Log.d("SampleAppActivity", "Inside updateStatus "+fragment.type);
		 
		 if(reading != null) {
			 
			 fragment.updateReadings(reading);
		 }
		 else
			 fragment.handleError("Reading is null");
	}
	
	

	
	@Override
	public void onBackPressed() {
		stopRepeatingTask();
		finish();
	}


	void startRepeatingTask() {
		m_statusChecker.run();
	}

	void stopRepeatingTask() {
		m_handler.removeCallbacks(m_statusChecker);
	}
	
	protected void update() {
		try {
			int index = vPager.getCurrentItem();
			Log.d("BaseSampleActivity", "Inside update : index  = "+index);
		
			 switch(index){
			 case 0:
				 updateStatus((SensorReading)mService.getAccelerometerReading(), index);
				 break;
			 case 1:
				 updateStatus((SensorReading)mService.getBatteryReading(), index);
				 break;
			 case 2:
				//beacons
				 break;
			 case 3:
				 updateStatus((SensorReading)mService.getConnectivityReading(), index);
				 break;
			 case 4:
				 updateStatus((SensorReading)mService.getGyroReading(), index);
				 break;
			 case 5:
				 //HUmidity
				 break;
			 case 6:
				 updateStatus((SensorReading)mService.getLocationReading(), index);
				 break;
			 case 7:
				 updateStatus((SensorReading)mService.getLightReading(), index);
				 break;
			 case 8:
				//Magnetic
				 break;		 
			 case 9:
				 updateStatus((SensorReading)mService.getNoiseReading(), index);
				 break;
					 
					 
			 }

			 vPager.getAdapter().notifyDataSetChanged();

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void doBindService() {
		Log.d("SensorDisplayActivity", "doBindService successfull");
		Intent it = new Intent();
		it.setClassName("ch.ethz.coss.nervousnet", "ch.ethz.coss.nervousnet.SensorService");
		bindFlag = getApplicationContext().bindService(it, mServiceConnection, 0);
		 
	}
	
	protected void doUnbindService() {
		getApplicationContext().unbindService(mServiceConnection);
		bindFlag = false;
		 Log.d("SensorDisplayActivity ", "doUnbindService successfull");
	}
}
