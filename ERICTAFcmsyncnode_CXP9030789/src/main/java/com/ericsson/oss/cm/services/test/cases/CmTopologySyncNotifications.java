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
import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement;
import com.ericsson.oss.cm.services.test.data.SyncNodeServiceTestDataProvider;
import com.ericsson.oss.cm.services.test.data.SynchNodeTestData;
import com.ericsson.oss.cm.services.test.netsim.NetsimHelper;
import com.ericsson.oss.cm.services.test.netsim.TMONetsimResultsHolder;
import com.ericsson.oss.cm.services.utilities.MoComparatorUtility;
import com.ericsson.oss.cm.services.utilities.RestUtility;
import com.ericsson.oss.services.dps.TestManagedObject;


public class CmTopologySyncNotifications extends TorTestCaseHelper implements TestCase {
    private static final Logger logger = Logger.getLogger(CmTopologySyncNotifications.class);
    private List<String> nodesAddedList = new ArrayList<String>();
        
    @TestId(id = "TORF-9317-1", title = "Deletion of all nodes added during test case execution")
    @AfterMethod
    public void DeletionOfAllAddedNodes() {
        logger.info("In DeletionOfAllAddedNodes method");

        for (String nodeName : nodesAddedList) {
            assertTrue("Failed to delete Node !!!", SyncNodeServiceTestDataProvider.deleteSingleNodeCLI(nodeName));
        }

    }

    @DataDriven(name = "syncNodeDataProvider1Node1Notification")
    @TestId(id = "TORF-9317-1", title = "Verification of successful consumption of 1 notification change")
    @Test(groups = { "acceptance" })
    public void Successful1Node1Notification(@Input("TargetSync1Node1Notification") SynchNodeTestData synchNodePojo) {
        assertNotNull("There is No Test Case Data Object, cannot proceed with the TestCase", synchNodePojo);
        nodesAddedList = synchNodePojo.getNodeNames();
               
        String targetNodeName = synchNodePojo.getNodeNames().get(0);
        setAdditionalResultInfo("Target Node= " + targetNodeName);

        setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
        assertTrue(SyncNodeServiceTestDataProvider.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));
        
        setTestStep("Synchronize Action for Node " + targetNodeName);
        assertTrue("Failed to synch node", SyncNodeServiceTestDataProvider.synchNode(targetNodeName));

        setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
        TestManagedObject enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
        TestManagedObject netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(targetNodeName).getTestManagedObject();
        assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));
        
        //ensure that the following notification is not already on netsim
        SyncNodeServiceTestDataProvider.deleteMoNotification(targetNodeName, synchNodePojo);
        sleep(5);

        long beforeCreateMoGCCount = RestUtility.getGenerationCounter(targetNodeName);

        setTestStep("Sending createMO notification");
        SyncNodeServiceTestDataProvider.createMoNotification(targetNodeName, synchNodePojo);
        sleep(5);

        long afterCreateMoGCCount = RestUtility.getGenerationCounter(targetNodeName);     
        logger.info(" The GC count before createMo was " + beforeCreateMoGCCount);
        logger.info(" The GC count after  createMo was " + afterCreateMoGCCount);
        assertEquals(beforeCreateMoGCCount + (2 * synchNodePojo.getNotificationRequestData().size()), afterCreateMoGCCount);

        setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");

        SyncNodeServiceTestDataProvider.reReadNetsimAfterNotification(synchNodePojo.getNetsimSimulationNames(), synchNodePojo.getNumberOfNodes());
        netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(targetNodeName).getTestManagedObject();
        enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
        assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));

        long beforedeleteMoGCCount = RestUtility.getGenerationCounter(targetNodeName);

        setTestStep("Sending deleteMO notification");
        SyncNodeServiceTestDataProvider.deleteMoNotification(targetNodeName, synchNodePojo);
        sleep(5);

        long afterdeleteMoGCCount = RestUtility.getGenerationCounter(targetNodeName);
        logger.info(" The GC count before deleteMo was " + beforedeleteMoGCCount);
        logger.info(" The GC count after  deleteMo was " + afterdeleteMoGCCount);
        assertEquals(beforedeleteMoGCCount + (2 * synchNodePojo.getNotificationRequestData().size()), afterdeleteMoGCCount);

        setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
        SyncNodeServiceTestDataProvider.reReadNetsimAfterNotification(synchNodePojo.getNetsimSimulationNames(), synchNodePojo.getNumberOfNodes());
        netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(targetNodeName).getTestManagedObject();
        enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
        assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));

    }

    @DataDriven(name = "syncNodeDataProvider1Node5Notification")
    @TestId(id = "TORF-9317-2", title = "Verification of successful consumption of 5 notification change")
    @Test(groups = { "acceptance" })
    public void Successful1Node5Notifications(@Input("TargetSync1Node5Notification") SynchNodeTestData synchNodePojo) {
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

        long beforeCreateMoGCCount = RestUtility.getGenerationCounter( targetNodeName);

        SyncNodeServiceTestDataProvider.createMoNotification(targetNodeName, synchNodePojo);
        setTestStep("Comparison of Netsim Fdn Tree  vs DPS Fdn Tree");
        sleep(15);

        long afterCreateMoGCCount = RestUtility.getGenerationCounter(targetNodeName);
        logger.info(" The GC count before createMo was " + beforeCreateMoGCCount);
        logger.info(" The GC count after  createMo was " + afterCreateMoGCCount);
        assertEquals(beforeCreateMoGCCount + (2 * synchNodePojo.getNotificationRequestData().size()), afterCreateMoGCCount);

        SyncNodeServiceTestDataProvider.reReadNetsimAfterNotification(synchNodePojo.getNetsimSimulationNames(), synchNodePojo.getNumberOfNodes());
        netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(targetNodeName).getTestManagedObject();
        setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
        enmManagedObjectTree = null;
        enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
        assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));

       
        long beforeDeleteMoGCCount = RestUtility.getGenerationCounter( targetNodeName);  

        SyncNodeServiceTestDataProvider.deleteMoNotification(targetNodeName, synchNodePojo);
        sleep(15);
          
        long afterDeleteMoGCCount = RestUtility.getGenerationCounter( targetNodeName);  
        logger.info(" The GC count before deleteMo was " + beforeDeleteMoGCCount);
        logger.info(" The GC count after  deleteMo was " + afterDeleteMoGCCount);
        assertEquals(beforeDeleteMoGCCount + (2 * synchNodePojo.getNotificationRequestData().size()), afterDeleteMoGCCount);

        setTestStep("Comparison of Netsim Fdn Tree  vs DPS Fdn Tree");

        SyncNodeServiceTestDataProvider.reReadNetsimAfterNotification(synchNodePojo.getNetsimSimulationNames(), synchNodePojo.getNumberOfNodes());
        netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(targetNodeName).getTestManagedObject();
        setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");

        enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
        assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));

    }

    @DataDriven(name = "syncNodeDataProvider1Node100Notification")
    @TestId(id = "TORF-9317-2", title = "Verification of successful consumption of 100 notification change")
    @Test(groups = { "acceptance" })
    public void Successful1Node100Notifications(@Input("TargetSync1Node100Notification") SynchNodeTestData synchNodePojo) {
        assertNotNull("There is No Test Case Data Object, cannot proceed with the TestCase", synchNodePojo);

        String targetNodeName = synchNodePojo.getNodeNames().get(0);
        setAdditionalResultInfo("Target Node= " + targetNodeName);

        setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
        //        final AddRemoveLteNodeOperator AddNodeOperator = operatorProvider.provide(AddRemoveLteNodeOperator.class);
        //        assertTrue(SyncNodeServiceTestDataProvider.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap(), AddNodeOperator));
        //
        //        setTestStep("Synchronize Action for Node " + targetNodeName);
        //        SyncNodeServiceTestDataProvider.synchNode(targetNodeName);
        //
        setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
        TestManagedObject enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
        TestManagedObject netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(targetNodeName).getTestManagedObject();
        assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));

        long beforeCreateMoGCCount = RestUtility.getGenerationCounter( targetNodeName);
        SyncNodeServiceTestDataProvider.createMoNotification(targetNodeName, synchNodePojo);
        sleep(30);
            
        long afterCreateMoGCCount = RestUtility.getGenerationCounter( targetNodeName);
        logger.info(" The GC count before createMo was " + beforeCreateMoGCCount);
        logger.info(" The GC count after  createMo was " + afterCreateMoGCCount);
        assertEquals(beforeCreateMoGCCount + (2 * synchNodePojo.getNotificationRequestData().size()), afterCreateMoGCCount);

        setTestStep("Comparison of Netsim Fdn Tree  vs DPS Fdn Tree");

        SyncNodeServiceTestDataProvider.reReadNetsimAfterNotification(synchNodePojo.getNetsimSimulationNames(), synchNodePojo.getNumberOfNodes());
        netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(targetNodeName).getTestManagedObject();
        setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
        //enmManagedObjectTree = null;
        enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
        assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));
 
        long beforeDeleteMoGCCount = RestUtility.getGenerationCounter( targetNodeName);

        SyncNodeServiceTestDataProvider.deleteMoNotification(targetNodeName, synchNodePojo);
        sleep(30);
         
        long afterDeleteMoGCCount = RestUtility.getGenerationCounter( targetNodeName);
        logger.info(" The GC count before deleteMo was " + beforeDeleteMoGCCount);
        logger.info(" The GC count after  deleteMo was " + afterDeleteMoGCCount);
        assertEquals(beforeDeleteMoGCCount + (2 * synchNodePojo.getNotificationRequestData().size()), afterDeleteMoGCCount);

        setTestStep("Comparison of Netsim Fdn Tree  vs DPS Fdn Tree");

        SyncNodeServiceTestDataProvider.reReadNetsimAfterNotification(synchNodePojo.getNetsimSimulationNames(), synchNodePojo.getNumberOfNodes());
        netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(targetNodeName).getTestManagedObject();
        setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
        enmManagedObjectTree = null;
        enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
        assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));

    }


    //work in progress here. As need clever/simply way to verify the send notifications across multiple nodes
    @DataDriven(name = "syncNodeDataProvider5Node1Notification")
    @TestId(id = "TORF-9317-2", title = "Verification of successful consumption of 100 notification change")
    @Test(groups = { "acceptance" })
    public void Successful5Node1Notifications(@Input("TargetSync5Node1Notification") SynchNodeTestData synchNodePojo) {
        assertNotNull("There is No Test Case Data Object, cannot proceed with the TestCase", synchNodePojo);

        String targetNodeName = synchNodePojo.getNodeNames().get(0);
        setAdditionalResultInfo("Target Node= " + targetNodeName);

        setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
        assertTrue(SyncNodeServiceTestDataProvider.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));

        List<String> nodeNames = synchNodePojo.getNodeNames();

        for (String nodeName : nodeNames) {

            setTestStep("Synchronize Action for Node " + nodeName);
            //   SyncNodeServiceTestDataProvider.synchNode(nodeName);
        }

        for (String nodeName : nodeNames) {

            SyncNodeServiceTestDataProvider.createMoNotification(nodeName, synchNodePojo);
        }

        for (String nodeName : nodeNames) {

            SyncNodeServiceTestDataProvider.reReadNetsimAfterNotification(synchNodePojo.getNetsimSimulationNames(), synchNodePojo.getNumberOfNodes());
            TestManagedObject netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(nodeName).getTestManagedObject();
            setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
            TestManagedObject enmManagedObjectTree = RestUtility.getTestMOsFromDPS(nodeName);

            assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree, netsimManagedObjectTree));

        }
    }
    
    
 // Test case for TORF-24612 - Node re-sync fails after MOs deleted from Netsim
    @DataDriven(name = "syncNodeDataProvider1Node")
    @TestId(id = "TORF-9312-004", title = "Verification of successful Topology Re-Synchronization of 1 LTE Node")
    @Test(groups = { "acceptance" })
    public void SuccessfulReSyncOf1NodeAfterMoCreationAndDeletion(
                    @Input("TargetSync1Node") SynchNodeTestData synchNodePojo) {
            assertNotNull(
                            "There is No Test Case Data Object, cannot proceed with the TestCase",
                            synchNodePojo);
            nodesAddedList = synchNodePojo.getNodeNames();

            String targetNodeName = synchNodePojo.getNodeNames().get(0);
            setAdditionalResultInfo("Target Node= " + targetNodeName);

            setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
            assertTrue(SyncNodeServiceTestDataProvider.createMultipleNodesInDPS(
                            synchNodePojo.getNetsimNodeMap()));

            setTestStep("Synchronize Action for Node " + targetNodeName);
            assertTrue("Failed to synch node",
                            SyncNodeServiceTestDataProvider.synchNode(targetNodeName));

            setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
            TestManagedObject enmManagedObjectTree = RestUtility
                            .getTestMOsFromDPS(targetNodeName);
            TestManagedObject netsimManagedObjectTree = TMONetsimResultsHolder
                            .getTMONetsimResults(targetNodeName).getTestManagedObject();
            assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree,
                            netsimManagedObjectTree));
 
            setTestStep("Create MO Notification");
            SyncNodeServiceTestDataProvider.createMoNotification(targetNodeName,
                            synchNodePojo);
            sleep(2);

            setTestStep("Performing a Re-Sync after create MO");
            setTestStep("Synchronize Action for node " + targetNodeName);
            assertTrue("Failed to synch node",
                            SyncNodeServiceTestDataProvider.synchNode(targetNodeName));

            setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
            enmManagedObjectTree = null;
            enmManagedObjectTree = RestUtility.getTestMOsFromDPS( targetNodeName);
            
            SyncNodeServiceTestDataProvider.reReadNetsimAfterNotification(synchNodePojo.getNetsimSimulationNames(), synchNodePojo.getNumberOfNodes());
            netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(
                            targetNodeName).getTestManagedObject();
            assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree,
                            netsimManagedObjectTree));

            // STOP NETSIM NODE to stop the subscription
            NetworkElement ne = synchNodePojo.getNetsimNodeMap().get(targetNodeName);
            NetsimHelper.stopNE(ne);
            sleep(2);

            // START NETSIM NODE to allow delete MO
            NetsimHelper.startNE(ne);
            sleep(2);
            
            setTestStep("Delete MO Notification");
            SyncNodeServiceTestDataProvider.deleteMoNotification(targetNodeName,
                            synchNodePojo);
            sleep(2);

            setTestStep("Performing a Re-Sync ater delete MO");
            setTestStep("Synchronize Action for node " + targetNodeName);
            assertTrue("Failed to synch node",
                            SyncNodeServiceTestDataProvider.synchNode(targetNodeName));

            setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
            enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
            SyncNodeServiceTestDataProvider.reReadNetsimAfterNotification(synchNodePojo.getNetsimSimulationNames(), synchNodePojo.getNumberOfNodes());
            netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(
                            targetNodeName).getTestManagedObject();
            assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree,
                            netsimManagedObjectTree));


    }

}
