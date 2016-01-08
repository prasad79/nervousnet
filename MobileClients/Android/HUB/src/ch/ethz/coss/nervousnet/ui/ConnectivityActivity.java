package ch.ethz.coss.nervousnet.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.sensors.AccelerometerSensor;
import ch.ethz.coss.nervousnet.sensors.ConnectivitySensor;
import ch.ethz.coss.nervousnet.sensors.ConnectivitySensor.ConnectivitySensorListener;
import ch.ethz.coss.nervousnet.vm.AccelerometerReading;
import ch.ethz.coss.nervousnet.vm.ConnectivityReading;

public class ConnectivityActivity extends BaseSensorActivity implements ConnectivitySensorListener {

	TextView connectionStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conn_sensor);
		connectionStatus = (TextView) findViewById(R.id.statusTF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ethz.coss.nervousnet.sensors.ConnectivitySensor.
	 * ConnectivitySensorListener#connectivitySensorDataReady(ch.ethz.coss.
	 * nervousnet.vm.ConnectivityReading)
	 */
	@Override
	public void connectivitySensorDataReady(ConnectivityReading reading) {
		// TODO Auto-generated method stub
		update(reading);
	}

	@Override
	protected void onResume() {
		super.onResume();

		addListener();
		Log.d("ConnectivityActivity", "onResume() - addListener");
		// The activity has become visible (it is now "resumed").
	}

	@Override
	protected void onPause() {
		super.onPause();
		removeListener();

		Log.d("ConnectivityActivity", "onPause() - removeListener");
		// Another activity is taking focus (this activity is about to be
		// "paused").
	}

	@Override
	protected void onStop() {
		super.onStop();
		removeListener();
		Log.d("ConnectivityActivity", "onStop() - removeListener");
		// The activity is no longer visible (it is now "stopped")
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		removeListener();
		Log.d("ConnectivityActivity", "onDestroy() - removeListener");

		// The activity is about to be destroyed.
	}

	/**
	* 
	*/
	private void addListener() {
		// TODO Auto-generated method stub
		Log.d("ConnectivityActivity", "Inside ConnectivityActivity addListener ");

		ConnectivitySensor.getInstance(ConnectivityActivity.this).addListener(ConnectivityActivity.this);
	}

	/**
	 * 
	 */
	private void removeListener() {
		// TODO Auto-generated method stub
		Log.d("ConnectivityActivity", "Inside ConnectivityActivity removeListener ");
		ConnectivitySensor.getInstance(ConnectivityActivity.this).removeListener(ConnectivityActivity.this);

	}

	private void update(final ConnectivityReading reading) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				connectionStatus.setText("Connected = " + reading.isConnected() + "\nRoaming = " + reading.isRoaming()
						+ "\nWifi Strength = " + reading.getWifiStrength());
			}
		});
	}

}
