package ch.ethz.coss.nervousnet.lib;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author prasad
 */
public class DeviceReading extends SensorReading {


	public DeviceReading(long timestamp, float[] values) {
		this.type = Constants.SENSOR_DEVICE;
		this.timestamp = timestamp;
	}

	/**
	 * @param in
	 */
	public DeviceReading(Parcel in) {
		readFromParcel(in);
	}

	

	public void readFromParcel(Parcel in) {

		timestamp = in.readLong();

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
	}

	public static final Parcelable.Creator<DeviceReading> CREATOR = new Parcelable.Creator<DeviceReading>() {
		@Override
		public DeviceReading createFromParcel(Parcel in) {
			return new DeviceReading(in);
		}

		@Override
		public DeviceReading[] newArray(int size) {
			return new DeviceReading[size];
		}
	};

}
