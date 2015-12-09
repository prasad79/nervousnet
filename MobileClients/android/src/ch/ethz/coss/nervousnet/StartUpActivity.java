package ch.ethz.coss.nervousnet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import ch.ethz.coss.nervousnet.ui.MainActivity;

/**
 * StartUpActivity is the main activity within the Nervousnet app. This activity
 * starts when the app is launched. If the TERMS_ENABLED flag is true it shows
 * the "Terms Of Use" Dialog and if this flag is not enabled it launches the
 * MainActivity
 */
public class StartUpActivity extends Activity {

	public static boolean TERMS_ENABLED = true;

	public StartUpActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (TERMS_ENABLED)
			new TermsOfUse(StartUpActivity.this).showTerms();
		else
			skipTermsScreen();

	}

	public void skipTermsScreen() {
		Intent intent = new Intent(StartUpActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		finish();

	}

}