package ch.ethz.coss.nervousnet;

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

	}



	private void handleUncaughtException(Thread thread, Throwable e) {
		e.printStackTrace();
		System.exit(0);
	}



}
