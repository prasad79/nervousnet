package ch.ethz.coss.nervousnet.vm;

import java.util.Timer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.Constants;
import ch.ethz.coss.nervousnet.vm.NervousnetRemote;
import ch.ethz.coss.nervousnet.vm.NervousnetRemote.Stub;

public class NervousnetVMService extends Service {

	
	private static int SERVICE_STATE = 0;  //0 - NOT RUNNING, 1 - RUNNING
	
	
	private static int counter = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final NervousnetRemote.Stub mBinder = new NervousnetRemote.Stub() {
		@Override
		public int getCounter() {
			return counter;
		}

	};

	private static Handler handler;
	private static Runnable runnable;
	private final int runTime = 10000;

	@Override
	public void onCreate() {
		Log.d("NervousnetVMService", "oncreate - Service started");
		super.onCreate();
		SERVICE_STATE = 1;
		
		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				counter++;
				Toast.makeText(NervousnetVMService.this, "" + counter, Toast.LENGTH_LONG).show();
				if(handler != null)
					handler.postDelayed(runnable, runTime);
			
			}
		};
		handler.post(runnable);
		
		if(Constants.DEBUG)
		Toast.makeText(NervousnetVMService.this,"Service started", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onDestroy() {
		Log.d("NervousnetVMService", "onDestroy - Service destroyed");
		if (timer != null) {
			timer.cancel();
		}
		runnable = null;
		handler = null;
		
		SERVICE_STATE = 0;
		
		if(Constants.DEBUG)
		Toast.makeText(NervousnetVMService.this,"Service destroyed", Toast.LENGTH_LONG).show();
	}

	
	private Timer timer = new Timer();

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {

		System.out.println("help");
		Toast.makeText(NervousnetVMService.this, "Started!", Toast.LENGTH_LONG);
		// timer.scheduleAtFixedRate(new TimerTask() {
		//
		// @Override
		// public void run() {
		// counter++;
		// Toast.makeText(NervousnetVMService.this, ""+counter,
		// Toast.LENGTH_LONG);
		// }
		// }, 0, UPDATE_INTERVAL);
		return START_STICKY;
	}
	
	
	
	public static boolean isServiceRunning(){
	
		if(SERVICE_STATE != 0)
			return true;
		else return false;
	}
	
	
	
	public static void startService(Context context) {
		Intent sensorIntent = new Intent(context, NervousnetVMService.class);
		context.startService(sensorIntent);
	}

	public static void stopService(Context context) {
		Intent sensorIntent = new Intent(context, NervousnetVMService.class);
		context.stopService(sensorIntent);
	}
	



}