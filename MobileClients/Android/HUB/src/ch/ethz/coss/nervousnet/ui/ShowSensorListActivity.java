/**
 * 
 */
package ch.ethz.coss.nervousnet.ui;

import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.ethz.coss.nervousnet.BaseActivity;
import ch.ethz.coss.nervousnet.R;

/**
 * @author prasad
 *
 */
public class ShowSensorListActivity extends BaseActivity {
	SensorManager smm;
	List<Sensor> sensor;
	ListView lv;

	Hashtable<Integer, Sensor> hSensors = new Hashtable<Integer, Sensor>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_sensor_list);
		smm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		lv = (ListView) findViewById(R.id.listView1);
		sensor = smm.getSensorList(Sensor.TYPE_ALL);

		for (int i = 0; i < sensor.size(); i++) {
			hSensors.put(sensor.get(i).getType(), sensor.get(i));
		}
		lv.setAdapter(new ArrayAdapter<Sensor>(this, android.R.layout.simple_list_item_1, sensor));
	}
}
