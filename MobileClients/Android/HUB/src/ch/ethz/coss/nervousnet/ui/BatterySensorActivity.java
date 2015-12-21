package ch.ethz.coss.nervousnet.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;

public class BatterySensorActivity extends BaseSensorActivity {
	
	
	TextView battery_percent, battery_isCharging; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_batt_sensor);
		
		
		battery_percent = (TextView) findViewById(R.id.battValueTF);
		battery_isCharging = (TextView) findViewById(R.id.statusTF);
		

	}

}
