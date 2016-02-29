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
package ch.ethz.coss.nervousnet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
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
			startNextActivity(new Intent(SensorsActivity.this, LocSensorActivity.class));
			break;
		case 7:
			startNextActivity(new Intent(SensorsActivity.this, LightSensorActivity.class));
			break;
		case 8:
			startNextActivity(new Intent(SensorsActivity.this, MagneticActivity.class));
			break;
		case 9:
			startNextActivity(new Intent(SensorsActivity.this, NoiseActivity.class));
			break;
		case 10:
			startNextActivity(new Intent(SensorsActivity.this, PressureActivity.class));
			break;
		case 11:
			startNextActivity(new Intent(SensorsActivity.this, ProximityActivity.class));
			break;
		case 12:
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
