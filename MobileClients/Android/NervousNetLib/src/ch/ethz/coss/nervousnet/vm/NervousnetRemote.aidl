package ch.ethz.coss.nervousnet.vm;

import ch.ethz.coss.nervousnet.vm.BatteryReading;

	interface NervousnetRemote
	{
		int getCounter();
		
		BatteryReading getBatteryReading();
	
	    float getBatteryPercent();
	}