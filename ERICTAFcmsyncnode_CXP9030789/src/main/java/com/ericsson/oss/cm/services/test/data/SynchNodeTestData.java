/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.cm.services.test.data;

import java.util.*;

import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement;
import com.ericsson.oss.cm.services.test.netsim.notifications.*;

public class SynchNodeTestData {

    Map<String, NetworkElement> netsimNodeMap;

    List<String> netsimSimulationNames = new ArrayList<String>();
    
    List<NotificationMORequest> notificationMORequest;

    /**
     * @param netsimSimulationNames
     *            the netsimSimulationNames to set
     */
    public void setNetsimSimulationNames(List<String> netsimSimulationNames) {
        this.netsimSimulationNames = netsimSimulationNames;
    }
    
    public List<String> getNetsimSimulationNames() {
        return netsimSimulationNames;
    }

    public SynchNodeTestData(Map<String, NetworkElement> netsimNodeMap) {
        this.netsimNodeMap = netsimNodeMap;

    }

    public Map<String, NetworkElement> getNetsimNodeMap() {
        return netsimNodeMap;

    }

    public List<String> getNodeNames() {
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, NetworkElement> entry : netsimNodeMap.entrySet()) {
            list.add(entry.getKey());
        }

        return list;
    }
    
    public int getNumberOfNodes(){
        return netsimNodeMap.size();
    }
    
    public NetworkElement getNetsimSimulationNE(final String nodeName){
        return netsimNodeMap.get(nodeName);
    }


    public void setNotificationRequestData(List<NotificationMORequest> notificationRequest) {
        this.notificationMORequest = notificationRequest;
        
    }
    
    public  List<NotificationMORequest> getNotificationRequestData() {
        return notificationMORequest;
        
    }
     

}