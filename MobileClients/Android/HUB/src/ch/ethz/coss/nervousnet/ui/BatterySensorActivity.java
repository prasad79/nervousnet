package ch.ethz.coss.nervousnet.ui;

import android.os.Bundle;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.sensors.BatterySensor;
import ch.ethz.coss.nervousnet.sensors.BatterySensor.BatteryListener;
import ch.ethz.coss.nervousnet.vm.BatteryReading;
import ch.ethz.coss.nervousnet.vm.NervousnetVMService;

public class BatterySensorActivity extends BaseSensorActivity implements BatteryListener{

	TextView battery_percent, battery_isCharging;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_batt_sensor);

		battery_percent = (TextView) findViewById(R.id.battValueTF);
		battery_isCharging = (TextView) findViewById(R.id.statusTF);

		
		BatterySensor.getInstance(BatterySensorActivity.this).addListener(this);
	}

	/* (non-Javadoc)
	 * @see ch.ethz.coss.nervousnet.sensors.BatterySensor.BatteryListener#batterySensorDataReady(ch.ethz.coss.nervousnet.vm.BatteryReading)
	 */
	@Override
	public void batterySensorDataReady(BatteryReading reading) {
		System.out.println("batterySensorDataReady called");
		// TODO Auto-generated method stub
		battery_percent.setText("Battery level = "+reading.getPercent() +"%");
		battery_isCharging.setText("Charging status = "+reading.isCharging());
	}

	
	
}
