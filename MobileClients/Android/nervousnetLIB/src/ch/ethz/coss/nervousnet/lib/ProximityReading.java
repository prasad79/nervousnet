package ch.ethz.coss.nervousnet.lib;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author prasad
 */
public class ProximityReading extends SensorReading {

	private float proximity;

	public ProximityReading(long timestamp, float proximity) {
		this.timestamp = timestamp;
		this.proximity = proximity;
	}

	/**
	 * @param in
	 */
	public ProximityReading(Parcel in) {
		readFromParcel(in);
	}

	public float getProximity() {
		return proximity;
	}


	public void readFromParcel(Parcel in) {

		timestamp = in.readLong();

		proximity = in.readFloat();
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
		out.writeFloat(proximity);
	}

	public static final Parcelable.Creator<ProximityReading> CREATOR = new Parcelable.Creator<ProximityReading>() {
		@Override
		public ProximityReading createFromParcel(Parcel in) {
			return new ProximityReading(in);
		}

		@Override
		public ProximityReading[] newArray(int size) {
			return new ProximityReading[size];
		}
	};

}
