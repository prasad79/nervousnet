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
 *******************************************************************************/
package ch.ethz.coss.nervousnet.utils;

import java.util.UUID;

public class UnsignedArithmetic {

	public static short upcastToShort(byte in) {
		// ............................0xHHLL
		return (short) ((in) & 0x00FF);
	}

	public static int upcastToInt(byte in) {
		// .................0xHHHHLLLL
		return (in) & 0x000000FF;
	}

	public static long upcastToLong(byte in) {
		// ..................0xHHHHHHHHLLLLLLLL
		return (in) & 0x00000000000000FFL;
	}

	public static int upcastToInt(short in) {
		// .................0xHHHHLLLL
		return (in) & 0x0000FFFF;
	}

	public static long upcastToLong(short in) {
		// ..................0xHHHHHHHHLLLLLLLL
		return (in) & 0x000000000000FFFFL;
	}

	public static long upcastToLong(int in) {
		// ..................0xHHHHHHHHLLLLLLLL
		return (in) & 0x00000000FFFFFFFFL;
	}

	public static UUID toUUIDLittleEndian(byte[] data, int start, int stop) {
		long msb = 0x0000000000000000L;
		long lsb = 0x0000000000000000L;
		int size = stop - start + 1;
		int msbsize = size - 8;
		int lsbsize = 8;
		for (int i = 0; i < msbsize; i++) {
			msb |= upcastToLong(data[stop - i]) << ((msbsize - 1 - i) * 8);
		}
		for (int i = 0; i < lsbsize; i++) {
			lsb |= upcastToLong(data[stop - msbsize - i]) << ((lsbsize - 1 - i) * 8);
		}
		return new UUID(msb, lsb);
	}

	public static UUID toUUIDBigEndian(byte[] data, int start, int stop) {
		long msb = 0x0000000000000000L;
		long lsb = 0x0000000000000000L;
		int size = stop - start + 1;
		int msbsize = size - 8;
		int lsbsize = 8;
		for (int i = 0; i < msbsize; i++) {
			msb |= upcastToLong(data[start + i]) << ((msbsize - 1 - i) * 8);
		}
		for (int i = 0; i < lsbsize; i++) {
			lsb |= upcastToLong(data[start + msbsize + i]) << ((lsbsize - 1 - i) * 8);
		}
		return new UUID(msb, lsb);
	}

	public static long stringMacToLong(String mac) {
		long macLong = 0x0000000000000000L;
		String[] macSplit = mac.split(":");
		int length = macSplit.length;
		for (int i = 0; i < length; i++) {
			// Workaround for stupid sign extension that always is the case with
			// Java
			macLong |= upcastToLong(Short.decode("0x" + macSplit[i])) << (length - 1 - i) * 8;
		}
		return macLong;
	}

}
