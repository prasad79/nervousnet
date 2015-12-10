package ch.ethz.coss.nervousnet.example;


import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.example.R;
import ch.ethz.coss.nervousnet.vm.NervousnetRemote;

public class MainActivity extends Activity {
	protected NervousnetRemote mService;
	ServiceConnection mServiceConnection;
	EditText counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 counter = (EditText) findViewById(R.id.Counter);
		
		
//		Intent i = new Intent();
//		i.setClassName("ch.ethz.coss.nervousnet.service.core", "ch.ethz.coss.nervousnet.service.NervousnetVMService");
//		startService(i);
		
		
		initConnection();
	}
	
	
	void initConnection(){
	    mServiceConnection = new ServiceConnection() {

				@Override
				public void onServiceDisconnected(ComponentName name) {
					// TODO Auto-generated method stub
					mService = null;
					Toast.makeText(getApplicationContext(), "NervousnetRemote Service not connected", Toast.LENGTH_SHORT).show();
					Log.d("NervousnetRemote", "Binding - Service disconnected");
				}

				@Override
				public void onServiceConnected(ComponentName name, IBinder service)
				{
					// TODO Auto-generated method stub
					mService = NervousnetRemote.Stub.asInterface((IBinder) service);
					try {
						counter.setText(mService.getCounter()+"");
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(getApplicationContext(), "Nervousnet Remote Service Connected", Toast.LENGTH_SHORT).show();
					Log.d("IRemote", "Binding is done - Service connected");
				}
			};
			if(mService == null)
			{
				Intent it = new Intent();
//				it.setPackage("ch.ethz.coss.nervousnet.service");
				it.setClassName("ch.ethz.coss.nervousnet", "ch.ethz.coss.nervousnet.vm.NervousnetVMService");
//				it.setAction("ch.ethz.nervousnet.VM");
				
				  try {

					    Boolean flag = bindService(it, mServiceConnection , 0 );
					    Log.d("DEBUG", flag.toString());  // will return "true"
						if(!flag)
							Toast.makeText(MainActivity.this, "Please check if the Nervousnet Remote Service is installed and running.", Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(MainActivity.this, "Nervousnet Remote is running fine", Toast.LENGTH_SHORT).show();
					
					    } catch (Exception e) {
					    	e.printStackTrace();
					    Log.e("DEBUG", "not able to bind ! ");
					    }

				
//				//binding to remote service
//				boolean flag = bindService(it, mServiceConnection, Service.BIND_AUTO_CREATE);
//				
//				
			}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}


	public void toastToScreen(String msg, boolean lengthLong) {
		int toastLength = lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
		Toast.makeText(getApplicationContext(), msg, toastLength).show();
	}
	
	public void onBackPressed() {
		finish();	
	}
}