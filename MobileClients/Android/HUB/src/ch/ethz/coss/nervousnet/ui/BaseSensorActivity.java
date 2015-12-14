package ch.ethz.coss.nervousnet.ui;

import android.os.Bundle;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.BaseActivity;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.vm.NervousnetVMServiceHandler;

public class BaseSensorActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	protected void setSensorStatus(int type) {
		if (!NervousnetVMServiceHandler.isSensorSupported(type)) {
			TextView tv = (TextView) findViewById(R.id.statusTF);
			tv.setText("Sensor not supported by your mobile phone.");
		}
	}
}
