package ch.ethz.coss.nervousnet.vm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author prasad
 */
public class AccelerometerReading extends SensorReading {
	
	
	
	public static final long SENSOR_ID = 0x0000000000000000L;
	
	private float []values;
	 
	
	

	public AccelerometerReading(int timestamp, float [] values) {
		this.timestamp = timestamp;
		this.values = values;
	}

	/**
	 * @param in
	 */
	public AccelerometerReading(Parcel in) {
		readFromParcel(in);
	}
	
	
	public float getX(){
		return values[0];
	}
	
	public float getY(){
		return values[1];
	}
	
	public float getZ(){
		return values[2];
	}

	public void readFromParcel(Parcel in) {

		timestamp = in.readInt();
		
		in.readFloatArray(values);
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

	public static final Parcelable.Creator<AccelerometerReading> CREATOR = new Parcelable.Creator<AccelerometerReading>() {
		@Override
		public AccelerometerReading createFromParcel(Parcel in) {
			return new AccelerometerReading(in);
		}

		@Override
		public AccelerometerReading[] newArray(int size) {
			return new AccelerometerReading[size];
		}
	};

	




}
