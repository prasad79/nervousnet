/**
 * 
 */
package ch.ethz.coss.nervousnet.sensors;

import ch.ethz.coss.nervousnet.lib.SensorReading;

/**
 * @author prasad
 *
 */
public interface SensorStatusImplementation {

	public void doCollect();

	public SensorReading getReading();

}
