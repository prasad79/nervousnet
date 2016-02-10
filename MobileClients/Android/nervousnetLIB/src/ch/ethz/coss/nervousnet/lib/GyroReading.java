
package ch.ethz.coss.nervousnet.lib;

import android.os.BatteryManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author prasad
 */
public class GyroReading extends SensorReading {

	private float[] values = new float[3];

	public GyroReading(int timestamp, float[] values) {
		this.timestamp = timestamp;
		this.values = values;
	}

	/**
	 * @param in
	 */
	public GyroReading(Parcel in) {
		readFromParcel(in);
	}

	public void readFromParcel(Parcel in) {

		timestamp = in.readInt();
		in.readFloatArray(values);
	}

	public float getGyroX() {
		return values[0];
	}

	public float getGyroY() {
		return values[1];
	}

	public float getGyroZ() {
		return values[2];
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
		out.writeInt(timestamp);
		out.writeFloatArray(values);

	}

	public static final Parcelable.Creator<GyroReading> CREATOR = new Parcelable.Creator<GyroReading>() {
		@Override
		public GyroReading createFromParcel(Parcel in) {
			return new GyroReading(in);
		}

		@Override
		public GyroReading[] newArray(int size) {
			return new GyroReading[size];
		}
	};

}
