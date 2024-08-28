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
package com.ericsson.oss.cm.services.test.cases;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.oss.cm.services.test.data.SyncNodeServiceTestDataProvider;
import com.ericsson.oss.cm.services.test.data.SynchNodeTestData;
import com.ericsson.oss.cm.services.test.netsim.TMONetsimResultsHolder;
import com.ericsson.oss.cm.services.test.netsim.notifications.NotificationMORequest;
import com.ericsson.oss.cm.services.utilities.MoComparatorUtility;
import com.ericsson.oss.cm.services.utilities.RestUtility;
import com.ericsson.oss.services.dps.TestManagedObject;


public class CmTopologySyncNotificationsAVC extends TorTestCaseHelper implements TestCase {
    private static final Logger logger = Logger.getLogger(CmTopologySyncNotificationsAVC.class);
    private List<String> nodesAddedList = new ArrayList<String>();
        
    @TestId(id = "TORF-9317-2", title = "Deletion of all nodes added during test case execution")
    @AfterMethod
    public void DeletionOfAllAddedNodes() {
        logger.info("In DeletionOfAllAddedNodes method");

        for (String nodeName : nodesAddedList) {
            assertTrue("Failed to delete Node !!!", SyncNodeServiceTestDataProvider.deleteSingleNodeCLI(nodeName));
        }

    }

    //work in progress here. As need clever/simply way to verify the simple AVCs
    @DataDriven(name = "syncNodeDataProvider1NodeAVCNotifications")
    @TestId(id = "TORF-9317-2", title = "Verification of successful consumption of 1 AVC String notification change")
    @Test(groups = { "acceptance" })
    public void Successful1Node1AVCStringNotifications(@Input("Target1NodeAVCNotificationsData") SynchNodeTestData synchNodePojo) {
        assertNotNull("There is No Test Case Data Object, cannot proceed with the TestCase", synchNodePojo);
        nodesAddedList = synchNodePojo.getNodeNames();

        String targetNodeName = synchNodePojo.getNodeNames().get(0);
        setAdditionalResultInfo("Target Node= " + targetNodeName);

        setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
        assertTrue(SyncNodeServiceTestDataProvider.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));

        setTestStep("Synchronize Action for Node " + targetNodeName);
        SyncNodeServiceTestDataProvider.synchNode(targetNodeName);

        setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
        TestManagedObject enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
        TestManagedObject netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(targetNodeName).getTestManagedObject();
        assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));    

        SyncNodeServiceTestDataProvider.createMoNotification(targetNodeName, synchNodePojo);
        sleep(5);
        
        List<NotificationMORequest> list = synchNodePojo.getNotificationRequestData();
        NotificationMORequest moRequest= list.get(0);
        String moFDN = moRequest.getFdn();
        String expectedAttributeValue = "userLabelAVC1";
        String attributeName = "userLabel";
        SyncNodeServiceTestDataProvider.sendAVCNotification(targetNodeName, synchNodePojo, moFDN ,attributeName, expectedAttributeValue);
        sleep(3);        
  
        
        String actualAttributeValueAndType = RestUtility.getMoAttributeNameAndType("MeContext=" + targetNodeName + "," + moFDN, attributeName);
        String actualAttributeValue = actualAttributeValueAndType.substring(0, actualAttributeValueAndType.indexOf(":::"));
        logger.info("expected = " + expectedAttributeValue + ", actual = "  + actualAttributeValue);
        assertEquals(expectedAttributeValue,actualAttributeValue);
        
        SyncNodeServiceTestDataProvider.deleteMoNotification(targetNodeName, synchNodePojo);
        sleep(5);

    }
    

    @DataDriven(name = "syncNodeDataProvider1NodeAVCNotifications")
    @TestId(id = "TORF-9317-2", title = "Verification of successful consumption of 1 AVC Long notification change")
    @Test(groups = { "acceptance" })
    public void Successful1Node1AVCLongNotifications(@Input("Target1NodeAVCNotificationsData") SynchNodeTestData synchNodePojo) {
        assertNotNull("There is No Test Case Data Object, cannot proceed with the TestCase", synchNodePojo);
        nodesAddedList = synchNodePojo.getNodeNames();

        String targetNodeName = synchNodePojo.getNodeNames().get(0);
        setAdditionalResultInfo("Target Node= " + targetNodeName);

        setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
        assertTrue(SyncNodeServiceTestDataProvider.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));

        setTestStep("Synchronize Action for Node " + targetNodeName);
        SyncNodeServiceTestDataProvider.synchNode(targetNodeName);
        
        SyncNodeServiceTestDataProvider.createMoNotification(targetNodeName, synchNodePojo);
        sleep(5);

        List<NotificationMORequest> list = synchNodePojo.getNotificationRequestData();
        NotificationMORequest moRequest= list.get(0);
        String moFDN = moRequest.getFdn();
        long expectedAttributeValue = 50L;
        String attributeName = "heartbeatInterval";
        
        SyncNodeServiceTestDataProvider.sendAVCNotification(targetNodeName, synchNodePojo, moFDN ,attributeName, expectedAttributeValue);
        sleep(3);        
        
        String actualAttributeValueAndType = RestUtility.getMoAttributeNameAndType("MeContext=" + targetNodeName + "," + moFDN, attributeName);
        String actualAttributeValue = actualAttributeValueAndType.substring(0, actualAttributeValueAndType.indexOf(":::"));
        logger.info("expected = " + expectedAttributeValue + ", actual = "  + actualAttributeValue);
        assertEquals(expectedAttributeValue,Long.parseLong(actualAttributeValue));
        
        SyncNodeServiceTestDataProvider.deleteMoNotification(targetNodeName, synchNodePojo);
        sleep(5);
  
    }
    
    
    @DataDriven(name = "syncNodeDataProvider1Node1AVCNotifications")
    @TestId(id = "TORF-9317-2", title = "Verification of successful consumption of 1 AVC Boolean notification change")
    @Test(groups = { "acceptance" })
    public void Successful1Node1AVCBooleanNotifications(@Input("Target1NodeAVCNotificationsData") SynchNodeTestData synchNodePojo) {
        assertNotNull("There is No Test Case Data Object, cannot proceed with the TestCase", synchNodePojo);
        nodesAddedList = synchNodePojo.getNodeNames();

        String targetNodeName = synchNodePojo.getNodeNames().get(0);
        setAdditionalResultInfo("Target Node= " + targetNodeName);
        
        setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
        assertTrue(SyncNodeServiceTestDataProvider.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));
        
        setTestStep("Synchronize Action for Node " + targetNodeName);
        SyncNodeServiceTestDataProvider.synchNode(targetNodeName);
        
        SyncNodeServiceTestDataProvider.createMoNotification(targetNodeName, synchNodePojo);
        sleep(5);

        List<NotificationMORequest> list = synchNodePojo.getNotificationRequestData();
        NotificationMORequest moRequest= list.get(0);
        String moFDN = moRequest.getFdn();
        boolean expectedAttributeValue = false;
        String attributeName = "heartbeatStatus";
        SyncNodeServiceTestDataProvider.sendAVCNotification(targetNodeName, synchNodePojo, moFDN ,attributeName, expectedAttributeValue);
        sleep(3);        
  
        
        String actualAttributeValueAndType = RestUtility.getMoAttributeNameAndType("MeContext=" + targetNodeName + "," + moFDN, attributeName);
        String actualAttributeValue = actualAttributeValueAndType.substring(0, actualAttributeValueAndType.indexOf(":::"));
        logger.info("expected = " + expectedAttributeValue + ", actual = "  + actualAttributeValue);
        assertEquals(expectedAttributeValue,Boolean.parseBoolean(actualAttributeValue));
  
        SyncNodeServiceTestDataProvider.deleteMoNotification(targetNodeName, synchNodePojo);
        sleep(5);

    }
}
