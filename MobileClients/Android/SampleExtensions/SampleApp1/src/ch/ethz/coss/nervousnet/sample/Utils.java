package ch.ethz.coss.nervousnet.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class Utils {

	public static void displayAlert(Context context, String title, String message, String posButtonTitle,
			OnClickListener posOnClickListener, String negButtonTitle, OnClickListener negOnClickListener) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton(posButtonTitle,
				posOnClickListener);
		if (negButtonTitle != null) {
			alertDialogBuilder.setNegativeButton(negButtonTitle, negOnClickListener);
		}

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
}
