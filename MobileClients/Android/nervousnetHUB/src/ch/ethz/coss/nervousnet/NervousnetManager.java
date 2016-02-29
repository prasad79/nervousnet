package ch.ethz.coss.nervousnet;

public class NervousnetManager {
	
	private static NervousnetManager _instance = null;
	
	
	private NervousnetManager(){
		
	}
	
	public static NervousnetManager getInstance(){
		if(_instance == null) {
			_instance = new NervousnetManager();
		}
		
		return _instance;
	}

}
