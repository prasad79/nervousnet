package ch.ethz.coss.nervousnet.vm;

import ch.ethz.coss.nervousnet.vm.BatteryReading;
import ch.ethz.coss.nervousnet.vm.LocationReading;

	interface NervousnetRemote
	{
		int getCounter();
		
		BatteryReading getBatteryReading();
	
	    float getBatteryPercent();
	    
	    LocationReading getLocationReading();
	}