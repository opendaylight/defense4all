/**
 * Copyright (c) <2013> <Radware Ltd.> and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * @author Gera Goft 
 * @version 0.1
 */


package org.opendaylight.controlapps.defense4all.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.opendaylight.controlapps.framework.core.PropertiesSerializer;
import org.opendaylight.controlapps.framework.core.RepoCD;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;


public class OFC {

	public static final String HOSTNAME = "hostname";
	public static final String IP_ADDR_STRING = "ip_addr_string";
	public static final String PORT = "port";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String FOR_STATS_COLLECTION = "for_stats_collection";
	public static final String FOR_DIVERSION = "for_diversion";
	public static final String PROPS = "props";
	
	public String hostname; 			public String ipAddrString;  	public int port;
	public String username; 			public String password;
	public boolean forStatsCollection;	public boolean forDiversion;
	public Properties props;
	
	protected static ArrayList<RepoCD> mOFCsRepoCDs = null;
	
	/* ### Description ###
	 * @param param_name 
	 */
	public OFC() {
		hostname = null; ipAddrString = null; port = -1; username = password = ""; 
		forStatsCollection = forDiversion = false; props = new Properties();
	}
	
	/* ### Description ###
	 * @param param_name 
	 */
	public OFC(String hostname, String ipAddrString, int port, String username, String password, 
			boolean forStatsCollection, boolean forDiversion, Properties props) {
		this.hostname = hostname; this.ipAddrString = ipAddrString; this.port = port; 
		this.username = username; this.password = password; this.forStatsCollection = forStatsCollection; 
		this.forDiversion = forDiversion; 
		this.props = (props == null ? new Properties() : props);
	}
	
	public OFC(Hashtable<String, Object> ofcRow) {
		this();
		hostname = (String) ofcRow.get(HOSTNAME);
		ipAddrString = (String) ofcRow.get(IP_ADDR_STRING);
		port = (Integer) ofcRow.get(PORT);
		username = (String) ofcRow.get(USERNAME);
		password = (String) ofcRow.get(PASSWORD);
		forStatsCollection = (Boolean) ofcRow.get(FOR_STATS_COLLECTION);
		forDiversion = (Boolean) ofcRow.get(FOR_DIVERSION);
		props = (Properties) ofcRow.get(PROPS);
	}
	
	public String getHostname() {return hostname;}
	public void setHostname(String hostname) {this.hostname = hostname;}
	
	public String getIpAddrString() {return ipAddrString;}
	public void setIpAddrString(String ipAddrString) {this.ipAddrString = ipAddrString;}
	
	public int getPort() {return port;}
	public void setPort(int port) {this.port = port;}
	
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}
	
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	
	public boolean getForStatsCollection() {return forStatsCollection;}
	public void setForStatsCollection(boolean forStatsCollection) {this.forStatsCollection = forStatsCollection;}
	
	public boolean getForDiversion() {return forDiversion;}
	public void setForDiversion(boolean forDiversion) {this.forDiversion = forDiversion;}
	
	public Properties getProps() {return props;}
	public void setProps(Properties props) {this.props = props;}

	public Hashtable<String, Object> toRow() {
		
		/* Change any null value to empty, otherwise Hashtable.put() will throw an exception */
		if(hostname == null) hostname = "";
		if(ipAddrString == null) ipAddrString = "";
		if(username == null) username = "";
		if(props == null) props = new Properties();
		
		Hashtable<String, Object> row = new Hashtable<String, Object>();
		row.put(HOSTNAME, hostname);
		row.put(IP_ADDR_STRING, ipAddrString);
		row.put(PORT, port);
		row.put(USERNAME, username);
		row.put(PASSWORD, password);
		row.put(FOR_STATS_COLLECTION, forStatsCollection);
		row.put(FOR_DIVERSION, forDiversion);
		row.put(PROPS, props);
		return row;
	}
	
	public static List<RepoCD> getOFCRCDs() {

		if(mOFCsRepoCDs == null) {
			RepoCD rcd;
			mOFCsRepoCDs = new ArrayList<RepoCD>();			
			rcd = new RepoCD(HOSTNAME, StringSerializer.get(), null);	mOFCsRepoCDs.add(rcd);
			rcd = new RepoCD(IP_ADDR_STRING, StringSerializer.get(), null);	mOFCsRepoCDs.add(rcd);
			rcd = new RepoCD(PORT, IntegerSerializer.get(), null);	mOFCsRepoCDs.add(rcd);
			rcd = new RepoCD(USERNAME, StringSerializer.get(), null);	mOFCsRepoCDs.add(rcd);
			rcd = new RepoCD(PASSWORD, StringSerializer.get(), null);	mOFCsRepoCDs.add(rcd);
			rcd = new RepoCD(FOR_STATS_COLLECTION, BooleanSerializer.get(), null); mOFCsRepoCDs.add(rcd);
			rcd = new RepoCD(FOR_DIVERSION, BooleanSerializer.get(), null);	mOFCsRepoCDs.add(rcd);
			rcd = new RepoCD(PROPS, PropertiesSerializer.get(), null); mOFCsRepoCDs.add(rcd);
		}		
		return mOFCsRepoCDs;
	}
}
