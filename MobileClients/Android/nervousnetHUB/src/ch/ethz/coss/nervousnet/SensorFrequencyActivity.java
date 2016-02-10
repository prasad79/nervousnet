package ch.ethz.coss.nervousnet;

import ch.ethz.coss.nervousnet.sensors.SensorDescAccelerometer;
import ch.ethz.coss.nervousnet.sensors.SensorDescBLEBeacon;
import ch.ethz.coss.nervousnet.sensors.SensorDescBattery;
import ch.ethz.coss.nervousnet.sensors.SensorDescConnectivity;
import ch.ethz.coss.nervousnet.sensors.SensorDescGyroscope;
import ch.ethz.coss.nervousnet.sensors.SensorDescHumidity;
import ch.ethz.coss.nervousnet.sensors.SensorDescLight;
import ch.ethz.coss.nervousnet.sensors.SensorDescMagnetic;
import ch.ethz.coss.nervousnet.sensors.SensorDescNoise;
import ch.ethz.coss.nervousnet.sensors.SensorDescPressure;
import ch.ethz.coss.nervousnet.sensors.SensorDescProximity;
import ch.ethz.coss.nervousnet.sensors.SensorDescTemperature;
import ch.ethz.coss.nervousnet.utils.NervousStatics;
import ch.ethz.coss.nervousnet.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SensorFrequencyActivity extends Activity {

	ListView listSensorFrequency;
	String[] sensorNames = { "Accelerometer", "Battery", "BLEBeacon", "Connectivity", "Gyroscope", "Humidity", "Light",
			"Magnetic", "Noise", "Pressure", "Proximity", "Temperature" };
	String[] arrFrequency = { "1 sec", "5 sec", "10 sec", "20 sec", "30 sec", "1 min", "2 min", "3 min", "5 min", "10 min", "15 min", "20 min", "30 min",
			"45 min", "1 h", "2 h", "10 h", "12 h", "1 d", "2 d", "5 d", "1 w", "2 w", "1 m" };

	final int m = 60;
	final int h = 60 * m;
	final int d = 24 * h;
	final int w = 7 * d;
	final int mo = 4 * w;

	int[] arrSeconds = {1, 5, 10, 20, 30, m, 2 * m, 3 * m, 5 * m, 10 * m, 15 * m, 20 * m, 30 * m, 45 * m, h, 2 * h, 10 * h, 12 * h,
			d, 2 * d, 5 * d, w, 2 * w, mo };

	long[] sensorIds = { SensorDescAccelerometer.SENSOR_ID, SensorDescBattery.SENSOR_ID, SensorDescBLEBeacon.SENSOR_ID,
			SensorDescConnectivity.SENSOR_ID, SensorDescGyroscope.SENSOR_ID, SensorDescHumidity.SENSOR_ID,
			SensorDescLight.SENSOR_ID, SensorDescMagnetic.SENSOR_ID, SensorDescNoise.SENSOR_ID,
			SensorDescPressure.SENSOR_ID, SensorDescProximity.SENSOR_ID, SensorDescTemperature.SENSOR_ID };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_frequency);

		ArrayAdapter<String> freqUnitArrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				arrFrequency) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				((TextView) v).setGravity(Gravity.END);
				return v;
			}

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				View v = super.getDropDownView(position, convertView, parent);
				((TextView) v).setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
				return v;
			}
		};
		freqUnitArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		CustomListAdapter adapter = new CustomListAdapter(SensorFrequencyActivity.this, sensorNames,
				freqUnitArrAdapter);
		listSensorFrequency = (ListView) findViewById(R.id.list_SensorFrequency);
		listSensorFrequency.setAdapter(adapter);

	}

	public class CustomListAdapter extends ArrayAdapter<String> {
		private final Activity context;
		String[] sensorName;
		ArrayAdapter<String> freqUnitArrAdapter;

		public CustomListAdapter(Activity context, String[] sensorName, ArrayAdapter<String> freqUnitArrAdapter2) {
			super(context, R.layout.sensor_frequency_listitem, sensorName);
			this.context = context;
			this.sensorName = sensorName;
			this.freqUnitArrAdapter = freqUnitArrAdapter2;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.sensor_frequency_listitem, null);
			final TextView txtSensorName = (TextView) rowView.findViewById(R.id.txt_SensorFreq_SensorItem);
			txtSensorName.setText(sensorName[position]);

			final Spinner spinnerSensorFreq = (Spinner) rowView.findViewById(R.id.spinner_sensor_frequency);
			spinnerSensorFreq.setAdapter(freqUnitArrAdapter);

			final SharedPreferences settings = context.getSharedPreferences(NervousStatics.SENSOR_FREQ, 0);
			int sensFreqIndex = settings.getInt(Long.toHexString(sensorIds[position]) + "_freqIndex", 0);

			// Log.d("###SensFreqAct###", position + ": " + frequencyValue + "
			// ("
			// + sensFreqIndex + ")");
			spinnerSensorFreq.setSelection(sensFreqIndex);

			spinnerSensorFreq.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int freqIndex, long id) {
					Editor edit = settings.edit();
					edit.putInt(Long.toHexString(sensorIds[position]) + "_freqValue", arrSeconds[freqIndex]);
					edit.putInt(Long.toHexString(sensorIds[position]) + "_freqIndex", freqIndex);
					edit.commit();
					// toastToScreen("saved: " + position, false);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			return rowView;
		}

		private int freqUnitToIndex(String freqUnit) {
			// TODO
			return 0;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Changed settings must be applied
		if (SensorService.isServiceRunning(this)) {
			SensorService.stopService(this);
			SensorService.startService(this);
		}
		if (UploadService.isServiceRunning(this)) {
			UploadService.stopService(this);
			UploadService.startService(this);
		}
	}

	public void toastToScreen(String msg, boolean lengthLong) {

		int toastLength = lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
		Toast.makeText(getApplicationContext(), msg, toastLength).show();
	}
}