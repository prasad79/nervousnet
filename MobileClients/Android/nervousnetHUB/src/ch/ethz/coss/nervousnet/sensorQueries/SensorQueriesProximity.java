package ch.ethz.coss.nervousnet.sensorQueries;

import java.io.File;
import java.util.ArrayList;

import android.content.IntentSender.SendIntentException;
import ch.ethz.coss.nervousnet.Queries.*;
import ch.ethz.coss.nervousnet.nervousproto.SensorUploadProtos.SensorUpload.SensorData;
import ch.ethz.coss.nervousnet.sensors.*;

public class SensorQueriesProximity extends
		QueryNumSingleValue<SensorDescProximity> {

	@Override
	public long getSensorId() {
		return SensorDescProximity.SENSOR_ID;
	}

	public SensorQueriesProximity(long timestamp_from, long timestamp_to,
			File file) {
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