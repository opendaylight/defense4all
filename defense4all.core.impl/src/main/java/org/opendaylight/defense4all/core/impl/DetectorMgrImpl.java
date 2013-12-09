/**
 * Copyright (c) <2013> <Radware Ltd.> and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * @author Konstantin Pozdeev 
 * @version 0.1
 */

package org.opendaylight.defense4all.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.opendaylight.defense4all.core.DFAppRoot;
import org.opendaylight.defense4all.core.DFDetector;
import org.opendaylight.defense4all.core.Detection;
import org.opendaylight.defense4all.core.DetectorMgr;
import org.opendaylight.defense4all.core.Detector;
import org.opendaylight.defense4all.core.DetectorInfo;
import org.opendaylight.defense4all.core.PN;
import org.opendaylight.defense4all.core.StatReport;
import org.opendaylight.defense4all.framework.core.ExceptionControlApp;
import org.opendaylight.defense4all.framework.core.HealthTracker;
import org.opendaylight.defense4all.framework.core.FrameworkMain.ResetLevel;



public class DetectorMgrImpl extends DFAppCoreModule  implements DetectorMgr {

	protected Detector detector0 = null;
	protected Detector detector1 = null;
	protected Detector detector2 = null;
	protected Detector detector3 = null;
	protected Detector detector4 = null;
	protected Detector detector5 = null;
	public Hashtable<String, DFDetector> detectors = null;

	public void setdetector0(Detector detector0) {this.detector0 = detector0;}
	public void setdetector1(Detector detector1) {this.detector1 = detector1;}
	public void setdetector2(Detector detector2) {this.detector2 = detector2;}
	public void setdetector3(Detector detector3) {this.detector3 = detector3;}
	public void setdetector4(Detector detector4) {this.detector4 = detector4;}
	public void setdetector5(Detector detector5) {this.detector5 = detector5;}

	static private Properties defaultProperties = null; 


	public DetectorMgrImpl() {
		super();
		detectors = new Hashtable<String,DFDetector>();
	}

	/** Post-constructor initialization	 */
	public void init() throws ExceptionControlApp {
		
		super.init();	

		fr.logRecord(DFAppRoot.FR_DF_OPERATIONAL,"DetectorMgr is starting");
		
		// Create and registry all detectors from repo
		Hashtable<String, Hashtable<String, Object>> detectorTable;

		detectorTable = dfAppRoot.detectorsRepo.getTable();

		if ( detectorTable == null) {
			log.error("Failed to get detectorsRepo table in DetectorMgr. " );
			fr.logRecord(DFAppRoot.FR_DF_FAILURE,"DetectorMgr failed to properly start");
			fMain.getHealthTracker().reportHealthIssue(HealthTracker.MODERATE_HEALTH_ISSUE);			
			throw new ExceptionControlApp("Failed to get detectorsRepo table in DetectorMgr. ");
		}

		Iterator<Map.Entry<String,Hashtable<String,Object>>> iter = detectorTable.entrySet().iterator();
		Map.Entry<String,Hashtable<String,Object>> entry; DetectorInfo detectorInfo = null; Detector detector;		
		while(iter.hasNext()) {		
			try {
				entry = iter.next();
				detectorInfo = new DetectorInfo(entry.getValue());
				if(!detectorInfo.getOfBasedDetector()) continue;

				detector = instrumentDetector(detectorInfo.getLabel() );
				if (detector == null || ! (detector instanceof DFDetector)) continue;

				detector.fromRow(entry.getValue());	
				detector.init();
				addDetector ( detector );
			} catch (Throwable e) {
				String msg = detectorInfo != null ? " Detector label "+detectorInfo.label:"";
				log.error("Failed to inflate detector from detectorsRepo row. " + msg );
				fr.logRecord(DFAppRoot.FR_DF_FAILURE,"Failed initilizing detector " + detectorInfo.label);
				fMain.getHealthTracker().reportHealthIssue(HealthTracker.MODERATE_HEALTH_ISSUE);			
				continue;
			}
		}

		if(detectors.size() > 0 ) return; // Not first time init - no need to take from properties file

		/* First time repo initialization - add Detector if one set through properties file. Next time it will be in repo. */
		for (Detector dt : instrumentDetectors() ) {
			if (dt == null ||  !(dt instanceof DFDetector)) continue;
			// read properties file
			try {
				Hashtable<String, Object> detectorRow = getProperties ( dt);
				dt.fromRow(detectorRow);		
				dt.init();
				addDetector ( dt );
			} catch (Exception e) {
				String msg = detectorInfo != null ? " Detector label "+detectorInfo.label:"";
				log.error("Failed to inflate detector from properties file. " + msg , e);
				fr.logRecord(DFAppRoot.FR_DF_FAILURE,"Failed initilizing from properties file detector "+ detectorInfo.label);
				fMain.getHealthTracker().reportHealthIssue(HealthTracker.MODERATE_HEALTH_ISSUE);			
				continue;
			}
		}
	}

