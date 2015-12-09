package ch.ethz.coss.nervousnet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import ch.ethz.coss.nervousnet.BaseActivity;
import ch.ethz.coss.nervousnet.Constants;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.ui.adapters.ImageAdapter;

public class SensorsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensors);
		parentView = findViewById(R.id.list_sensors);
		GridView gridview = (GridView) parentView.findViewById(R.id.list_sensors);
		gridview.setAdapter(new ImageAdapter(SensorsActivity.this, getResources().getStringArray(R.array.sensors_list),
				Constants.dummy_icon_array_sensors));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				showNextSensorActivity(position);
			}
		});

	}

	protected void showNextSensorActivity(int position) {
		switch (position) {
		case 0:
			startNextActivity(new Intent(SensorsActivity.this, AccSensorActivity.class));
			break;
		case 1:
			startNextActivity(new Intent(SensorsActivity.this, BatterySensorActivity.class));
			break;
		case 2:
			startNextActivity(new Intent(SensorsActivity.this, BLEBeaconActivity.class));
			break;
		case 3:
			startNextActivity(new Intent(SensorsActivity.this, ConnectivityActivity.class));
			break;
		case 4:
			startNextActivity(new Intent(SensorsActivity.this, GyroscopeSensorActivity.class));
			break;
		case 5:
			startNextActivity(new Intent(SensorsActivity.this, HumiditySensorActivity.class));
			break;
		case 6:
			startNextActivity(new Intent(SensorsActivity.this, LightSensorActivity.class));
			break;
		case 7:
			startNextActivity(new Intent(SensorsActivity.this, MagneticActivity.class));
			break;
		case 8:
			startNextActivity(new Intent(SensorsActivity.this, NoiseActivity.class));
			break;
		case 9:
			startNextActivity(new Intent(SensorsActivity.this, PressureActivity.class));
			break;
		case 10:
			startNextActivity(new Intent(SensorsActivity.this, ProximityActivity.class));
			break;
		case 11:
			startNextActivity(new Intent(SensorsActivity.this, TemperatureActivity.class));
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
