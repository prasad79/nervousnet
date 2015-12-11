package ch.ethz.coss.nervousnet.sensorQueries;

import java.io.File;

import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.SensorDescBattery;
import ch.ethz.coss.nervousnet.vm.lae.Queries.QueryNumSingleValue;

public class SensorQueriesBattery extends QueryNumSingleValue<SensorDescBattery> {

	@Override
	public long getSensorId() {
		return SensorDescBattery.SENSOR_ID;
	}

	public SensorQueriesBattery(long timestamp_from, long timestamp_to, File file) {
		super(timestamp_from, timestamp_to, file);
	}

	// @Override
	// public
	// SensorDescBattery createSensorDescSingleValue(SensorData sensorData) {
	// return new SensorDescBattery(sensorData);
	// }

	@Override
	public SensorDescBattery createDummyObject() {
		return new SensorDescBattery(0, 0, false, false, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ethz.coss.nervousnet.Queries.QueryNumSingleValue#
	 * createSensorDescSingleValue(ch.ethz.coss.nervousnet.nervousproto.
	 * SensorUploadProtos.SensorUpload.SensorData)
	 */
	@Override
	public SensorDescBattery createSensorDescSingleValue(SensorData sensorData) {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO PP
	// /* (non-Javadoc)
	// * @see
	// ch.ethz.coss.nervousnet.Queries.QueryNumSingleValue#createSensorDescSingleValue(ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData)
	// */
	// @Override
	// public SensorDescBattery createSensorDescSingleValue(SensorData
	// sensorData) {
	// // TODO Auto-generated method stub
	// return new SensorDescBattery(sensorData);
	// }
}