	/** Pre-shutdown cleanup */
	public void finit() {
		fr.logRecord(DFAppRoot.FR_DF_OPERATIONAL,"DetectorMgr is stopping");
		super.finit();

		// loop over all detectors in detectors repo 
		for ( Detector dt : detectors.values() ) {
			dt.finit();
		}
	}

	/** Reset 
	 * @throws ExceptionControlApp */
	public void reset(ResetLevel resetLevel) throws ExceptionControlApp {
		fr.logRecord(DFAppRoot.FR_DF_OPERATIONAL,"DetectorMgr is resetting to level " + resetLevel);
		super.reset(resetLevel);
	}

	/**
	 * Instrument an Detector constructed from spring context 
	 * by the label parameters
	 * 
	 */
	protected Detector instrumentDetector (String label) {
		for (Detector detector:instrumentDetectors()  ) {
			if ( detector != null) {
				if ( detector.getDetectorInfo().getLabel().equals(label) )
					return detector;
			}
		}
		return null;
	}
	protected Detector[] instrumentDetectors() {
		Detector allDetectors[] = {detector0, detector1, detector2, detector3, detector4, detector5 };
		return allDetectors;
	}


	/**
	 * Read properties file for pre-created detector
	 * @param param_name param description
	 * @return return description
	 * @throws ExceptionControlApp 
	 * @throws exception_type circumstances description 
	 */

	private  Hashtable<String, Object>  getProperties(Detector detector) throws ExceptionControlApp {

		// Read properties file with default attributes
		if ( defaultProperties == null ) { 
			defaultProperties=new Properties();
			InputStream is=DFMgmtPointImpl.class.getClassLoader().getResourceAsStream("detectors.properties");
			try {
				defaultProperties.load(is);
			} catch(IOException ioExc) {
				log.error("Failed to load config properties file detectors.properties", ioExc);
				throw new ExceptionControlApp("Failed to load config properties file detectors.properties", ioExc);
			} 
		}

		// Get default map of attributes
		Hashtable<String, Object> detectorRow;
		try {
			detectorRow = detector.toRow();
			// get list of attributes and replace values from properties file
			for ( String key : detectorRow.keySet() ) {
				String propertyName = "Detector."+detector.getDetectorInfo().getLabel()+"."+key;
				if ( defaultProperties.containsKey(propertyName) && defaultProperties.get(propertyName) != null) {
					detectorRow.put( key , defaultProperties.get(propertyName));
				}
			}
		} catch (Throwable e) {
			log.error("Failed to process config properties file detectors.properties", e);
			throw new ExceptionControlApp("Failed to process config properties file detectors.properties", e);		
		}
		return detectorRow;

	}

