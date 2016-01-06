package ch.ethz.coss.nervousnet.ui;

import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.sensors.AccelerometerSensor;
import ch.ethz.coss.nervousnet.sensors.GyroSensor;
import ch.ethz.coss.nervousnet.sensors.GyroSensor.GyroSensorListener;
import ch.ethz.coss.nervousnet.vm.AccelerometerReading;
import ch.ethz.coss.nervousnet.vm.GyroReading;

public class GyroscopeSensorActivity extends BaseSensorActivity implements GyroSensorListener{

TextView gyro_values;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gyro_sensor);
		setSensorStatus(Sensor.TYPE_GYROSCOPE);
		gyro_values = (TextView) findViewById(R.id.statusTF);
		
		
	}

	/* (non-Javadoc)
	 * @see ch.ethz.coss.nervousnet.sensors.AccelerometerSensor.AccelerometerSensorListener#accelSensorDataReady(ch.ethz.coss.nervousnet.vm.AccelerometerReading)
	 */
	@Override
	public void gyroSensorDataReady(GyroReading reading) {
		Log.d("GyroscopeSensorActivity", "gyroSensorDataReady called");

		update(reading);
	}

	@Override
	protected void onResume() {
		super.onResume();

		addListener();
		Log.d("GyroscopeSensorActivity", "onResume() - addListener");
		// The activity has become visible (it is now "resumed").
	}

	@Override
	protected void onPause() {
		super.onPause();
		removeListener();

		Log.d("GyroscopeSensorActivity", "onPause() - removeListener");
		// Another activity is taking focus (this activity is about to be
		// "paused").
	}

	@Override
	protected void onStop() {
		super.onStop();
		removeListener();
		Log.d("GyroscopeSensorActivity", "onStop() - removeListener");
		// The activity is no longer visible (it is now "stopped")
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeListener();
		Log.d("GyroscopeSensorActivity", "onDestroy() - removeListener");

		// The activity is about to be destroyed.
	}

	/**
	* 
	*/
	private void addListener() {
		// TODO Auto-generated method stub
		Log.d("GyroscopeSensorActivity", "Inside AccSensorActivity addListener ");

		GyroSensor.getInstance().addListener(GyroscopeSensorActivity.this);
	}

	/**
	 * 
	 */
	private void removeListener() {
		// TODO Auto-generated method stub
		Log.d("AccSensorActivity", "Inside AccSensorActivity removeListener ");
		GyroSensor.getInstance().removeListener(GyroscopeSensorActivity.this);

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

	private void update(final GyroReading reading) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				gyro_values.setText("X = "+reading.getGyroX()+" \n"+
						"Y = "+reading.getGyroY()+" \n"+
						"Z = "+reading.getGyroZ()+" \n");
			}
		});
	}

}
