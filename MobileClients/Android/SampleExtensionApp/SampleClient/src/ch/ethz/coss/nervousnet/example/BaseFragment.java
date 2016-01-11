/**
 * 
 */
package ch.ethz.coss.nervousnet.example;

import android.support.v4.app.Fragment;
import ch.ethz.coss.nervousnet.vm.SensorReading;

/**
 * @author prasad
 *
 */
public abstract class BaseFragment extends Fragment {
	
	int type = 0;

	/**
	 * 
	 */
	public BaseFragment(int type) {
		// TODO Auto-generated constructor stub
		
		this.type = type;
	}
	
	
	public abstract void updateReadings(SensorReading reading);

}
