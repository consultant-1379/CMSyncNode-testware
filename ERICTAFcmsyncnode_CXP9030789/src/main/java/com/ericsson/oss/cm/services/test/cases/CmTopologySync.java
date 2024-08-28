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

import static com.ericsson.oss.cm.services.data.CmSynchConstants.DEFAULT_ENM_USER;
import static com.ericsson.oss.cm.services.data.CmSynchConstants.DEFAULT_ENM_USER_PASSWORD;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.*;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.nms.launcher.LauncherOperator;
import com.ericsson.oss.cm.services.data.CmSyncNodeTestDependencies;
import com.ericsson.oss.cm.services.test.data.SyncNodeServiceTestDataProvider;
import com.ericsson.oss.cm.services.test.data.SynchNodeTestData;
import com.ericsson.oss.cm.services.test.netsim.TMONetsimResultsHolder;
import com.ericsson.oss.cm.services.utilities.MoComparatorUtility;
import com.ericsson.oss.cm.services.utilities.RestUtility;
import com.ericsson.oss.mediation.cm.operator.impl.lte.AddRemoveLteNodeOperatorImpl;
import com.ericsson.oss.mediation.cm.operator.impl.lte.SyncLteNodeOperatorImpl;
import com.ericsson.oss.services.dps.TestManagedObject;
import com.google.inject.Inject;

/**
 * //TODO Add class javadoc
 */
public class CmTopologySync extends TorTestCaseHelper implements TestCase {
	private static final Logger logger = Logger.getLogger(CmTopologySync.class);
	private List<String> nodesAddedList = new ArrayList<String>();

	@Inject
	private DependenciesTest dependenciesTest;

	@BeforeSuite
	public void dependencyInvocation() {
		dependenciesTest.verifyRequiredServicesAreDeployed();
	}

	@AfterSuite
	public void dependencyCleanDown() {
		dependenciesTest.afterSuiteTearDown();
	}

	@TestId(id = "TORF-9312-007", title = "Verification Test Dependencies")
	@Test(groups = { "acceptance" })
	public void dependencyVerifyAllDependencies() {
		dependenciesTest.verifyAllDependencies();
	}

	@TestId(id = "TORF-9312-001", title = "Deletion of all nodes added during test case execution")
	@AfterMethod
	public void DeletionOfAllAddedNodes() {
		logger.info("In DeletionOfAllAddedNodes method");

		for (String nodeName : nodesAddedList) {
			assertTrue("Failed to delete Node !!!",
					SyncNodeServiceTestDataProvider
							.deleteSingleNodeCLI(nodeName));

		}

	}

	@DataDriven(name = "syncNodeDataProvider1Node")
	@TestId(id = "TORF-9312-001", title = "Verification of successful Topology Synchronization of 1 LTE Node")
	@Test(groups = { "acceptance" })
	public void SuccessfulSynchOf1Node(
			@Input("TargetSync1Node") SynchNodeTestData synchNodePojo) {

		assertNotNull(
				"There is No Test Case Data Object, cannot proceed with the TestCase",
				synchNodePojo);

		nodesAddedList = synchNodePojo.getNodeNames();
		String targetNodeName = synchNodePojo.getNodeNames().get(0);
		setAdditionalResultInfo("Target Node= " + targetNodeName);

		setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
		assertTrue(SyncNodeServiceTestDataProvider
				.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));

		setTestStep("Synchronize Action for Node " + targetNodeName);
		assertTrue("Failed to synch node",
				SyncNodeServiceTestDataProvider.synchNode(targetNodeName));

		// setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
		// TestManagedObject enmManagedObjectTree = RestUtility
		// .getTestMOsFromDPS(targetNodeName);
		// TestManagedObject netsimManagedObjectTree = TMONetsimResultsHolder
		// .getTMONetsimResults(targetNodeName).getTestManagedObject();
		// assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree,
		// netsimManagedObjectTree));

