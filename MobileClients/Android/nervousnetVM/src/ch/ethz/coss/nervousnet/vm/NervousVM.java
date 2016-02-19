package ch.ethz.coss.nervousnet.vm;

import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import ch.ethz.coss.nervousnet.vm.model.AccelData;
import ch.ethz.coss.nervousnet.vm.model.AccelDataDao;
import ch.ethz.coss.nervousnet.vm.model.Config;
import ch.ethz.coss.nervousnet.vm.model.ConfigDao;
import ch.ethz.coss.nervousnet.vm.model.DaoMaster;
import ch.ethz.coss.nervousnet.vm.model.DaoMaster.DevOpenHelper;
import ch.ethz.coss.nervousnet.vm.model.DaoSession;
import ch.ethz.coss.nervousnet.vm.model.LocationData;
import ch.ethz.coss.nervousnet.vm.model.LocationDataDao;
import ch.ethz.coss.nervousnet.vm.model.SensorDataImpl;


public class NervousVM {

	private static NervousVM nervousVM;
	private static String TAG = "NERVOUS_VM";
	private static String DB_NAME = "NN-DB";
	
	private UUID uuid;
	private Context context;
	DaoMaster daoMaster;
	DaoSession daoSession;
	SQLiteDatabase sqlDB;
	ConfigDao configDao;
	AccelDataDao accDao;
	LocationDataDao locDao;
	
	public static synchronized  NervousVM getInstance(Context context) {
		if (nervousVM == null) {
			nervousVM = new NervousVM(context);
		}
		return nervousVM;
	}

	public NervousVM(Context context) {
		Log.d(TAG, "Inside constructor");
		 try {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
			
			 Log.d(TAG, "Inside constructor1");
			 sqlDB = helper.getWritableDatabase();
		} catch (Exception e) {
			Log.e(TAG, "Inside constructor and creating DB = "+DB_NAME, e);
		}
		 Log.d(TAG, "Inside constructor2");
		 daoMaster = new DaoMaster(sqlDB);
		 Log.d(TAG, "Inside constructor3");
		 daoSession = daoMaster.newSession();
		 Log.d(TAG, "Inside constructor4");
		 configDao = daoSession.getConfigDao();
		 
		 accDao = daoSession.getAccelDataDao();
		 
		 locDao = daoSession.getLocationDataDao();
		 
	    boolean hasVMConfig = loadVMConfig();
		if (!hasVMConfig) {
			Log.d(TAG, "Inside no config found. Create a new config.");
			uuid = UUID.randomUUID();
			 
			storeVMConfig();
		}
		
	}

//	private synchronized boolean removeOldPages(long sensorID, long currentPage, long maxPages) {
//		boolean stmHasChanged = false;
//		boolean success = true;
//		TreeMap<PageInterval, PageInterval> treeMap = sensorTreeMap.get(sensorID);
//		if (treeMap != null) {
//			for (long i = currentPage - maxPages; i >= 0; i--) {
//				PageInterval pi = treeMap.get(new PageInterval(new Interval(0, 0), i));
//				if (pi == null) {
//					break;
//				}
//				treeMap.remove(pi);
//				SensorStorePage stp = new SensorStorePage(dir, sensorID, pi.getPageNumber());
//				boolean successEvict = stp.evict();
//				success = success && successEvict;
//			}
//			if (maxPages == 0) {
//				// All removed, delete sensor as a whole
//				sensorTreeMap.remove(sensorID);
//				stmHasChanged = true;
//			} else {
//				PageInterval pi = treeMap.get(new PageInterval(new Interval(0, 0), currentPage - maxPages + 1));
//				// Correct so that the time interval is always from 0 to MAX_LONG in the tree
//				if (pi != null) {
//					treeMap.remove(pi);
//					pi.getInterval().setLower(0);
//					treeMap.put(pi, pi);
//				}
//			}
//		}
//		if(stmHasChanged) {
//			writeSTM();
//		}
//		return success;
//	}

//	public synchronized List<SensorReading> retrieve(long sensorID, long fromTimestamp, long toTimestamp) {
//		TreeMap<PageInterval, PageInterval> treeMap = sensorTreeMap.get(sensorID);
//		if (treeMap != null) {
//			PageInterval lower = treeMap.get(new PageInterval(new Interval(fromTimestamp, fromTimestamp), -1));
//			PageInterval upper = treeMap.get(new PageInterval(new Interval(toTimestamp, toTimestamp), -1));
//			ArrayList<SensorReading> sensorData = new ArrayList<SensorReading>();
//			for (long i = lower.getPageNumber(); i <= upper.getPageNumber(); i++) {
//				SensorStorePage stp = new SensorStorePage(dir, sensorID, i);
//				List<SensorReading> sensorDataFromPage = stp.retrieve(fromTimestamp, toTimestamp);
//				if (sensorDataFromPage != null) {
//					sensorData.addAll(sensorDataFromPage);
//				}
//			}
//			return sensorData;
//		} else {
//			return null;
//		}
//	}

//	public synchronized void markLastUploaded(long sensorID, long lastUploaded) {
//		SensorStoreConfig ssc = new SensorStoreConfig(dir, sensorID);
//		ssc.setLastUploadedTimestamp(lastUploaded);
//		ssc.store();
//	}

