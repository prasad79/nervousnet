/**
 * 
 */
package ch.ethz.coss.nervousnet.ui.fragments;

import android.os.Bundle;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.SensorReading;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author prasad
 *
 */
public class AccelFragment extends BaseFragment{

	/**
	 * 
	 */
	public AccelFragment(int type) {
		super(type);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_accel, container, false);
		
		return rootView;
	}
	

	/* (non-Javadoc)
	 * @see ch.ethz.coss.nervousnet.sample.BaseFragment#updateReadings()
	 */
	@Override
	public void updateReadings(SensorReading reading){
		 Log.d("AccelFragment", "Inside updateReadings");
		 TextView x_value = (TextView) getActivity().findViewById(R.id.accel_x);
		 x_value.setText("" + ((AccelerometerReading)reading).getX());
	     

		 TextView y_value = (TextView) getActivity().findViewById(R.id.accel_y);
		 y_value.setText("" + ((AccelerometerReading)reading).getY());
	     

		 TextView z_value = (TextView) getActivity().findViewById(R.id.accel_z);
		 z_value.setText("" + ((AccelerometerReading)reading).getZ());
		
	}


}
