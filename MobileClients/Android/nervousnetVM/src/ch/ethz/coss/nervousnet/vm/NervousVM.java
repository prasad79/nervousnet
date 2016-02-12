package ch.ethz.coss.nervousnet.vm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import ch.ethz.coss.nervousnet.vm.model.Config;
import ch.ethz.coss.nervousnet.vm.model.ConfigDao;
import ch.ethz.coss.nervousnet.vm.model.DaoMaster;
import ch.ethz.coss.nervousnet.vm.model.DaoSession;
import ch.ethz.coss.nervousnet.vm.model.SensorReading;
import ch.ethz.coss.nervousnet.vm.model.DaoMaster.DevOpenHelper;


public class NervousVM {

	private static NervousVM nervousVM;
	private UUID uuid;
	private Context context;
	DaoMaster daoMaster;
	DaoSession daoSession;
	SQLiteDatabase sqlDB;
	
	public static synchronized  NervousVM getInstance(Context context) {
		if (nervousVM == null) {
			nervousVM = new NervousVM(context);
		}
		return nervousVM;
	}

	public NervousVM(Context context) {
	    boolean hasVMConfig = loadVMConfig();
		if (!hasVMConfig) {
			uuid = UUID.randomUUID();
			DevOpenHelper doh = new DaoMaster.DevOpenHelper(context, "nn-db", null);
			 sqlDB = doh.getWritableDatabase();
			 daoMaster = new DaoMaster(sqlDB);
			 daoSession = daoMaster.newSession();
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
		boolean success = true;
		
		ConfigDao configDao = daoSession.getConfigDao();
		Config config = null;
		
		if(configDao.queryBuilder().count() != 0){
			config = configDao.queryBuilder().unique();
			uuid = UUID.fromString(config.getUUID());
		}else 
			success = false;
		
		return success;
	}

	private synchronized void storeVMConfig() {
		
		
		
		ConfigDao configDao = daoSession.getConfigDao();
		Config config = null;
		
		if(configDao.queryBuilder().count() == 0){
			config = new Config(0L, uuid.toString(), Build.MANUFACTURER, Build.MODEL, "Android", Build.VERSION.RELEASE, new Date()); 
			configDao.insert(config);
		}
	
		
		
	}



//	public synchronized boolean storeSensor(long sensorID, SensorData sensorData) {
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
