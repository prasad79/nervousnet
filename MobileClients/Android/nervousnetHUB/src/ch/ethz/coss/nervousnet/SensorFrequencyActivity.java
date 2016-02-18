package ch.ethz.coss.nervousnet;

import ch.ethz.coss.nervousnet.utils.NervousStatics;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.vm.Constants;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_frequency);

		ArrayAdapter<String> freqUnitArrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				UIConstants.arrFrequency) {
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

		CustomListAdapter adapter = new CustomListAdapter(SensorFrequencyActivity.this, UIConstants.sensorNames,
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
			int sensFreqIndex = settings.getInt(Long.toHexString(UIConstants.sensorIds[position]) + "_freqIndex", 0);

			// Log.d("###SensFreqAct###", position + ": " + frequencyValue + "
			// ("
			// + sensFreqIndex + ")");
			spinnerSensorFreq.setSelection(sensFreqIndex);

			spinnerSensorFreq.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int freqIndex, long id) {
					Editor edit = settings.edit();
					edit.putInt(Long.toHexString(UIConstants.sensorIds[position]) + "_freqValue", UIConstants.arrSeconds[freqIndex]);
					edit.putInt(Long.toHexString(UIConstants.sensorIds[position]) + "_freqIndex", freqIndex);
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