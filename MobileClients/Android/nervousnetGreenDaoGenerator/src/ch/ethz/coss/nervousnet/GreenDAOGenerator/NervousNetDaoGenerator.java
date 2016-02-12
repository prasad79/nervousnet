package ch.ethz.coss.nervousnet.GreenDAOGenerator;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class NervousNetDaoGenerator {

	private static final String OUTPUT_FOLDER = "model";
	
	public static void main(String[] args) throws Exception {
        Schema schema = new Schema(3, "ch.ethz.coss.nervousnet.vm.model");
        schema.enableKeepSectionsByDefault();
        addConfig(schema);
        addSensors(schema);
        new DaoGenerator().generateAll(schema, OUTPUT_FOLDER);
    }
	
    private static void addConfig(Schema schema) {
        Entity config = schema.addEntity("Config");
        config.addIdProperty();
        config.addStringProperty("UUID");
        config.addStringProperty("DeviceBrand");
        config.addStringProperty("DeviceModel");
        config.addStringProperty("DeviceOS");
        config.addStringProperty("DeviceOSversion");
        config.addDateProperty("LastSyncTime");
        
    }
    
    private static void addSensors(Schema schema) {
    	Entity sensorReading = schema.addEntity("SensorReading");
    	sensorReading.addIntProperty("Type");
    	sensorReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	sensorReading.addLongProperty("Volatility").notNull();
    	sensorReading.addBooleanProperty("ShareFlag");
    	
    	Entity locationReading = schema.addEntity("LocationReading");
    	locationReading.setSuperclass("SensorReading");
    	locationReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	locationReading.addDoubleProperty("Latitude");
    	locationReading.addDoubleProperty("Longitude");
    	locationReading.addDoubleProperty("Altitude");
    	
    	Entity accelReading = schema.addEntity("AccelReading");
    	accelReading.setSuperclass("SensorReading");
    	accelReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	accelReading.addFloatProperty("X");
    	accelReading.addFloatProperty("Y");
    	accelReading.addFloatProperty("Z");
    	
    	Entity batteryReading = schema.addEntity("BatteryReading");
    	batteryReading.setSuperclass("SensorReading");
    	batteryReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	batteryReading.addFloatProperty("Percent");
    	batteryReading.addByteProperty("ChargingType"); //0 - No Charging, 1 - AC, 2 - USB, 3 - Wireless
    	batteryReading.addByteProperty("Health"); 
    	batteryReading.addFloatProperty("Temperature"); 
    	batteryReading.addIntProperty("Volt"); 
    	
    	
    	Entity beaconReading = schema.addEntity("BeaconReading");
    	beaconReading.setSuperclass("SensorReading");
    	beaconReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	
    	Entity connectivityReading = schema.addEntity("ConnectivityReading");
    	connectivityReading.setSuperclass("SensorReading");
    	connectivityReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	
    	
    	Entity gyroReading = schema.addEntity("GyroReading");
    	gyroReading.setSuperclass("SensorReading");
    	gyroReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	
    	Entity humidityReading = schema.addEntity("HumidityReading");
    	humidityReading.setSuperclass("SensorReading");
    	humidityReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	
    	Entity lightReading = schema.addEntity("LightReading");
    	lightReading.setSuperclass("SensorReading");
    	lightReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	lightReading.addFloatProperty("Lux");
    	
    	
    	Entity magneticReading = schema.addEntity("MagneticReading");
    	magneticReading.setSuperclass("SensorReading");
    	magneticReading.addDateProperty("TimeStamp").primaryKey().notNull();
    
    	Entity noiseReading = schema.addEntity("NoiseReading");
    	noiseReading.setSuperclass("SensorReading");
    	noiseReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	
    	Entity pressureReading = schema.addEntity("PressureReading");
    	pressureReading.setSuperclass("SensorReading");
    	pressureReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	
    	
    	Entity proximityReading = schema.addEntity("ProximityReading");
    	proximityReading.setSuperclass("SensorReading");
    	proximityReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	
    	Entity temperatureReading = schema.addEntity("TemperatureReading");
    	temperatureReading.setSuperclass("SensorReading");
    	temperatureReading.addDateProperty("TimeStamp").primaryKey().notNull();
    	
    	Entity nnPacket = schema.addEntity("Packet");
    	nnPacket.addDateProperty("TimeStamp").primaryKey().notNull();
    	
    	
    	Property sensor1 = nnPacket.addDateProperty("sensor1").getProperty();
    	nnPacket.addToOne(locationReading, sensor1);
    	
    	Property sensor2 = nnPacket.addDateProperty("sensor2").getProperty();
    	nnPacket.addToOne(accelReading, sensor2);
    	
    	
    	Property sensor3 = nnPacket.addDateProperty("sensor3").getProperty();
    	nnPacket.addToOne(batteryReading, sensor3);
    	
    	Property sensor4 = nnPacket.addDateProperty("sensor4").getProperty();
    	nnPacket.addToOne(beaconReading, sensor4);
    	
    	Property sensor5 = nnPacket.addDateProperty("sensor5").getProperty();
    	nnPacket.addToOne(connectivityReading, sensor5);
    	
    	Property sensor6 = nnPacket.addDateProperty("sensor6").getProperty();
    	nnPacket.addToOne(gyroReading, sensor6);
    	
    	Property sensor7 = nnPacket.addDateProperty("sensor7").getProperty();
    	nnPacket.addToOne(humidityReading, sensor7);
    	
    	Property sensor8 = nnPacket.addDateProperty("sensor8").getProperty();
    	nnPacket.addToOne(lightReading, sensor8);
    	
    	Property sensor9 = nnPacket.addDateProperty("sensor9").getProperty();
    	nnPacket.addToOne(magneticReading, sensor9);
    	
    	Property sensor10 = nnPacket.addDateProperty("sensor10").getProperty();
    	nnPacket.addToOne(noiseReading, sensor10);
    	
    	Property sensor11 = nnPacket.addDateProperty("sensor11").getProperty();
    	nnPacket.addToOne(pressureReading, sensor11);
    	
    	Property sensor12 = nnPacket.addDateProperty("sensor12").getProperty();
    	nnPacket.addToOne(proximityReading, sensor12);
    	
    	Property sensor13 = nnPacket.addDateProperty("sensor13").getProperty();
    	nnPacket.addToOne(temperatureReading, sensor13);
    	
    }
    
  

}
