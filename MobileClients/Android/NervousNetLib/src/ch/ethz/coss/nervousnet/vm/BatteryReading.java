
package ch.ethz.coss.nervousnet.vm;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author prasad
 */
public class BatteryReading extends SensorReading {

	private static String LOG_TAG = "BatteryReading";
			
			
	public BatteryReading(long timestamp, float batteryPercent, boolean isCharging, boolean isUsbCharge,
			boolean isAcCharge) {
		Log.d(LOG_TAG, "Inside BatteryReading constructor 1");
		this.timestamp = timestamp;
		this.batteryPercent = batteryPercent;
		this.isCharging = isCharging;
		this.isUsbCharge = isUsbCharge;
		this.isAcCharge = isAcCharge;
	}

	/**
	 * @param in
	 */
	public BatteryReading(Parcel in) {
		Log.d(LOG_TAG, "Inside BatteryReading constructor 2");
		readFromParcel(in);
	}

	public void readFromParcel(Parcel in) {

		timestamp = in.readLong();
		batteryPercent = in.readFloat();

		boolean array[] = in.createBooleanArray();
		isCharging = array[0];
		isUsbCharge = array[1];
		isAcCharge = array[2];

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
		out.writeFloat(batteryPercent);
		out.writeBooleanArray(new boolean[] { isCharging, isUsbCharge, isAcCharge });

	}

	public static final Parcelable.Creator<BatteryReading> CREATOR = new Parcelable.Creator<BatteryReading>() {
		@Override
		public BatteryReading createFromParcel(Parcel in) {
			return new BatteryReading(in);
		}

		@Override
		public BatteryReading[] newArray(int size) {
			return new BatteryReading[size];
		}
	};
	
	
	

	/**
	 * @return the isCharging
	 */
	public boolean isCharging() {
		return isCharging;
	}

	/**
	 * @return the batteryPercent
	 */
	public float getBatteryPercent() {
		return batteryPercent;
	}

	/**
	 * @param batteryPercent the batteryPercent to set
	 */
	public void setBatteryPercent(float batteryPercent) {
		this.batteryPercent = batteryPercent;
	}
	
	public String toString(){
		return new String("BatteryReading - BatteryPercent = "+batteryPercent+", "+"isCharging = "+isCharging+", "+"isUsbCharging = "+isUsbCharge+", "+"isAcCharging = "+isAcCharge+".");
	}


	private float batteryPercent;
	private boolean isCharging;
	private boolean isUsbCharge;
	private boolean isAcCharge;

}
