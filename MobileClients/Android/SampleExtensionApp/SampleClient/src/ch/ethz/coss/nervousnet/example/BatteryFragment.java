/**
 * 
 */
package ch.ethz.coss.nervousnet.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.vm.AccelerometerReading;
import ch.ethz.coss.nervousnet.vm.BatteryReading;
import ch.ethz.coss.nervousnet.vm.SensorReading;
/**
 * @author prasad
 *
 */
public class BatteryFragment extends BaseFragment {

	/**
	 * 
	 */
	public BatteryFragment(int type) {
		super(type);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_battery, container, false);
		
		return rootView;
	}

	/* (non-Javadoc)
	 * @see ch.ethz.coss.nervousnet.example.BaseFragment#updateReadings(ch.ethz.coss.nervousnet.vm.SensorReading)
	 */
	@Override
	public void updateReadings(SensorReading reading) {
		 TextView percent = (TextView) getActivity().findViewById(R.id.battery_percent);
		 percent.setText("" + ((BatteryReading)reading).getPercent() * 100 +" %");
	     

		 TextView isCharging = (TextView) getActivity().findViewById(R.id.battery_isCharging);
		 isCharging.setText("" + ((BatteryReading)reading).isCharging());
	     

		 TextView USB_Charging = (TextView) getActivity().findViewById(R.id.battery_isUSB);
		 USB_Charging.setText(((BatteryReading)reading).getCharging_type() == 1 ? "YES" : "NO");
		
		 TextView AC_charging = (TextView) getActivity().findViewById(R.id.battery_isAC);
		 AC_charging.setText(((BatteryReading)reading).getCharging_type() == 0 ? "YES" : "NO");
		
	}

}
