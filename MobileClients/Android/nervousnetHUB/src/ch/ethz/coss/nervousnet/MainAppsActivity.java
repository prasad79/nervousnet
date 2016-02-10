package ch.ethz.coss.nervousnet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import ch.ethz.coss.nervousnet.R;

public class MainAppsActivity extends Activity {

	ListView listMainApps;
	private static final int vibDuration = 50;

	class MainApp {
		String name, description, playStoreLink;
		int imageResource;

		public MainApp(String sName, String sDescription, String sAppStoreLink, int appimage) {
			name = sName;
			description = sDescription;
			playStoreLink = sAppStoreLink;
			imageResource = appimage;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_apps);

		final MainApp[] arrMainApp = fetchMainApps();

		CustomListAdapter adapter = new CustomListAdapter(MainAppsActivity.this, arrMainApp);
		listMainApps = (ListView) findViewById(R.id.list_MainApps);
		listMainApps.setAdapter(adapter);

		final Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		listMainApps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try {
					vibrator.vibrate(vibDuration);
					// startActivity(new Intent(Intent.ACTION_VIEW,
					// Uri.parse("market://details?id=" + appPackageName)));
					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(arrMainApp[position].playStoreLink));
					startActivity(browserIntent);
				} catch (android.content.ActivityNotFoundException anfe) {
					System.out.println("Tried to open " + arrMainApp[position].playStoreLink);
				}

			}
		});
	}

	private MainApp[] fetchMainApps() {
		ArrayList<MainApp> arrListMA = new ArrayList<MainAppsActivity.MainApp>();

		// TODO: Sid get list from server
		arrListMA.add(new MainApp("nervousnetedu", "Collects data",
				"https://play.google.com/store/apps/details?id=ch.ethz.soms.nervousedu.android&hl=en",
				R.drawable.appimages_nervousnetedu));
		arrListMA.add(new MainApp("nervousnet for 31c3", "Collects anonymous data at the 31c3 Conference",
				"https://play.google.com/store/apps/details?id=ch.ethz.soms.nervous.android&hl=en",
				R.drawable.appimages_nervousnet31c3));

		return arrListMA.toArray(new MainApp[arrListMA.size()]);
	}

	public class CustomListAdapter extends ArrayAdapter<MainApp> {
		private final Activity context;
		MainApp[] arrMainApp;

		public CustomListAdapter(Activity context, MainApp[] arrMainApp) {
			super(context, R.layout.mainapp_listitem, arrMainApp);
			this.context = context;
			this.arrMainApp = arrMainApp;

		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.mainapp_listitem, null);
			final TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_MainAppName);
			final TextView txtDesc = (TextView) rowView.findViewById(R.id.txt_MainAppDesc);
			final ImageView imgPic = (ImageView) rowView.findViewById(R.id.img_mainAppImage);

			txtTitle.setText(arrMainApp[position].name);
			txtDesc.setText(arrMainApp[position].description);
			imgPic.setImageResource(arrMainApp[position].imageResource);
			return rowView;
		}
	}

}
