package ch.ethz.coss.nervousnet.sensorQueries;

import java.io.File;

import ch.ethz.coss.nervousnet.Queries.*;
import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.*;

import java.util.ArrayList;

public class SensorQueriesTemperature extends
		QueryNumSingleValue<SensorDescTemperature> {

	@Override
	public long getSensorId() {
		return SensorDescTemperature.SENSOR_ID;
	}

	public SensorQueriesTemperature(long timestamp_from, long timestamp_to,
			File file) {
		super(timestamp_from, timestamp_to, file);
	}

	@Override
	public SensorDescTemperature createSensorDescSingleValue(SensorData sensorData) {
		return new SensorDescTemperature(sensorData);
	}

	@Override
	public SensorDescTemperature createDummyObject() {
		return new SensorDescTemperature(0, 0);
	}

}