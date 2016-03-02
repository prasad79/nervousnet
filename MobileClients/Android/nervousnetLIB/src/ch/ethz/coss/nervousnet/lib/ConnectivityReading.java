/*******************************************************************************
 *
 *  *     Nervousnet - a distributed middleware software for social sensing. 
 *  *      It is responsible for collecting and managing data in a fully de-centralised fashion
 *  *
 *  *     Copyright (C) 2016 ETH Zürich, COSS
 *  *
 *  *     This file is part of Nervousnet Framework
 *  *
 *  *     Nervousnet is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     Nervousnet is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with SwarmPulse. If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 *  * 	Contributors:
 *  * 	Prasad Pulikal - prasad.pulikal@gess.ethz.ch  -  Initial API and implementation
 *******************************************************************************/

package ch.ethz.coss.nervousnet.lib;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author prasad
 */
public class ConnectivityReading extends SensorReading {

	private boolean isConnected;

	/**
	 * @return the isConnected
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * @param isConnected
	 *            the isConnected to set
	 */
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	/**
	 * @return the networkType
	 */
	public int getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType
	 *            the networkType to set
	 */
	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	/**
	 * @return the isRoaming
	 */
	public boolean isRoaming() {
		return isRoaming;
	}

	/**
	 * @param isRoaming
	 *            the isRoaming to set
	 */
	public void setRoaming(boolean isRoaming) {
		this.isRoaming = isRoaming;
	}

	/**
	 * @return the wifiHashId
	 */
	public String getWifiHashId() {
		return wifiHashId;
	}

	/**
	 * @param wifiHashId
	 *            the wifiHashId to set
	 */
	public void setWifiHashId(String wifiHashId) {
		this.wifiHashId = wifiHashId;
	}

	/**
	 * @return the wifiStrength
	 */
	public int getWifiStrength() {
		return wifiStrength;
	}

	/**
	 * @param wifiStrength
	 *            the wifiStrength to set
	 */
	public void setWifiStrength(int wifiStrength) {
		this.wifiStrength = wifiStrength;
	}

	/**
	 * @return the mobileHashId
	 */
	public String getMobileHashId() {
		return mobileHashId;
	}

	/**
	 * @param mobileHashId
	 *            the mobileHashId to set
	 */
	public void setMobileHashId(String mobileHashId) {
		this.mobileHashId = mobileHashId;
	}

	private int networkType;
	private boolean isRoaming;
	private String wifiHashId;
	private int wifiStrength;
	private String mobileHashId;

	public ConnectivityReading(long timestamp, boolean isConnected, int networkType, boolean isRoaming,
			String wifiHashId, int wifiStrength, String mobileHashId) {
		this.type = LibConstants.SENSOR_CONNECTIVITY;
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
		timestamp = in.readLong();
		isConnected = in.readByte() == 1 ? true : false;
		networkType = in.readInt();
		isRoaming = in.readByte() == 1 ? true : false;
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
		out.writeLong(timestamp);
		out.writeByte(isConnected ? (byte) 1 : (byte) 0);
		out.writeInt(networkType);
		out.writeByte(isRoaming ? (byte) 1 : (byte) 0);
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
