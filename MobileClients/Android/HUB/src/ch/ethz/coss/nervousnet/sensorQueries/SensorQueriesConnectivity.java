package ch.ethz.coss.nervousnet.sensorQueries;

import java.io.File;

import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.SensorDescConnectivity;
import ch.ethz.coss.nervousnet.vm.lae.Queries.QueryNumVectorValue;

// only uses few functions of query NOT ALL
//does not make sense otherwise
// find way to block other usage
public class SensorQueriesConnectivity extends QueryNumVectorValue<SensorDescConnectivity> {

	@Override
	public long getSensorId() {

		return SensorDescConnectivity.SENSOR_ID;
	}

	public SensorQueriesConnectivity(long timestamp_from, long timestamp_to, File file) {
		super(timestamp_from, timestamp_to, file);
	}

	@Override
	public SensorDescConnectivity createDummyObject() {
		return new SensorDescConnectivity(0, false, 0, false, null, 0, null);
	}

	@Override
	public SensorDescConnectivity createSensorDescVectorValue(SensorData sensorData) {
		// TODO Auto-generated method stub
		return new SensorDescConnectivity(sensorData);
	}

	// Please test this function !!

}