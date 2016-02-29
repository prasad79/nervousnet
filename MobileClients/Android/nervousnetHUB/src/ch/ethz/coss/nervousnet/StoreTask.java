package ch.ethz.coss.nervousnet;

import android.os.AsyncTask;
import android.content.Context;
import ch.ethz.coss.nervousnet.vm.NervousVM;
import ch.ethz.coss.nervousnet.vm.model.SensorDataImpl;


public class StoreTask extends AsyncTask<SensorDataImpl, Void, Void> {

	private Context context;

	public StoreTask(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(SensorDataImpl... params) {

		if (params != null && params.length > 0) {
			NervousVM nervousVM = NervousVM.getInstance(context);
			for (int i = 0; i < params.length; i++) {
				//TODO: PP
				nervousVM.storeSensor(params[i]);
			}
		}
		return null;
	}

}