package ch.ethz.coss.nervousnet.sensors;

import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;

public abstract class SensorDescSingleValue extends SensorDesc {

	public SensorDescSingleValue(long timestamp) {
		super(timestamp);
	}

	public SensorDescSingleValue(SensorData sensorData) {
		super(sensorData);
	}

	public abstract float getValue();

}