package ch.ethz.coss.nervousnet;

import ch.ethz.coss.nervousnet.utils.NervousStatics;
import ch.ethz.coss.nervousnet.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SensorLoggingToggleActivity extends Activity {

	ListView listSensorLoggingToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_logging_toggle);

		// Toolbar toolbar = (Toolbar)
		// findViewById(R.id.fragment_toolbar_light);
		// this.setSupportActionBar(toolbar);

		CustomListAdapter adapter = new CustomListAdapter(SensorLoggingToggleActivity.this, UIConstants.sensorNames);
		listSensorLoggingToggle = (ListView) findViewById(R.id.list_SensorLoggingToggle);
		listSensorLoggingToggle.setAdapter(adapter);
	}

	public class CustomListAdapter extends ArrayAdapter<String> {
		private final Activity context;
		String[] sensorName;

		public CustomListAdapter(Activity context, String[] sensorName) {
			super(context, R.layout.sensor_logging_toggle_listitem, sensorName);
			this.context = context;
			this.sensorName = sensorName;

		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.sensor_logging_toggle_listitem, null);
			final TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_sensorName);

			final CheckBox checkBoxLog = (CheckBox) rowView.findViewById(R.id.checkBox_Log);
			final CheckBox checkBoxShare = (CheckBox) rowView.findViewById(R.id.checkBox_Share);

			txtTitle.setText(sensorName[position]);

			final SharedPreferences settings = context.getSharedPreferences(NervousStatics.SENSOR_PREFS, 0);
			boolean doMeasure = settings.getBoolean(Long.toHexString(UIConstants.sensorIds[position]) + "_doMeasure", true);
			boolean doShare = settings.getBoolean(Long.toHexString(UIConstants.sensorIds[position]) + "_doShare", true);

			checkBoxLog.setChecked(doMeasure);
			checkBoxShare.setChecked(doShare);

			checkBoxLog.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Editor edit = settings.edit();
					edit.putBoolean(Long.toHexString(UIConstants.sensorIds[position]) + "_doMeasure", isChecked);
					edit.commit();
				}
			});

			checkBoxShare.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Editor edit = settings.edit();
					edit.putBoolean(Long.toHexString(UIConstants.sensorIds[position]) + "_doShare", isChecked);
					edit.commit();
				}
			});

			return rowView;
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

}
