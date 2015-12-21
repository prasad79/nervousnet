package ch.ethz.coss.nervousnet;

import android.util.Log;
import ch.ethz.coss.nervousnet.vm.NervousnetVMServiceHandler;

public class Application extends android.app.Application {

	public Application() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				handleUncaughtException(thread, e);
			}
		});

		init();

	}

	/**
	 * 
	 */
	private void init() {
		Log.d(Constants.LOG_TAG, "Inside Application init()");
		NervousnetVMServiceHandler.getInstance().initAvailableSensors(getApplicationContext());

	}

	private void handleUncaughtException(Thread thread, Throwable e) {
		Log.e(Constants.LOG_TAG, "Inside handleUncaughtException: Exception thrown here.");

		e.printStackTrace();
		System.exit(0);
	}

}
