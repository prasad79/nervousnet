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
package ch.ethz.coss.nervousnet.hub;

import android.content.Context;
import ch.ethz.coss.nervousnet.vm.NervousnetVM;

public class NervousnetHubManager {

	private static NervousnetHubManager _instance = null;

	private NervousnetHubManager() {
	}

	public static NervousnetHubManager getInstance() {
		if (_instance == null) {
			_instance = new NervousnetHubManager();
		}

		return _instance;
	}

	public byte getState(Context context) {
		return NervousnetVM.getInstance(context).getState();
	}

	public void setState(Context context, byte state) {
		NervousnetVM.getInstance(context).storeNervousnetState(state);
	}

}
