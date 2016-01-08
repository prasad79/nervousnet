package ch.ethz.coss.nervousnet.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.sensors.LocationSensor;
import ch.ethz.coss.nervousnet.sensors.LocationSensor.LocationSensorListener;
import ch.ethz.coss.nervousnet.vm.BatteryReading;
import ch.ethz.coss.nervousnet.vm.LocationReading;
import ch.ethz.coss.nervousnet.vm.NervousnetVMService;

public class LocSensorActivity extends BaseSensorActivity implements LocationSensorListener {

	TextView gps, alt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_sensor);
		gps = (TextView) findViewById(R.id.gps);
		alt = (TextView) findViewById(R.id.altitude);

	}

	@Override
	public void locSensorDataReady(LocationReading reading) {
		Log.d("LocSensorActivity", "Inside LocSensorActivity  Data Ready called ");
		// TODO Auto-generated method stub
		double[] coords = reading.getLatnLong();
		gps.setText("GPS = " + coords[0] + "," + coords[1]);
		alt.setText("Altitude = " + reading.getAltitude());

		updater.run();
	}

	@Override
	protected void onResume() {
		super.onResume();

		addListener();
		Log.d("LocSensorActivity", "onResume() - addListener");
		// The activity has become visible (it is now "resumed").
	}

	@Override
	protected void onPause() {
		super.onPause();
		removeListener();

		Log.d("LocSensorActivity", "onPause() - removeListener");
		// Another activity is taking focus (this activity is about to be
		// "paused").
	}

	@Override
	protected void onStop() {
		super.onStop();
		removeListener();
		Log.d("LocSensorActivity", "onStop() - removeListener");
		// The activity is no longer visible (it is now "stopped")
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeListener();
		Log.d("LocSensorActivity", "onDestroy() - removeListener");

		// The activity is about to be destroyed.
	}

	/**
	* 
	*/
	private void addListener() {
		// TODO Auto-generated method stub
		Log.d("LocSensorActivity", "Inside LocSensorActivity addListener- ");

		System.out.println("before adding listener ");

		LocationSensor.getInstance(LocSensorActivity.this).addListener(LocSensorActivity.this);
	}

	/**
	 * 
	 */
	private void removeListener() {
		// TODO Auto-generated method stub
		System.out.println("before adding listener");
		LocationSensor.getInstance(LocSensorActivity.this).removeListener(LocSensorActivity.this);

	}

	private int m_interval = 1000;
	private Handler m_handler = new Handler();
	Runnable updater = new Runnable() {
		@Override
		public void run() {
			m_handler.postDelayed(updater, m_interval);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.
	 * Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.hardware.SensorEventListener#onSensorChanged(android.hardware.
	 * SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

	}

}
