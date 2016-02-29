/*******************************************************************************
 *
 *  *     Nervousnet - a distributed middleware software for social sensing. 
 *  *      It is responsible for collecting and managing data in a fully de-centralised fashion
 *  *
 *  *     Copyright (C) 2016 ETH Zürich, COSS
 *  *
 *  *     This file is part of Nervousnet Framework
 *  *
 *  *     Nervousnet is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     Nervousnet is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with SwarmPulse. If not, see <http://www.gnu.org/licenses/>.
 *  *
 *  *
 *  * 	Contributors:
 *  * 	Prasad Pulikal - prasad.pulikal@gess.ethz.ch  -  Initial API and implementation
 *******************************************************************************/
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
        config.addLongProperty("LastSyncTime");
        
    }
    
    private static void addSensors(Schema schema) {
    	
    	Entity locationData = schema.addEntity("LocationData");
    	locationData.implementsInterface("SensorDataImpl");
    	locationData.addLongProperty("TimeStamp").primaryKey().notNull();
    	locationData.addDoubleProperty("Latitude");
    	locationData.addDoubleProperty("Longitude");
    	locationData.addDoubleProperty("Altitude");
    	locationData.addLongProperty("Volatility").notNull();
    	locationData.addBooleanProperty("ShareFlag");
    	
    	Entity accelData = schema.addEntity("AccelData");
    	accelData.implementsInterface("SensorDataImpl");
    	accelData.addLongProperty("TimeStamp").primaryKey().notNull();
    	accelData.addFloatProperty("X");
    	accelData.addFloatProperty("Y");
    	accelData.addFloatProperty("Z");
    	accelData.addLongProperty("Volatility").notNull();
    	accelData.addBooleanProperty("ShareFlag");
    	
    	Entity batteryData = schema.addEntity("BatteryData");
    	batteryData.implementsInterface("SensorDataImpl");
    	
    	batteryData.addLongProperty("TimeStamp").primaryKey().notNull();
    	batteryData.addFloatProperty("Percent");
    	batteryData.addByteProperty("ChargingType"); //0 - No Charging, 1 - AC, 2 - USB, 3 - Wireless
    	batteryData.addByteProperty("Health"); 
    	batteryData.addFloatProperty("Temperature"); 
    	batteryData.addIntProperty("Volt"); 
    	batteryData.addLongProperty("Volatility").notNull();
    	batteryData.addBooleanProperty("ShareFlag");
    	
    	
    	Entity beaconData = schema.addEntity("BeaconData");
    	beaconData.implementsInterface("SensorDataImpl");
    
    	beaconData.addLongProperty("TimeStamp").primaryKey().notNull();
    	beaconData.addIntProperty("rssi");
    	beaconData.addLongProperty("mac");
    	beaconData.addLongProperty("advertisementMSB");
    	beaconData.addLongProperty("advertisementLSB");
    	beaconData.addLongProperty("bleuuidMSB");
    	beaconData.addLongProperty("bleuuidLSB");
    	beaconData.addIntProperty("major");
    	beaconData.addIntProperty("minor");
    	beaconData.addIntProperty("txpower");
    	beaconData.addLongProperty("Volatility").notNull();
    	beaconData.addBooleanProperty("ShareFlag");
    	
    	Entity connectivityData = schema.addEntity("ConnectivityData");
    	connectivityData.implementsInterface("SensorDataImpl");
    
    	connectivityData.addLongProperty("TimeStamp").primaryKey().notNull();
    	connectivityData.addBooleanProperty("isConnected");
    	connectivityData.addIntProperty("networkType");
    	connectivityData.addBooleanProperty("isRoaming");
    	connectivityData.addStringProperty("wifiHashId");
    	connectivityData.addIntProperty("wifiStrength");
    	connectivityData.addStringProperty("mobileHashId");
    	connectivityData.addLongProperty("Volatility").notNull();
    	connectivityData.addBooleanProperty("ShareFlag");
    	
    	Entity gyroData = schema.addEntity("GyroData");
    	gyroData.implementsInterface("SensorDataImpl");
    	
    	gyroData.addLongProperty("TimeStamp").primaryKey().notNull();
    	gyroData.addFloatProperty("GyroX");
    	gyroData.addFloatProperty("GyroY");
    	gyroData.addFloatProperty("GyroZ");
    	gyroData.addLongProperty("Volatility").notNull();
    	gyroData.addBooleanProperty("ShareFlag");
    	
    	Entity humidityData = schema.addEntity("HumidityData");
    	humidityData.implementsInterface("SensorDataImpl");
    	
    	humidityData.addLongProperty("TimeStamp").primaryKey().notNull();
    	humidityData.addFloatProperty("Humidity");
    	humidityData.addLongProperty("Volatility").notNull();
    	humidityData.addBooleanProperty("ShareFlag");
    	
    	Entity lightData = schema.addEntity("LightData");
    	lightData.implementsInterface("SensorDataImpl");
    	
    	lightData.addLongProperty("TimeStamp").primaryKey().notNull();
    	lightData.addFloatProperty("Lux");
    	lightData.addLongProperty("Volatility").notNull();
    	lightData.addBooleanProperty("ShareFlag");
    	
    	Entity magneticData = schema.addEntity("MagneticData");
    	magneticData.implementsInterface("SensorDataImpl");
    
    	magneticData.addLongProperty("TimeStamp").primaryKey().notNull();
    	magneticData.addFloatProperty("MagX");
    	magneticData.addFloatProperty("MagY");
    	magneticData.addFloatProperty("MagZ");
    	magneticData.addLongProperty("Volatility").notNull();
    	magneticData.addBooleanProperty("ShareFlag");
    	
    	
    	Entity noiseData = schema.addEntity("NoiseData");
    	noiseData.implementsInterface("SensorDataImpl");
    	
    	noiseData.addLongProperty("TimeStamp").primaryKey().notNull();
    	noiseData.addFloatProperty("Decibel");
    	noiseData.addLongProperty("Volatility").notNull();
    	noiseData.addBooleanProperty("ShareFlag");
    	
    	Entity pressureData = schema.addEntity("PressureData");
    	pressureData.implementsInterface("SensorDataImpl");
    	
    	pressureData.addLongProperty("TimeStamp").primaryKey().notNull();
    	pressureData.addFloatProperty("Pressure");
    	pressureData.addLongProperty("Volatility").notNull();
    	pressureData.addBooleanProperty("ShareFlag");
    	
    	
    	Entity proximityData = schema.addEntity("ProximityData");
    	proximityData.implementsInterface("SensorDataImpl");
    
    	proximityData.addLongProperty("TimeStamp").primaryKey().notNull();
    	proximityData.addFloatProperty("Proximity");
    	proximityData.addLongProperty("Volatility").notNull();
    	proximityData.addBooleanProperty("ShareFlag");
    	
    	Entity temperatureData = schema.addEntity("TemperatureData");
    	temperatureData.implementsInterface("SensorDataImpl");
    
    	temperatureData.addLongProperty("TimeStamp").primaryKey().notNull();
    	temperatureData.addFloatProperty("Temperature");
    	temperatureData.addLongProperty("Volatility").notNull();
    	temperatureData.addBooleanProperty("ShareFlag");
    	
    	Entity nnPacket = schema.addEntity("Packet");
    	nnPacket.addLongProperty("TimeStamp").primaryKey().notNull();   
    	nnPacket.addByteProperty("SynceStatus"); // 0= unsynched, 1=synched, 2 = empty so don't sync (ideally this should not happen)
    	
    	Property sensor1 = nnPacket.addLongProperty("sensor1").getProperty();
    	nnPacket.addToOne(locationData, sensor1);
    	
    	Property sensor2 = nnPacket.addLongProperty("sensor2").getProperty();
    	nnPacket.addToOne(accelData, sensor2);
    	
    	Property sensor3 = nnPacket.addLongProperty("sensor3").getProperty();
    	nnPacket.addToOne(batteryData, sensor3);
    	
    	Property sensor4 = nnPacket.addLongProperty("sensor4").getProperty();
    	nnPacket.addToOne(beaconData, sensor4);
    	
    	Property sensor5 = nnPacket.addLongProperty("sensor5").getProperty();
    	nnPacket.addToOne(connectivityData, sensor5);
    	
    	Property sensor6 = nnPacket.addLongProperty("sensor6").getProperty();
    	nnPacket.addToOne(gyroData, sensor6);
    	
    	Property sensor7 = nnPacket.addLongProperty("sensor7").getProperty();
    	nnPacket.addToOne(humidityData, sensor7);
    	
    	Property sensor8 = nnPacket.addLongProperty("sensor8").getProperty();
    	nnPacket.addToOne(lightData, sensor8);
    	
    	Property sensor9 = nnPacket.addLongProperty("sensor9").getProperty();
    	nnPacket.addToOne(magneticData, sensor9);
    	
    	Property sensor10 = nnPacket.addLongProperty("sensor10").getProperty();
    	nnPacket.addToOne(noiseData, sensor10);
    	
    	Property sensor11 = nnPacket.addLongProperty("sensor11").getProperty();
    	nnPacket.addToOne(pressureData, sensor11);
    	
    	Property sensor12 = nnPacket.addLongProperty("sensor12").getProperty();
    	nnPacket.addToOne(proximityData, sensor12);
    	
    	Property sensor13 = nnPacket.addLongProperty("sensor13").getProperty();
    	nnPacket.addToOne(temperatureData, sensor13);
    	
    }
    
  

}
