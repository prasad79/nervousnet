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
 *  *     along with NervousNet. If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 *  * 	Contributors:
 *  * 	Prasad Pulikal - prasad.pulikal@gess.ethz.ch  -  Initial API and implementation
 *******************************************************************************/
/**
 * 
 */
package ch.ethz.coss.nervousnet.lib;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author prasad
 *
 */
public abstract class SensorReading implements Parcelable {
	public int type = 0; // 0-light, 1- sound,
	public long timestamp;
	public String uuid;

	/*
	 * Volatility defines the time this specific data object will be kept alive
	 * on the Server and Database. Possible values: -1 = Permanently store in
	 * database 0 = Do not store in the database. by default all data pushed to
	 * the Server will have a value of 0. User has to explicitly change the
	 * settings to required days. 1 to (any long number) = Time the data can be
	 * kept alive in the database in Seconds.
	 */
	public long volatility = -1;

	public boolean isCollect;
	public boolean isShare;
	
	/*
	 * This variable contains the errorcode, if any.
	 * 0 - no error;
	 * 1 - Sensor Collection turned OFF at Global Settings level;
	 * 2 - Sensor Collection turned OFF at Sensors Settings Level;
	 * 
	 */
	public short errorCode = 0;


	public SensorReading() {
	}

	public SensorReading(boolean isCollect) {
	}
	
	
}
