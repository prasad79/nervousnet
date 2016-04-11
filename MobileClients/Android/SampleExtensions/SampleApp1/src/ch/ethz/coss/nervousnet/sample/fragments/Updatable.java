package ch.ethz.coss.nervousnet.sample.fragments;

import ch.ethz.coss.nervousnet.lib.SensorReading;

public interface Updatable {

	
	public void updateReadings(SensorReading reading);
	
}
