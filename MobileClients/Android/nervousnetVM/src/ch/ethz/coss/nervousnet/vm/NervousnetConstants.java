package ch.ethz.coss.nervousnet.vm;

public class NervousnetConstants {
	
	public static final int STATE_PAUSED = 0;
	public static final int STATE_RUNNING = 1;

	/****************** Preferences ****************/
	public final static String SENSOR_PREFS = "SensorPreferences";
	public final static String SENSOR_FREQ = "SensorFrequencies";
	public final static String SERVICE_PREFS = "ServicePreferences";
	public final static String UPLOAD_PREFS = "UploadPreferences";

	public final static int REQUEST_ENABLE_BT = 0;

	
	public static String[] sensor_labels = { "Accelerometer", "Battery", "Beacons", "Connectivity", "Gyroscope",
			"Humidity", "Location", "Light", "Magnetic", "Noise", "Pressure", "Proximity", "Temperature"
	};
	
	public static String[] sensor_freq_labels = {"High", "Medium", "Low", "Off"};
	
	public static int[][] sensor_freq_constants = {
			{0, 10000, 30000, -1},
			{0, 300000, 1800000, -1},
			{0, 60000, 300000, -1},
			{0, 300000, 1800000, -1},
			{0, 10000, 30000, -1},
			{0, 300000, 1800000, -1},
			{0, 300000, 1800000, -1},
			{0, 300000, 1800000, -1},
			{0, 300000, 1800000, -1},
			{0, 300000, 1800000, -1},
			{0, 300000, 1800000, -1},
			{0, 300000, 1800000, -1},
			{0, 300000, 1800000, -1}
			};
	
	

}
