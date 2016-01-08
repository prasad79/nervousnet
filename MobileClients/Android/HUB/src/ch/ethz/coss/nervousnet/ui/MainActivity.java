package ch.ethz.coss.nervousnet.ui;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import ch.ethz.coss.nervousnet.BaseActivity;
import ch.ethz.coss.nervousnet.Constants;
import ch.ethz.coss.nervousnet.R;
import ch.ethz.coss.nervousnet.ui.adapters.ImageAdapter;
import ch.ethz.coss.nervousnet.vm.NervousnetRemote;

public class MainActivity extends BaseActivity {
	protected NervousnetRemote mService;
	ServiceConnection mServiceConnection;
	EditText counter;

	View parentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		parentView = findViewById(R.id.main_grid);
		GridView gridview = (GridView) parentView.findViewById(R.id.main_grid);
		gridview.setAdapter(new ImageAdapter(MainActivity.this, getResources().getStringArray(R.array.main_grid),
				Constants.dummy_icon_array_home));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				showNextActivity(position);
			}
		});

	}

	protected void showNextActivity(int position) {

		switch (position) {
		case 0:
			startNextActivity(new Intent(MainActivity.this, SensorsActivity.class));
			break;
		case 1:
			startNextActivity(new Intent(MainActivity.this, AnalyticsActivity.class));
			break;
		case 2:
			startNextActivity(new Intent(MainActivity.this, ShowcaseActivity.class));
			break;
		case 3:
			startNextActivity(new Intent(MainActivity.this, SettingsActivity.class));
			break;
		case 4:
			startNextActivity(new Intent(MainActivity.this, HelpActivity.class));
			break;
		case 5:
			startNextActivity(new Intent(MainActivity.this, AboutActivity.class));
			break;
		case 6:
			startNextActivity(new Intent(MainActivity.this, ShowSensorListActivity.class));
			break;
		}
	}

	@Override
	protected void onResume() {

		Log.d("MainActivity", "onResumeCalled");

		super.onResume();
		updateActionBar();

	}

	public void toastToScreen(String msg, boolean lengthLong) {
		int toastLength = lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
		Toast.makeText(getApplicationContext(), msg, toastLength).show();
	}

	@Override
	public void onBackPressed() {
		finish();
		System.exit(0);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d("MainActivity", "onRestoreInstanceState");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("MainActivity", "onSaveInstanceState");
	}

}