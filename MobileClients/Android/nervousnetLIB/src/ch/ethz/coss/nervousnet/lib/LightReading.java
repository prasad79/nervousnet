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
public class LightReading extends SensorReading {

	private float value;

	public LightReading(long timestamp, float value) {
		this.type = LibConstants.SENSOR_LIGHT;
		this.timestamp = timestamp;
		this.value = value;
	}

	/**
	 * @param in
	 */
	public LightReading(Parcel in) {
		readFromParcel(in);
	}

	public void readFromParcel(Parcel in) {

		timestamp = in.readInt();
		value = in.readFloat();
	}

	public float getLuxValue() {
		return value;
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
		out.writeFloat(value);

	}

	public static final Parcelable.Creator<LightReading> CREATOR = new Parcelable.Creator<LightReading>() {
		@Override
		public LightReading createFromParcel(Parcel in) {
			return new LightReading(in);
		}

		@Override
		public LightReading[] newArray(int size) {
			return new LightReading[size];
		}
	};

}
