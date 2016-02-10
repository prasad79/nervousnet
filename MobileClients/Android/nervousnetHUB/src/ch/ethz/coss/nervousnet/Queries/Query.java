package ch.ethz.coss.nervousnet.Queries;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import ch.ethz.coss.nervousnet.MainActivity;
import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.SensorDesc;
import ch.ethz.coss.nervousnet.vm.NervousVM;

// abstracts all queries
// textual and numerical
// basic time stamp range
// and sensor ID

public abstract class Query<G extends SensorDesc> {
	
	
	public static List<SensorData> list;

		public abstract long getSensorId();

		public Query(long timestamp_from, long timestamp_to, File file) {
			NervousVM nervousVm = NervousVM.getInstance(file);
			list = nervousVm.retrieve(getSensorId(), timestamp_from, timestamp_to);
			if (containsReadings()) {
				Log.d(MainActivity.LOG_TAG, "Retrieved List Size: " + list.size());
			}
		}

		public boolean containsReadings() {
			if (list == null || list.isEmpty()) {
				return false;
			} else {
				return true;
			}
		}
		
		/**
		 * @return
		 * Returns the number of elements 
		 */
		public int getCount(){
			return list.size();
		}
		
		public abstract ArrayList<G> getSensorDescriptorList();
	}



