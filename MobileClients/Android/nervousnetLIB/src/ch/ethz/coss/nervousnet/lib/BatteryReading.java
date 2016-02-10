
package ch.ethz.coss.nervousnet.lib;

import android.os.BatteryManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author prasad
 */
public class BatteryReading extends SensorReading {

	private float percent;
	private boolean isCharging;
	private byte charging_type = 0; // 0 = Unknown, 1 = USB, 2 = AC
	private float temp = 0;
	private long volt = 0;
	private byte health = 0; // 0 = Unknown, -1 is not supported

	public BatteryReading(int timestamp, float batteryPercent, boolean isCharging, boolean isUsbCharge,
			boolean isAcCharge, float temp, long volt, byte health) {
		this.timestamp = timestamp;
		this.percent = batteryPercent;
		this.isCharging = isCharging;
		this.charging_type = (byte) (isUsbCharge ? 1 : (isAcCharge ? 2 : 0));
		this.temp = temp;
		this.volt = volt;
		this.health = health;
	}
	

	public BatteryReading(boolean isCollect) {
		super(isCollect);
	}
	

	/**
	 * @param in
	 */
	public BatteryReading(Parcel in) {
		readFromParcel(in);
	}

	public void readFromParcel(Parcel in) {

		timestamp = in.readInt();
		percent = in.readFloat();
		boolean array[] = in.createBooleanArray();
		isCharging = array[0];

		charging_type = in.readByte();
		temp = in.readFloat();
		volt = in.readLong();
		health = in.readByte();

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
		out.writeFloat(percent);
		out.writeBooleanArray(new boolean[] { isCharging });
		out.writeByte(charging_type);
		out.writeFloat(temp);
		out.writeLong(volt);
		out.writeByte(health);

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
	 * @return the percent
	 */
	public float getPercent() {
		return percent;
	}

	/**
	 * @return the charging_type
	 */
	public byte getCharging_type() {
		return charging_type;
	}

	/**
	 * @return the temp
	 */
	public float getTemp() {
		return temp / 10;
	}

	/**
	 * @return the volt
	 */
	public long getVolt() {
		return volt;
	}

	/**
	 * @return the health
	 */
	public byte getHealth() {
		return health;
	}

	/**
	 * @return the health status in string
	 */
	public String getHealthString() {

		switch (health) {
		default:
		case 0:
		case (byte) BatteryManager.BATTERY_HEALTH_UNKNOWN:
			return new String("unknown");

		case (byte) BatteryManager.BATTERY_HEALTH_GOOD:
			return new String("Good");

		case (byte) BatteryManager.BATTERY_HEALTH_OVERHEAT:
			return new String("Overheated");

		case (byte) BatteryManager.BATTERY_HEALTH_DEAD:
			return new String("Dead");

		case (byte) BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
			return new String("Over Voltage");

		case (byte) BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
			return new String("Unspecified Failure");

		case (byte) BatteryManager.BATTERY_HEALTH_COLD:
			return new String("Cold");

		}

	}

}
