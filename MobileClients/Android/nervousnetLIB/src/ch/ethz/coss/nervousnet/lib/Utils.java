package ch.ethz.coss.nervousnet.lib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;

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
	
	
	public static String getConnectivityTypeString(int networkType) {
		
		switch(networkType) {
		case ConnectivityManager.TYPE_MOBILE :
			return "Mobile";
		case ConnectivityManager.TYPE_WIFI :
			return "WiFi";
		case ConnectivityManager.TYPE_BLUETOOTH :
			return "Bluetooth";
		 default :
			return "Other";
	}
		
	}
}
