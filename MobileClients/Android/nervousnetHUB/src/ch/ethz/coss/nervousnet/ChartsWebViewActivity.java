package ch.ethz.coss.nervousnet;

import java.util.ArrayList;
import java.util.Calendar;
import ch.ethz.coss.nervousnet.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class ChartsWebViewActivity extends Activity {

	private WebView webView;
	private String selected_sensor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Load the webview that shows the plot from the corresponding html file
		 * in assets
		 */

		setContentView(R.layout.charts_webview);

		// To debug webview
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// WebView.setWebContentsDebuggingEnabled(true);
		// }

		// Get javascript variable from intent and set it into the webview
		String javascript_global_variables = getIntent().getStringExtra("javascript_global_variables");
		Log.i("javascript var: ", javascript_global_variables);
		String type_of_plot = getIntent().getStringExtra("type_of_plot");

		// Get selected sensor from sensors statistics activity
		selected_sensor = getIntent().getStringExtra("selected_sensor");

		webView = (WebView) findViewById(R.id.webView_charts);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("javascript:" + javascript_global_variables);
		webView.loadUrl("file:///android_asset/webview_charts_" + type_of_plot + ".html");

		if (type_of_plot.equals("1_line_live_data_over_time") || type_of_plot.equals("3_lines_live_data_over_time"))
			updateData();
		else
			findViewById(R.id.waiting_for_sensor_data_textView).setVisibility(View.GONE);
	}


	private void updateData() {
		new CountDownTimer(1000, 1000) {
			public void onTick(long millisUntilFinished) {
				
			}

			public void onFinish() {
				updateData();
			}

		}.start();
	}
}