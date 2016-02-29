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

package ch.ethz.coss.nervousnet.ui;

import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.widget.Button;
import ch.ethz.coss.nervousnet.R;

/**
 * @author prasad
 *
 */

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

		ListPreference lp = (ListPreference) findPreference("time_limit_data_retention");

		if (lp.getValue() == null) {
			// to ensure we don't get a null value
			// set first value by default
			lp.setValueIndex(0);

		}
		int index = lp.findIndexOfValue(prefs.getString("time_limit_data_retention", "Forever"));

		CharSequence[] entries = lp.getEntries();

		lp.setSummary(index >= 0 ? entries[index] : null);
		lp.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String textValue = newValue.toString();

				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(textValue);

				CharSequence[] entries = listPreference.getEntries();

				preference.setSummary(index >= 0 ? entries[index] : null);

				return true;
			}
		});

		// Preference preference = (Preference)findPreference("UUID");
		// if(preference != null){
		//
		// LinearLayout reset_uuid_layout = (LinearLayout)
		// findViewById(preference.getLayoutResource());
		// System.out.println("reset_uuid_layout "+reset_uuid_layout);
		// if(reset_uuid_layout != null) {
		// Button resetButton = (Button)
		// reset_uuid_layout.findViewById(R.id.reset);
		//
		//
		// resetButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// TextView txtUUID = (TextView) v.findViewById(R.id.UUID);
		// System.out.println("txtUUID "+txtUUID.getText());
		// txtUUID.setText("Setting UUID here");
		//
		// }
		// });
		// }
		// }
		//
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		
	}
}
	
	