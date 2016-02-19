package ch.ethz.coss.nervousnet;

import ch.ethz.coss.nervousnet.vm.Constants;

public class UIConstants {

	final static int m = 60;
	final static int h = 60 * m;
	final static int d = 24 * h;
	final static int w = 7 * d;
	final static int mo = 4 * w;
	
	static String[] sensorNames = { "Location", "Accelerometer", "Battery", "BLEBeacon", "Connectivity", "Gyroscope", "Humidity", "Light",
			"Magnetic", "Noise", "Pressure", "Proximity", "Temperature" };
	static String[] arrFrequency = { "1 sec", "5 sec", "10 sec", "20 sec", "30 sec", "1 min", "2 min", "3 min", "5 min", "10 min", "15 min", "20 min", "30 min",
			"45 min", "1 h", "2 h", "10 h", "12 h", "1 d", "2 d", "5 d", "1 w", "2 w", "1 m" };
	
	static int[] arrSeconds = {1, 5, 10, 20, 30, m, 2 * m, 3 * m, 5 * m, 10 * m, 15 * m, 20 * m, 30 * m, 45 * m, h, 2 * h, 10 * h, 12 * h,
			d, 2 * d, 5 * d, w, 2 * w, mo };

	static long[] sensorIds = {Constants.SENSOR_LOCATION, Constants.SENSOR_ACCELEROMETER, Constants.SENSOR_BATTERY, Constants.SENSOR_BLEBEACON,
			Constants.SENSOR_CONNECTIVITY, Constants.SENSOR_GYROSCOPE, Constants.SENSOR_HUMIDITY,
			Constants.SENSOR_LIGHT, Constants.SENSOR_MAGNETIC, Constants.SENSOR_NOISE,
			Constants.SENSOR_PRESSURE,Constants.SENSOR_PROXIMITY, Constants.SENSOR_TEMPERATURE};
}
