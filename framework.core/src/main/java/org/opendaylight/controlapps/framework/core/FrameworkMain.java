/**
 * Copyright (c) <2013> <Radware Ltd.> and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * @author Gera Goft 
 * @version 0.1
 */


package org.opendaylight.controlapps.framework.core;

import java.util.Properties;

public interface FrameworkMain {
	
	public enum ResetLevel {
		
		soft, // Reset easily reconstructible dynamic state (e.g., latest traffic statistics, network topology)
		dynamic, // Reset all dynamic state portions, including the not easily reconstructible (e.g., traffic baselines)
		factory;  // Full reset of both dynamic state as well as user configurations
		
		public static ResetLevel valueOf(String s, ResetLevel defaultLevel) {
			
			ResetLevel returnValue;
			try {
				returnValue = ResetLevel.valueOf(s);
			} catch (Exception e) {
				returnValue = defaultLevel;
			}
			return returnValue;
		}
	}
	
	public Properties getConfigProperties();
	public RepoFactory getRepoFactory();
	public ClusterMgr getClusterMgr();
	public PeerCommunicator getPeerCommunicator();
	public FrameworkMgmtPoint getFrameworkMgmtPoint();
	public AppRoot getAppRoot();
	public MyLogger getMyLogger();
}
