/**
 * 
 */
package ch.ethz.coss.nervousnet;

/**
 * @author prasad
 *
 */
public final class Constants {

	public final static String LOG_TAG = "NervousNetHUB";

	public static boolean DEBUG = true;

	public final static int VIBRATION_DURATION = 50;

	public static Integer[] dummy_icon_array_home = { R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing };

	public static Integer[] dummy_icon_array_sensors = { R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing };

	/****************** Preferences ****************/
	public final static String SENSOR_PREFS = "SensorPreferences";
	public final static String SENSOR_FREQ = "SensorFrequencies";
	public final static String SERVICE_PREFS = "ServicePreferences";
	public final static String UPLOAD_PREFS = "UploadPreferences";

	public final static int REQUEST_ENABLE_BT = 0;

	/****************** Sensors **********************/

	public final static int SENSOR_ACCELEROMETER = 0;
	public final static int SENSOR_LIGHT = 4;
	public final static int SENSOR_GYRO = 5;
	public final static int SENSOR_LOCATION = 99;

}
