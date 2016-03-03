package ch.ethz.coss.nervousnet.lib;

import ch.ethz.coss.nervousnet.lib.BatteryReading;
import ch.ethz.coss.nervousnet.lib.LocationReading;
import ch.ethz.coss.nervousnet.lib.AccelerometerReading;
import ch.ethz.coss.nervousnet.lib.ConnectivityReading;
import ch.ethz.coss.nervousnet.lib.GyroReading;

	interface NervousnetRemote
	{
		BatteryReading getBatteryReading();
	    
	    LocationReading getLocationReading();
	    
	    AccelerometerReading getAccelerometerReading();
	    
	    ConnectivityReading getConnectivityReading();
	    
	    GyroReading getGyroReading();
	}