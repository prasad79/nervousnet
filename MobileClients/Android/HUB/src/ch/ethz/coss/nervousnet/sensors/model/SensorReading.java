/**
 * 
 */
package ch.ethz.coss.nervousnet.sensors.model;

import android.os.Parcelable;

/**
 * @author prasad
 *
 */
public abstract class SensorReading implements Parcelable {

	public int type = 0; // 0-light, 1- sound,
	public long timestamp;
	public String uuid;

	/*
	 * Volatility defines the time this specific data object will be kept alive
	 * on the Server and Database. Possible values: -1 = Permanently store in
	 * database 0 = Do not store in the database. by default all data pushed to
	 * the Server will have a value of 0. User has to explicitly change the
	 * settings to required days. 1 to (any long number) = Time the data can be
	 * kept alive in the database in Seconds.
	 */
	public long volatility = -1;

}
