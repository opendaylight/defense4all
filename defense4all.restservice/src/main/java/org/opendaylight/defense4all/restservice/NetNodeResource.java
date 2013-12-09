package org.opendaylight.defense4all.restservice;

import java.util.Hashtable;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opendaylight.defense4all.core.DFHolder;
import org.opendaylight.defense4all.core.NetNode;
import org.opendaylight.defense4all.framework.core.ExceptionControlApp;
import org.opendaylight.defense4all.framework.core.Repo;

public class NetNodeResource {
	private static Log log = LogFactory.getLog(NetNodeResource.class);
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String netNodeLabel;
	
	public NetNodeResource(UriInfo uriInfo, Request request, String netNodeLabel) {
		this.uriInfo = uriInfo;	this.request = request;	this.netNodeLabel = netNodeLabel;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public NetNode getNetNode() {

		try {
			log.debug("In getnetNode. NetNode label is " + netNodeLabel);
			Repo<String> netNodesRepo = DFHolder.get().netNodesRepo;
			Hashtable<String,Object> netNodeRow = netNodesRepo.getRow(netNodeLabel);
			return new NetNode(netNodeRow);
		} catch (ExceptionControlApp e) {
			log.error("Failed to retrieve pn " + netNodeLabel, e);
			return null;
		}
	}
	
	@DELETE
	public void deleteNetNode() {
		
		try {
			log.debug("DeleteNetNode: invoked");
			DFHolder.get().getMgmtPoint().removeNetNode(netNodeLabel);
		} catch (ExceptionControlApp e) {{/* Ignore. Already logged in DFMgmtPoint. */}}
	}
}
