package ch.ethz.coss.nervousnet.hub.authentication;

import java.util.Enumeration;
import java.util.Hashtable;

import ch.ethz.coss.nervousnet.lib.LibConstants;

public class SensorAuthentication {

	private Hashtable<Integer, Byte> sensorAuthList = new Hashtable<Integer, Byte>();
	
	public void SensorAuthentication() {
		sensorAuthList.put(LibConstants.SENSOR_ACCELEROMETER, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_BATTERY,(byte)0);
		sensorAuthList.put(LibConstants.SENSOR_BLEBEACON,(byte)0);
		sensorAuthList.put(LibConstants.SENSOR_CONNECTIVITY, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_DEVICE, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_GYROSCOPE, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_HUMIDITY, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_LIGHT, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_LOCATION, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_MAGNETIC, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_NOISE, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_PRESSURE, (byte)0);
		sensorAuthList.put(LibConstants.SENSOR_PROXIMITY, (byte)0);
	}
	
	
	public void updateSensorAuthentication(int sensorID, byte accessRightsValue) {
		sensorAuthList.put(sensorID, accessRightsValue);
	}
	
	public String getSensorAuthenticationString(){
		String buffer = "";
		for (int key : sensorAuthList.keySet()) {
			buffer = +key+"|"+sensorAuthList.get(key);
		}
		
		return buffer;
	}
	
	
	public void parseSensorAuthenticationString(String sensorString) {
		
	}
	
	
}
