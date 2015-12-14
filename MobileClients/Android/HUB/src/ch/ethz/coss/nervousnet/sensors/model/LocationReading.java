/**
 * 
 */
package ch.ethz.coss.nervousnet.sensors.model;

import android.os.Parcel;

/**
 * @author prasad
 *
 */
public class LocationReading extends SensorReading {

	public LocationReading(double[] location) {
		latnLong = location;
	}

	/**
	 * @param in
	 */
	public LocationReading(Parcel in) {
		readFromParcel(in);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(timestamp);
		out.writeDoubleArray(latnLong);

	}

	public void readFromParcel(Parcel in) {

		timestamp = in.readLong();
		latnLong = in.createDoubleArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double[] latnLong;

}
