
package ch.ethz.coss.nervousnet.vm;

import android.os.BatteryManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author prasad
 */
public class ConnectivityReading extends SensorReading {
	
	private boolean isConnected;
	private int networkType;
	private boolean isRoaming;
	private String wifiHashId;
	private int wifiStrength;
	private String mobileHashId;


	public ConnectivityReading(int timestamp, boolean isConnected, int networkType, boolean isRoaming, String wifiHashId, int wifiStrength, String mobileHashId) {
		this.timestamp = timestamp;
		this.isConnected = isConnected;
		this.networkType = networkType;
		this.isRoaming = isRoaming;
		this.wifiHashId = wifiHashId;
		this.wifiStrength = wifiStrength;
		this.mobileHashId = mobileHashId;
	}

	/**
	 * @param in
	 */
	public ConnectivityReading(Parcel in) {
		readFromParcel(in);
	}

	public void readFromParcel(Parcel in) {
		timestamp = in.readInt();
		isConnected = in.readByte() == 1 ? true: false;
		networkType = in.readInt();
		isRoaming = in.readByte() == 1 ? true: false;
		wifiHashId = in.readString();
		wifiStrength = in.readInt();
		mobileHashId = in.readString();
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
		out.writeByte(isConnected ? (byte) 1: (byte) 0);
		out.writeInt(networkType);
		out.writeByte(isRoaming ? (byte) 1: (byte) 0);
		out.writeString(wifiHashId);
		out.writeInt(wifiStrength);
		out.writeString(mobileHashId);
	}

	public static final Parcelable.Creator<ConnectivityReading> CREATOR = new Parcelable.Creator<ConnectivityReading>() {
		@Override
		public ConnectivityReading createFromParcel(Parcel in) {
			return new ConnectivityReading(in);
		}

		@Override
		public ConnectivityReading[] newArray(int size) {
			return new ConnectivityReading[size];
		}
	};







}
