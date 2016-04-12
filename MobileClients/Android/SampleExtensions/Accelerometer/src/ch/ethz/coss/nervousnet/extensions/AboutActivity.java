package ch.ethz.coss.nervousnet.extensions;

import ch.ethz.coss.nervousnet.extensions.R;
import android.app.Activity;
import android.os.Bundle;
import ch.ethz.coss.nervousnet.extensions.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class AboutActivity extends Activity {


	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);

		
	}

}
