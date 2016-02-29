/*******************************************************************************
 *
 *  *     Nervousnet - a distributed middleware software for social sensing. 
 *  *      It is responsible for collecting and managing data in a fully de-centralised fashion
 *  *
 *  *     Copyright (C) 2016 ETH Zürich, COSS
 *  *
 *  *     This file is part of Nervousnet Framework
 *  *
 *  *     Nervousnet is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     Nervousnet is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with SwarmPulse. If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 *  * 	Contributors:
 *  * 	Prasad Pulikal - prasad.pulikal@gess.ethz.ch  -  Initial API and implementation
 *******************************************************************************/
package ch.ethz.coss.nervousnet;

/**
 * @author prasad
 *
 */
public final class Constants {

	public final static String APPLICATION_LOG_TAG = "NervousNetHUB";

	public static boolean DEBUG = true;

	public final static int VIBRATION_DURATION = 50;

	public static Integer[] dummy_icon_array_home = { R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing };

	public static Integer[] dummy_icon_array_sensors = { R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing,
			R.drawable.ic_missing, R.drawable.ic_missing, R.drawable.ic_missing };

	public static String[] sensor_labels = { "Accelerometer", "Battery", "Beacons", "Connectivity", "Gyroscope",
			"Humidity", "Location", "Light", "Magnetic", "Noise", "Pressure", "Proximity", "Temperature"

	};

	/****************** Preferences ****************/
	public final static String SENSOR_PREFS = "SensorPreferences";
	public final static String SENSOR_FREQ = "SensorFrequencies";
	public final static String SERVICE_PREFS = "ServicePreferences";
	public final static String UPLOAD_PREFS = "UploadPreferences";

	public final static int REQUEST_ENABLE_BT = 0;

	/****************** Sensors **********************/

	public final static int SENSOR_ACCELEROMETER = 0;
	public final static int SENSOR_LIGHT = 4;
	public final static int SENSOR_GYRO = 7;
	public final static int SENSOR_CONNECTIVITY = 8;
	public final static int SENSOR_LOCATION = 99;

}
