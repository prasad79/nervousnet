package ch.ethz.coss.nervousnet.lib;

import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.LocationReading;
import ch.ethz.coss.nervousnet.lib.AccelerometerReading;

	interface NervousnetRemote
	{
		BatteryReading getBatteryReading();
	    
	    LocationReading getLocationReading();
	    
	    AccelerometerReading getAccelerometerReading();
	}