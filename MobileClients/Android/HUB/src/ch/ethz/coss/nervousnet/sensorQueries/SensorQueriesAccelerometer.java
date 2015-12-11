package ch.ethz.coss.nervousnet.sensorQueries;

import java.io.File;

import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.SensorDescAccelerometerNew;
import ch.ethz.coss.nervousnet.vm.lae.Queries.QueryNumVectorValue;

public class SensorQueriesAccelerometer extends QueryNumVectorValue<SensorDescAccelerometerNew> {

	@Override
	public long getSensorId() {
		return SensorDescAccelerometerNew.SENSOR_ID;
	}

	public SensorQueriesAccelerometer(long timestamp_from, long timestamp_to, File file) {
		super(timestamp_from, timestamp_to, file);
	}

	// @Override
	// public SensorDescAccelerometerNew createSensorDescVectorValue(
	// SensorData sensorData) {
	// // TODO Auto-generated method stub
	// return new SensorDescAccelerometerNew(sensorData);
	// }

	@Override
	public SensorDescAccelerometerNew createDummyObject() {
		// TODO Auto-generated method stub
		return new SensorDescAccelerometerNew(0, 0, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.ethz.coss.nervousnet.Queries.QueryNumVectorValue#
	 * createSensorDescVectorValue(ch.ethz.coss.nervousnet.nervousproto.
	 * SensorUploadProtos.SensorUpload.SensorData)
	 */
	@Override
	public SensorDescAccelerometerNew createSensorDescVectorValue(SensorData sensorData) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * public SensorDescAccelerometerNew getMaxAverageValue() {
	 * SensorDescAccelerometerNew maxAccSensDesc = null; float maxAverage = 0;
	 * for (SensorData sensorData : list) { SensorDescAccelerometerNew sensDesc
	 * = new SensorDescAccelerometerNew( sensorData);
	 * 
	 * float x = Math.abs(sensDesc.getAccX()); float y =
	 * Math.abs(sensDesc.getAccY()); float z = Math.abs(sensDesc.getAccZ());
	 * float newAverage = (x + y + z) / 3; if (newAverage > maxAverage) {
	 * maxAverage = newAverage; maxAccSensDesc = sensDesc; } } return
	 * maxAccSensDesc; }
	 * 
	 * public SensorDescAccelerometerNew getMinAverageValue() {
	 * SensorDescAccelerometerNew minAccSensDesc = null; float maxAverage =
	 * Float.MAX_VALUE; for (SensorData sensorData : list) {
	 * SensorDescAccelerometerNew sensDesc = new SensorDescAccelerometerNew(
	 * sensorData);
	 * 
	 * float x = Math.abs(sensDesc.getAccX()); float y =
	 * Math.abs(sensDesc.getAccY()); float z = Math.abs(sensDesc.getAccZ());
	 * float newAverage = (x + y + z) / 3; if (newAverage < maxAverage) {
	 * maxAverage = newAverage; minAccSensDesc = sensDesc; } } return
	 * minAccSensDesc; }
	 * 
	 * @Override public ArrayList<SensorDescAccelerometerNew>
	 * getSensorDescriptorList() { ArrayList<SensorDescAccelerometerNew>
	 * descList = new ArrayList<SensorDescAccelerometerNew>(); for (SensorData
	 * sensorData : list) { descList.add(new
	 * SensorDescAccelerometerNew(sensorData)); } return descList; }
	 */

}