	public synchronized UUID getUUID() {
		return uuid;
	}

	public synchronized void newUUID() {
		uuid = UUID.randomUUID();
		storeVMConfig();
	}

	private synchronized boolean loadVMConfig() {
		Log.d(TAG, "Inside loadVMConfig");
		boolean success = true;
		Config config = null;
		
		Log.d(TAG, "Config - count = "+configDao.queryBuilder().count());
		if(configDao.queryBuilder().count() != 0){
			config = configDao.queryBuilder().unique();
			uuid = UUID.fromString(config.getUUID());
			Log.d(TAG, "Config - UUID = "+uuid);
		}else 
			success = false;
		
		return success;
	}

	private synchronized void storeVMConfig() {
		Log.d(TAG, "Inside storeVMConfig");
		Config config = null;
		
		if(configDao.queryBuilder().count() == 0){
			config = new Config(0L, uuid.toString(), Build.MANUFACTURER, Build.MODEL, "Android", Build.VERSION.RELEASE, System.currentTimeMillis()); 
			configDao.insert(config);
			Log.d(TAG, "Config DB created");
		} else {
			Log.d(TAG, "Config DB exists");
		}
		
	}


	public synchronized boolean storeSensor(SensorDataImpl sensorData) {
		Log.d(TAG, "Inside storeSensor ");
		
		if(sensorData == null) {
			Log.e(TAG, "SensorData is null. please check it");
			return false;
		}else {
			Log.d(TAG, "SensorData Type = (Type = "+sensorData.getType()+")"); //, Timestamp = "+sensorData.getTimeStamp()+", Volatility = "+sensorData.getVolatility());
		}
		
		
		switch(sensorData.getType()) {
		case Constants.SENSOR_ACCELEROMETER:
			
			AccelData accelData = (AccelData) sensorData;
			Log.d(TAG, "ACCEL_DATA table count = "+accDao.count());
			Log.d(TAG, "Inside Switch, AccelData Type = (Type = "+accelData.getType()+", Timestamp = "+accelData.getTimeStamp()+", Volatility = "+accelData.getVolatility());
			Log.d(TAG, "Inside Switch, AccelData Type = (X = "+accelData.getX()+", Y = "+accelData.getY()+", Z = "+accelData.getZ());
			
			accDao.insert(accelData);
			return true;
			
		case Constants.SENSOR_BATTERY:
			return true;
			
		case Constants.SENSOR_DEVICE:
			return true;
			
		case Constants.SENSOR_LOCATION:
			LocationData locData = (LocationData) sensorData;
			Log.d(TAG, "LOCATION_DATA table count = "+locDao.count());
			Log.d(TAG, "Inside Switch, LocationData Type = (Type = "+locData.getType()+", Timestamp = "+locData.getTimeStamp()+", Volatility = "+locData.getVolatility());
			Log.d(TAG, "Inside Switch, LocationData Type = (Latitude = "+locData.getLatitude()+", Longitude = "+locData.getLongitude()+", ALtitude = "+locData.getAltitude());
			
			locDao.insert(locData);
			return true;
			
		case Constants.SENSOR_BLEBEACON:
			return true;
			
		case Constants.SENSOR_CONNECTIVITY:
			return true;
		case Constants.SENSOR_GYROSCOPE:
			return true;
		case Constants.SENSOR_HUMIDITY:
			return true;
		case Constants.SENSOR_LIGHT:
			return true;
			
		case Constants.SENSOR_MAGNETIC:
			return true;
		case Constants.SENSOR_NOISE:
			return true;
		case Constants.SENSOR_PRESSURE:
			return true;
		case Constants.SENSOR_PROXIMITY:
			return true;
		case Constants.SENSOR_TEMPERATURE:
			return true;
			
		}
		return false;
	}

//	public synchronized boolean storeSensor(long sensorID, SensorReading sensorReading) {
//		if (sensorData != null) {
//			boolean stmHasChanged = false;
//			boolean success = true;
//			SensorStoreConfig ssc = new SensorStoreConfig(dir, sensorID);
//
//			TreeMap<PageInterval, PageInterval> treeMap = sensorTreeMap.get(sensorID);
//			if (treeMap == null) {
//				treeMap = new TreeMap<PageInterval, PageInterval>();
//				// Open the initial interval
//				PageInterval piFirst = new PageInterval(new Interval(0, Long.MAX_VALUE), 0);
//				treeMap.put(piFirst, piFirst);
//				sensorTreeMap.put(sensorID, treeMap);
//				stmHasChanged = true;
//			}
//
//			// Reject non monotonically increasing timestamps
//			if (ssc.getLastWrittenTimestamp() - sensorData.getRecordTime() >= 0) {
//				return false;
//			}
//
//			// Add new page if the last one is full
//			if (ssc.getEntryNumber() == MAX_ENTRIES) {
//				ssc.setCurrentPage(ssc.getCurrentPage() + 1);
//				ssc.setEntryNumber(0);
//
//				// Close the last interval
//				PageInterval piLast = treeMap.get(new PageInterval(new Interval(0, 0), ssc.getCurrentPage() - 1));
//				treeMap.remove(piLast);
//				piLast.getInterval().setUpper(ssc.getLastWrittenTimestamp());
//				treeMap.put(piLast, piLast);
//				// Open the next interval
//				PageInterval piNext = new PageInterval(new Interval(ssc.getLastWrittenTimestamp() + 1, Long.MAX_VALUE), ssc.getCurrentPage());
//				treeMap.put(piNext, piNext);
//
//				// Remove old pages
//				removeOldPages(sensorID, ssc.getCurrentPage(), MAX_PAGES);
//				stmHasChanged = true;
//			}
//
//			SensorStorePage ssp = new SensorStorePage(dir, ssc.getSensorID(), ssc.getCurrentPage());
//			ssp.store(sensorData, ssc.getEntryNumber());
//
//			ssc.setEntryNumber(ssc.getEntryNumber() + 1);
//
//			ssc.setLastWrittenTimestamp(sensorData.getRecordTime());
//			ssc.store();
//			
//			if (stmHasChanged) {
//				writeSTM();
//			}
//			return success;
//		}
//		return false;
//	}

//	public long getLastUploadedTimestamp(long sensorID) {
//		SensorStoreConfig ssc = new SensorStoreConfig(dir, sensorID);
//		return ssc.getLastUploadedTimestamp();
//	}
//
//	public void setLastUploadedTimestamp(long sensorID, long timestamp) {
//		SensorStoreConfig ssc = new SensorStoreConfig(dir, sensorID);
//		ssc.setLastUploadedTimestamp(timestamp);
//		ssc.store();
//	}
//
//	public void deleteSensor(long sensorID) {
//		SensorStoreConfig ssc = new SensorStoreConfig(dir, sensorID);
//		removeOldPages(sensorID, ssc.getCurrentPage(), 0);
//		ssc.delete();
//	}
//
//	public long[] getSensorStorageSize(long sensorID) {
//		long[] size = { 0, 0 };
//		SensorStoreConfig ssc = new SensorStoreConfig(dir, sensorID);
//		for (int i = 0; i < MAX_PAGES; i++) {
//			SensorStorePage ssp = new SensorStorePage(dir, sensorID, ssc.getCurrentPage() - i);
//			size[0] += ssp.getStoreSize();
//			size[1] += ssp.getIndexSize();
//		}
//		return size;
//	}
}
