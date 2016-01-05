package ch.ethz.coss.nervousnet.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.sensors.BatterySensor;
import ch.ethz.coss.nervousnet.sensors.AccelerometerSensor;
import ch.ethz.coss.nervousnet.sensors.AccelerometerSensor.AccelerometerSensorListener;
import ch.ethz.coss.nervousnet.vm.AccelerometerReading;

public class AccSensorActivity extends BaseSensorActivity implements AccelerometerSensorListener {

	
	TextView accel_values;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acc_sensor);
		setSensorStatus(Sensor.TYPE_ACCELEROMETER);
		accel_values = (TextView) findViewById(R.id.statusTF);
		
		
	}

	/* (non-Javadoc)
	 * @see ch.ethz.coss.nervousnet.sensors.AccelerometerSensor.AccelerometerSensorListener#accelSensorDataReady(ch.ethz.coss.nervousnet.vm.AccelerometerReading)
	 */
	@Override
	public void accelSensorDataReady(AccelerometerReading reading) {
		Log.d("AccSensorActivity", "accelSensorDataReady called");

		update(reading);
	}

	@Override
	protected void onResume() {
		super.onResume();

		addListener();
		Log.d("AccSensorActivity", "onResume() - addListener");
		// The activity has become visible (it is now "resumed").
	}

	@Override
	protected void onPause() {
		super.onPause();
		removeListener();

		Log.d("AccSensorActivity", "onPause() - removeListener");
		// Another activity is taking focus (this activity is about to be
		// "paused").
	}

	@Override
	protected void onStop() {
		super.onStop();
		removeListener();
		Log.d("AccSensorActivity", "onStop() - removeListener");
		// The activity is no longer visible (it is now "stopped")
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeListener();
		Log.d("AccSensorActivity", "onDestroy() - removeListener");

		// The activity is about to be destroyed.
	}

	/**
	* 
	*/
	private void addListener() {
		// TODO Auto-generated method stub
		Log.d("AccSensorActivity", "Inside AccSensorActivity addListener ");

		AccelerometerSensor.getInstance().addListener(AccSensorActivity.this);
	}

	/**
	 * 
	 */
	private void removeListener() {
		// TODO Auto-generated method stub
		Log.d("AccSensorActivity", "Inside AccSensorActivity removeListener ");
		AccelerometerSensor.getInstance().removeListener(AccSensorActivity.this);

	}
	// private int m_interval = 1000;
	// private Handler m_handler = new Handler();
	// Runnable updater = new Runnable() {
	// @Override
	// public void run() {
	// runOnUiThread(new Runnable() {
	// public void run() {
	// update();
	// }
	// });
	// m_handler.postDelayed(updater, m_interval);
	// }
	// };

	private void update(final AccelerometerReading reading) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				accel_values.setText("X = "+reading.getX()+" \n"+
						"Y = "+reading.getY()+" \n"+
						"Z = "+reading.getZ()+" \n");
			}
		});
	}

}
