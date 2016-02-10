package ch.ethz.coss.nervousnet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.utils.NervousStatics;
import ch.ethz.coss.nervousnet.*;

public class ServerDetailsActivity extends Activity {

	private SharedPreferences uploadPreferences;
	EditText edt_ServerIP, edt_ServerPort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_details);

		uploadPreferences = getSharedPreferences(NervousStatics.UPLOAD_PREFS, 0);
		String ip = uploadPreferences.getString("serverIP", null);
		int port = uploadPreferences.getInt("serverPort", -1);

		edt_ServerIP = (EditText) findViewById(R.id.edt_ServerIP);
		edt_ServerPort = (EditText) findViewById(R.id.edt_ServerPortNo);

		if (ip != null && port != -1) {
			edt_ServerIP.setText(ip);
			edt_ServerPort.setText(port + "");
		}
	}

	@Override
	protected void onPause() {
		toastToScreen("IP: " + edt_ServerIP.getText() + "\nPort: " + edt_ServerPort.getText(), true);

		Editor editor = uploadPreferences.edit();
		editor.putString("serverIP", edt_ServerIP.getText().toString());

		try {
			int port = Integer.parseInt(edt_ServerPort.getText().toString());
			editor.putInt("serverPort", port);
		} catch (Exception e) {
			e.printStackTrace();
			super.onPause();
			return;
		}

		if (editor.commit()) {
			if (SensorService.isServiceRunning(this)) {
				SensorService.stopService(this);
				SensorService.startService(this);
			}
			if (UploadService.isServiceRunning(this)) {
				UploadService.stopService(this);
				UploadService.startService(this);
			}
		}

		super.onPause();
	}

	public void toastToScreen(String msg, boolean lengthLong) {

		int toastLength = lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
		Toast.makeText(getApplicationContext(), msg, toastLength).show();
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.server_details, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
	 * R.id.action_settings) { return true; } return
	 * super.onOptionsItemSelected(item); }
	 */
}