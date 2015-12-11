package ch.ethz.coss.nervousnet.sensorQueries;

import java.io.File;

import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.SensorDescLight;
import ch.ethz.coss.nervousnet.vm.lae.Queries.QueryNumSingleValue;

public class SensorQueriesLight extends QueryNumSingleValue<SensorDescLight> {

	@Override
	public long getSensorId() {
		return SensorDescLight.SENSOR_ID;
	}

	public SensorQueriesLight(long timestamp_from, long timestamp_to, File file) {
		super(timestamp_from, timestamp_to, file);
	}

	@Override
	public SensorDescLight createSensorDescSingleValue(SensorData sensorData) {
		return new SensorDescLight(sensorData);
	}

	@Override
	public SensorDescLight createDummyObject() {
		return new SensorDescLight(0, 0);
	}

}