	/**
	 * Add an Detector constructed from parameters on init
	 * @param param_name param description
	 * @return return description
	 * @throws ExceptionControlApp 
	 * @throws exception_type circumstances description 
	 */
	public void addDetector(Detector detector) throws ExceptionControlApp {
		
		String detectorLabel = detector.getDetectorInfo().getLabel();

		// insert or update repository row
		try {
			dfAppRootFullImpl.detectorsRepo.setRow(detectorLabel,detector.toRow());
		} catch (Throwable e) {
			log.error("Failed to persist detector in detectorsRepo. Detector label:  "+detectorLabel, e );
			fMain.getHealthTracker().reportHealthIssue(HealthTracker.MINOR_HEALTH_ISSUE);			
			throw new ExceptionControlApp("Failed to persist detector in detectorsRepo. Detector label:  "+detectorLabel, e);
		} 	

		if (detector.getDetectorInfo().getOfBasedDetector() && detector instanceof DFDetector )
			detectors.put(detector.getDetectorInfo().getLabel(), (DFDetector)detector);
	}

	/**
	 * Return an initialized Detector from hash 
	 * @param param_name param description
	 * @return return description
	 * @throws exception_type circumstances description 
	 */
	public DFDetector getDetector(String label) {
		return detectors.get(label);
	}

	/**
	 * Delete an initialized Detector from hash 
	 * @param param_name param description
	 * @return return description
	 * @throws ExceptionControlApp 
	 * @throws exception_type circumstances description 
	 */
	public void removeDetector(String detectorLabel) throws ExceptionControlApp {
		
		Detector detector = detectors.get(detectorLabel);
		if ( detector != null ) {
			detector.finit();
			detectors.remove(detector);
		}
		// remove from repository also
		try {
			dfAppRootFullImpl.detectorsRepo.deleteRow(detectorLabel);
		} catch (ExceptionControlApp e) {
			log.error("Failed to delete detector from detectorsRepo. Detector label:  "+detectorLabel, e );
			fMain.getHealthTracker().reportHealthIssue(HealthTracker.MINOR_HEALTH_ISSUE);			
			throw new ExceptionControlApp("Failed to persist detector in detectorsRepo. Detector label:  "+detectorLabel, e);
		}
	}

	/**
	 * Loop over registered in PN detectors and pass stat report
	 * @param param_name param description
	 * @return return description
	 * @throws exception_type circumstances description 
	 */
	public void handleStatReport(Hashtable<String,Object> pnRow, StatReport statReport) {
		try {
			String label = (String) pnRow.get(PN.DETECTOR_LABEL);
			if ( detectors.containsKey(label)) {
				if ( detectors.get(label).getDetectorInfo().getOfBasedDetector()) {
					detectors.get(label).handleStatReport( statReport );
				}
			}
		} catch (Throwable e) {
			log.error("Excepted handling statReport.", e);
			fMain.getHealthTracker().reportHealthIssue(HealthTracker.MINOR_HEALTH_ISSUE);
		}
	}

	/**
	 * Notify all relevant detectors 
	 * @param param_name param description
	 * @return return description
	 * @throws ExceptionControlApp 
	 * @throws exception_type circumstances description 
	 */
	public void notifyEndDetection(String detectionKey) {

		String detectorLabel;
		try {
			detectorLabel = (String) dfAppRootFullImpl.detectionsRepo.getCellValue(detectionKey, Detection.DETECTOR);
		} catch (ExceptionControlApp e) {
			log.error("Failed to get detection from detectionsRepo. Detection key:  "+detectionKey, e );
			fMain.getHealthTracker().reportHealthIssue(HealthTracker.MINOR_HEALTH_ISSUE);			
			return;
		}

		if(detectorLabel == null) 
			return;	// Just in case detections repo is out of sync with attack detections

		DFDetector detector = getDetector(detectorLabel);
		try {
			if ( detector != null && detector.getDetectorInfo().getOfBasedDetector() )
				detector.notifyEndDetection(detectionKey);
		} catch (Throwable e) {
			log.error("Failed to notify datector about end detection Detection key:  "+detectionKey, e );
			return;
		}
	}

	public void cleanup() {	
		for (DFDetector detector : detectors.values() ) {
			try {
				detector.cleanup();
			} catch (Throwable e) {/* Ignore */}
		}
	}

	@Override
	protected void actionSwitcher(int actionCode, Object param) {
		switch(actionCode) {
		default:
			break;
		}              
	}
}
