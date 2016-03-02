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
public class LocationReading extends SensorReading {

	public LocationReading(long timestamp, double[] latnLong, double altitude) {
		this.type = LibConstants.SENSOR_LOCATION;
		this.timestamp = timestamp;
		this.latnLong = latnLong;
		this.altitude = altitude;
	}



	/**
	 * @param in
	 */
	public LocationReading(Parcel in) {
		readFromParcel(in);
	}

	/**
	 * @return the latnLong
	 */
	public double[] getLatnLong() {
		return latnLong;
	}

	/**
	 * @return the latnLong
	 */
	public double getAltitude() {
		return altitude;
	}

	/**
	 * @param latnLong
	 *            the latnLong to set
	 */
	public void setLatnLong(double[] latnLong) {
		this.latnLong = latnLong;
	}

	public void readFromParcel(Parcel in) {
		timestamp = in.readLong();
		latnLong = in.createDoubleArray();
		altitude = in.readDouble();
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
		out.writeDouble(altitude);

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
		if (latnLong == null)
			return new String("Location not set");
		return new String("Latitude = " + latnLong[0] + ", Longitude = " + latnLong[1] + ", Altitude = " + altitude);
	}

	private double[] latnLong;
	private double altitude;

}
