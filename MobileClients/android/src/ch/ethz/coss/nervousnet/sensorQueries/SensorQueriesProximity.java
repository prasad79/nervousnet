package ch.ethz.coss.nervousnet.sensorQueries;

import java.io.File;

import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.SensorDescProximity;
import ch.ethz.coss.nervousnet.vm.lae.Queries.QueryNumSingleValue;

public class SensorQueriesProximity extends QueryNumSingleValue<SensorDescProximity> {

	@Override
	public long getSensorId() {
		return SensorDescProximity.SENSOR_ID;
	}

	public SensorQueriesProximity(long timestamp_from, long timestamp_to, File file) {
		super(timestamp_from, timestamp_to, file);
	}

	@Override
	public SensorDescProximity createSensorDescSingleValue(SensorData sensorData) {
		return new SensorDescProximity(sensorData);
	}

	@Override
	public SensorDescProximity createDummyObject() {
		return new SensorDescProximity(0, 0);
	}
}