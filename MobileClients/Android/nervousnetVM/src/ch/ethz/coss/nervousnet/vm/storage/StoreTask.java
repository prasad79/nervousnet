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
 *******************************************************************************/
package ch.ethz.coss.nervousnet.vm.storage;

import android.os.AsyncTask;
import android.content.Context;
import ch.ethz.coss.nervousnet.vm.NervousnetVM;

public class StoreTask extends AsyncTask<SensorDataImpl, Void, Void> {

	private Context context;

	public StoreTask(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(SensorDataImpl... params) {

		if (params != null && params.length > 0) {
			NervousnetVM nervousVM = NervousnetVM.getInstance(context);
			for (int i = 0; i < params.length; i++) {
				// TODO: PP
				nervousVM.storeSensor(params[i]);
			}
		}
		return null;
	}

}