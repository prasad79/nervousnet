package ch.ethz.coss.nervousnet.sample;

import android.app.Activity;
import android.content.pm.PackageManager;

public class SampleUtils {

	  public static boolean checkForNervousnetHubApp(String uri, Activity context) {
	        PackageManager pm = context.getPackageManager();
	        boolean app_installed;
	        try {
	            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
	            app_installed = true;
	        }
	        catch (PackageManager.NameNotFoundException e) {
	            app_installed = false;
	        }
	        return app_installed;
	    }
}
