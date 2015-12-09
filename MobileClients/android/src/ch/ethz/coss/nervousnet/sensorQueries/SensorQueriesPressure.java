package ch.ethz.coss.nervousnet.sensorQueries;

import java.io.File;

import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.SensorDescPressure;
import ch.ethz.coss.nervousnet.vm.lae.Queries.QueryNumSingleValue;

public class SensorQueriesPressure extends QueryNumSingleValue<SensorDescPressure> {

	@Override
	public long getSensorId() {
		return SensorDescPressure.SENSOR_ID;
	}

	public SensorQueriesPressure(long timestamp_from, long timestamp_to, File file) {
		super(timestamp_from, timestamp_to, file);
	}

	@Override
	public SensorDescPressure createSensorDescSingleValue(SensorData sensorData) {
		return new SensorDescPressure(sensorData);
	}

	@Override
	public SensorDescPressure createDummyObject() {
		return new SensorDescPressure(0, 0);
	}
}