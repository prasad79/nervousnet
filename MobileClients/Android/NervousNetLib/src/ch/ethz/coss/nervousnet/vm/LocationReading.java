
package ch.ethz.coss.nervousnet.vm;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author prasad
 */
public class LocationReading extends SensorReading {

	private static String LOG_TAG = "LocationReading";

	public LocationReading(long timestamp, double [] latnLong) {
		Log.d(LOG_TAG, "Inside LocationReading constructor 1");
		this.timestamp = timestamp;
		this.latnLong = latnLong;
	}
	
	public LocationReading(long timestamp, double latitude, double longitude) {
		Log.d(LOG_TAG, "Inside LocationReading constructor 1");
		this.timestamp = timestamp;
		this.latnLong = new double[] {latitude, longitude};
	}

	/**
	 * @param in
	 */
	public LocationReading(Parcel in) {
		Log.d(LOG_TAG, "Inside LocationReading constructor 2");
		readFromParcel(in);
	}

	/**
	 * @return the latnLong
	 */
	public double[] getLatnLong() {
		return latnLong;
	}

	/**
	 * @param latnLong the latnLong to set
	 */
	public void setLatnLong(double[] latnLong) {
		this.latnLong = latnLong;
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

	public static final Parcelable.Creator<LocationReading> CREATOR = new Parcelable.Creator<LocationReading>() {
		@Override
		public LocationReading createFromParcel(Parcel in) {
			return new LocationReading(in);
		}

		@Override
		public LocationReading[] newArray(int size) {
			return new LocationReading[size];
		}
	};

	public String toString() {
		return new String("LocationReading - Latitude = " + latnLong[0]+ ", Longitude = "+latnLong[1]);
	}

	private double[] latnLong;
}
