package ch.ethz.coss.nervousnet.sensorQueries;
//Please only use the query to obtain data withing a certain timestamp,and get the descriptors
// do not use this with other methods!!!
import java.io.File;

import ch.ethz.coss.nervousnet.Queries.QueryNumVectorValue;
import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.SensorDescNoise;

public class SensorQueriesNoise extends QueryNumVectorValue<SensorDescNoise> {

	public SensorQueriesNoise(long timestamp_from, long timestamp_to, File file) {
		super(timestamp_from, timestamp_to, file);
		// TODO Auto-generated constructor stub
	}
	
	public long getSensorId(){
		return SensorDescNoise.SENSOR_ID;
	}
	
	public SensorDescNoise createDummyObject(){
		float[] b = new float[1000];	//maximum number of bands??
		return new SensorDescNoise(0,0,0,b);
	}

	@Override
	public SensorDescNoise createSensorDescVectorValue(SensorData sensorData) {
		// TODO Auto-generated method stub
		return new SensorDescNoise(sensorData);
	}

}