		// setTestStep("Comparison of selected Attributes in Five Random FullDefinedName Managed Objects");
		// assertTrue(MoComparatorUtility.verifyBasicAttributeKeys(
		// synchNodePojo.getNetsimSimulationNE(targetNodeName),
		// enmManagedObjectTree));

	}

	@DataDriven(name = "syncNodeDataProvider5Nodes")
	@TestId(id = "TORF-9312-002", title = "Verification of successful Sequential Topology Synchronizations of 5 LTE Nodes")
	@Test(groups = { "acceptance" })
	public void SuccessfulSyncOf5Nodes(
			@Input("TargetSync5Nodes") SynchNodeTestData synchNodePojo) {

		int numberTargetNodes = synchNodePojo.getNumberOfNodes();

		setTestStep("Creating " + numberTargetNodes + " node(s) in DPS ");
		assertTrue(SyncNodeServiceTestDataProvider
				.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));

		List<String> nodeNames = synchNodePojo.getNodeNames();
		nodesAddedList = nodeNames;
		for (String nodeName : nodeNames) {

			setAdditionalResultInfo("Target Node= " + nodeName);

			setTestStep("Synchronize Action for Node " + nodeName);
			assertTrue("Failed to synch node",
					SyncNodeServiceTestDataProvider.synchNode(nodeName));

			setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
			TestManagedObject enmManagedObjectTree = RestUtility
					.getTestMOsFromDPS(nodeName);
			TestManagedObject netsimManagedObjectTree = TMONetsimResultsHolder
					.getTMONetsimResults(nodeName).getTestManagedObject();
			assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree,
					netsimManagedObjectTree));

		}

	}

	@DataDriven(name = "syncNodeDataProvider1Node")
	@TestId(id = "TORF-9312-004", title = "Verification of successful Topology Re-Synchronization of 1 LTE Node")
	@Test(groups = { "acceptance" })
	public void SuccessfulReSyncOf1Node(
			@Input("TargetSync1Node") SynchNodeTestData synchNodePojo) {
		assertNotNull(
				"There is No Test Case Data Object, cannot proceed with the TestCase",
				synchNodePojo);

		String targetNodeName = synchNodePojo.getNodeNames().get(0);
		nodesAddedList = synchNodePojo.getNodeNames();
		setAdditionalResultInfo("Target Node= " + targetNodeName);

		setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
		assertTrue(SyncNodeServiceTestDataProvider
				.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));

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

		setTestStep("Comparison of selected Attributes in Five Random FullDefinedName Managed Objects");
		assertTrue(MoComparatorUtility.verifyBasicAttributeKeys(
				synchNodePojo.getNetsimSimulationNE(targetNodeName),
				enmManagedObjectTree));

		setTestStep("Performing a Re-Sync");
		setTestStep("Synchronize Action for node " + targetNodeName);
		assertTrue("Failed to synch node",
				SyncNodeServiceTestDataProvider.synchNode(targetNodeName));

		setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
		enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
		netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(
				targetNodeName).getTestManagedObject();
		assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree,
				netsimManagedObjectTree));

		setTestStep("Comparison of selected Attributes in Five Random FullDefinedName Managed Objects");
		assertTrue(MoComparatorUtility.verifyBasicAttributeKeys(
				synchNodePojo.getNetsimSimulationNE(targetNodeName),
				enmManagedObjectTree));

	}

	@DataDriven(name = "syncNodeDataProvider1Node")
	@TestId(id = "TORF-9312-004", title = "Verification of successful Topology Re-Synchronization of 1 LTE Node")
	@Test(groups = { "acceptance" })
	public void SuccessfulReSyncOf1NodeAfterMoCreationAndDeletion(
			@Input("TargetSync1Node") SynchNodeTestData synchNodePojo) {
		assertNotNull(
				"There is No Test Case Data Object, cannot proceed with the TestCase",
				synchNodePojo);

		String targetNodeName = synchNodePojo.getNodeNames().get(0);
		nodesAddedList = synchNodePojo.getNodeNames();
		setAdditionalResultInfo("Target Node= " + targetNodeName);

		setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
		assertTrue(SyncNodeServiceTestDataProvider
				.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));

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
		netsimManagedObjectTree = null;
		enmManagedObjectTree = RestUtility.getTestMOsFromDPS(targetNodeName);
		netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(
				targetNodeName).getTestManagedObject();
		assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree,
				netsimManagedObjectTree));

		// STOP NETSIM NODE
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
		netsimManagedObjectTree = TMONetsimResultsHolder.getTMONetsimResults(
				targetNodeName).getTestManagedObject();
		assertTrue(MoComparatorUtility.areEqual(enmManagedObjectTree,
				netsimManagedObjectTree));

	}

	@DataDriven(name = "syncNodeDataProvider2Nodes")
	@TestId(id = "TORF-9312-005", title = "Verification of successful comparison of 2 different LTE Nodes")
	@Test(groups = { "acceptance" })
	public void UnSuccessfulSynchOf1Node(
			@Input("TargetSync2Nodes") SynchNodeTestData synchNodePojo) {
		assertNotNull(
				"There is No Test Case Data Object, cannot proceed with the TestCase",
				synchNodePojo);

		String targetNodeName = synchNodePojo.getNodeNames().get(0);
		nodesAddedList = synchNodePojo.getNodeNames();
		setAdditionalResultInfo("Target Node= " + targetNodeName);

		setTestStep("Creating " + targetNodeName + " node(s) in DPS ");
		assertTrue(SyncNodeServiceTestDataProvider
				.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));

		List<String> nodeNames = synchNodePojo.getNodeNames();
		String nodeNameToSycn = nodeNames.get(0);
		String nodeNameNotToSycn = nodeNames.get(1);

		// setTestStep("Setting all nodes to UNSYNCHRONIZED state");
		// SyncNodeServiceTestDataProvider.setNodeToUnSynchronizedState(nodeNameToSycn);

		setTestStep("Synchronize Action for Node " + nodeNameToSycn);
		assertTrue("Failed to synch node",
				SyncNodeServiceTestDataProvider.synchNode(nodeNameToSycn));

		setTestStep("Comparison of ENM Fdn Tree Vs Netsim Fdn Tree");
		TestManagedObject enmManagedObjectTree = RestUtility
				.getTestMOsFromDPS(targetNodeName);
		TestManagedObject netsimManagedObjectTree = TMONetsimResultsHolder
				.getTMONetsimResults(nodeNameNotToSycn).getTestManagedObject();

		setTestStep("Comparison of selected Attributes in Five Random FullDefinedName Managed Objects");
		assertFalse(MoComparatorUtility.areEqual(enmManagedObjectTree,
				netsimManagedObjectTree));

	}

	@DataDriven(name = "syncNodeDataProvider38Nodes")
	@TestId(id = "TORF-9312-007", title = "Verification of successfull Parallel Node Synchronization of 38 LTE Nodes")
	@Context(context = { "REST" })
	@Test(groups = { "acceptance" })
	public void SuccessfulSyncOfNodesInParallelOf38Nodes(
			@Input("TargetSync38Nodes") SynchNodeTestData synchNodePojo) {

		int numberOfNodes = synchNodePojo.getNumberOfNodes();
		nodesAddedList = synchNodePojo.getNodeNames();

		setTestcase("TORF-9312-007",
				"Verification of successfull Parallel Node Synchronization of "
						+ numberOfNodes + " LTE Nodes");
		setTestStep("Creating " + numberOfNodes + " node(s) in DPS ");
		assertTrue(SyncNodeServiceTestDataProvider
				.createMultipleNodesInDPS(synchNodePojo.getNetsimNodeMap()));

		List<String> nodeNames = synchNodePojo.getNodeNames();

		setTestStep("Synchronize node(s) in parallel");
		assertTrue("Failed to synch node",
				SyncNodeServiceTestDataProvider
						.synchMultipleNodesInParallel(nodeNames));

		for (String nodeName : nodeNames) {
			setAdditionalResultInfo("Target Node= " + nodeName);
		}

	}
	//
	// @DataDriven(name = "syncNodeDataProvider100Nodes")
	// @TestId(id = "TORF-9312-008", title =
	// "Utility to successfull Parallel Node Synchronization of 100 LTE Nodes, Nodes not Deleted")
	// @Test(groups = { "acceptance" })
	// public void SuccessfulSyncOfNodesInParallelOf100Nodes(
	// @Input("TargetSync100Nodes") SynchNodeTestData synchNodePojo) {
	// int numberOfNodes = synchNodePojo.getNumberOfNodes();
	// setTestcase("TORF-9312-008",
	// "Verification of successfull Parallel Node Synchronization of "
	// + numberOfNodes + " LTE Nodes");
	// setTestStep("Creating " + numberOfNodes + " node(s) in DPS ");
	// final AddRemoveLteNodeOperator AddNodeOperator = operatorProvider
	// .provide(AddRemoveLteNodeOperator.class);
	// assertTrue(SyncNodeServiceTestDataProvider.createMultipleNodesInDPS(
	// synchNodePojo.getNetsimNodeMap(), AddNodeOperator));
	//
	// List<String> nodeNames = synchNodePojo.getNodeNames();
	// setTestStep("Setting all nodes to UNSYNCHRONIZED state");
	// SyncNodeServiceTestDataProvider.setAllNodesToUnSynchronizedState();
	//
	// setTestStep("Synchronize node(s) in parallel");
	// assertTrue("Failed to synch node",
	// SyncNodeServiceTestDataProvider
	// .synchMultipleNodesInParallel(nodeNames));
	//
	// for (String nodeName : nodeNames) {
	// setAdditionalResultInfo("Target Node= " + nodeName);
	// }
	// }
	//
	// @DataDriven(name = "syncNodeDataProvider1000Nodes")
	// @TestId(id = "TORF-9312-08", title =
	// "Verification of successfull Parallel Node Synchronization of 1000 LTE Nodes")
	// @Test(groups = { "acceptance" })
	// public void SuccessfulSyncOfNodesInParallelOf1000Nodes(
	// @Input("TargetSync1000Nodes") SynchNodeTestData synchNodePojo) {
	// int numberOfNodes = synchNodePojo.getNumberOfNodes();
	// setTestcase("TORF-9312-08",
	// "Verification of successfull Parallel Node Synchronization of "
	// + numberOfNodes + " LTE Nodes");
	//
	// List<String> nodeNames = synchNodePojo.getNodeNames();
	// setTestStep("Creating " + numberOfNodes + " node(s) in DPS ");
	// final AddRemoveLteNodeOperator AddNodeOperator = operatorProvider
	// .provide(AddRemoveLteNodeOperator.class);
	// assertTrue(SyncNodeServiceTestDataProvider.createMultipleNodesInDPS(
	// synchNodePojo.getNetsimNodeMap(), AddNodeOperator));
	//
	// setTestStep("Setting all nodes to UNSYNCHRONIZED state");
	// SyncNodeServiceTestDataProvider.setAllNodesToUnSynchronizedState();
	//
	// setTestStep("Synchronize node(s) in parallel");
	// assertTrue("Failed to synch node",
	// SyncNodeServiceTestDataProvider
	// .synchMultipleNodesInParallel(nodeNames));
	//
	// // setTestStep("Deleting Node(s) from DPS with Cmedit Delete All");
	// // assertTrue("Failed to delete Node !!!",
	// //
	// SyncNodeServiceTestDataProvider.deleteNodesFromDPS(synchNodePojo.getNodeNames()));
	//
	// }
}
