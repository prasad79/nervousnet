package ch.ethz.coss.nervousnet.ui;

import android.hardware.Sensor;
import android.os.Bundle;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.vm.NervousnetVMServiceHandler;

public class AccSensorActivity extends BaseSensorActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acc_sensor);
		
		
		setSensorStatus(Sensor.TYPE_ACCELEROMETER);
		
	}

}
