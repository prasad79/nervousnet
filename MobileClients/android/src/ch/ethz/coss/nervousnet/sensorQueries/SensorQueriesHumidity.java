package ch.ethz.coss.nervousnet.sensorQueries;

import java.io.File;

import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.SensorDescHumidity;
import ch.ethz.coss.nervousnet.vm.lae.Queries.QueryNumSingleValue;

public class SensorQueriesHumidity extends QueryNumSingleValue<SensorDescHumidity> {

	@Override
	public long getSensorId() {
		return SensorDescHumidity.SENSOR_ID;
	}

	public SensorQueriesHumidity(long timestamp_from, long timestamp_to, File file) {
		super(timestamp_from, timestamp_to, file);
	}

	@Override
	public SensorDescHumidity createSensorDescSingleValue(SensorData sensorData) {
		return new SensorDescHumidity(sensorData);
	}

	@Override
	public SensorDescHumidity createDummyObject() {
		return new SensorDescHumidity(0, 0);
	}

}