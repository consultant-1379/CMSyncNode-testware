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

//import static com.ericsson.oss.cm.services.data.CmSynchConstants.ACCESS_METHOD;

import java.util.*;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.TestData;
import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement;
//import com.ericsson.enm.data.NetworkNode;
import com.ericsson.oss.cm.services.data.CmSyncNodeTestDependencies;
import com.ericsson.oss.cm.services.test.concurrency.TopologySyncNodeMonitor;
import com.ericsson.oss.cm.services.test.netsim.NetsimHelper;
import com.ericsson.oss.cm.services.test.netsim.notifications.NotificationMORequest;
import com.ericsson.oss.cmsync.operator.api.lte.SuperSyncLteNodeOperator;
//import com.ericsson.oss.mediation.cm.operator.api.exception.CmMediationOperatorException;
import com.ericsson.oss.mediation.cm.operator.api.lte.AddRemoveLteNodeOperator;
import com.ericsson.oss.mediation.cm.operator.api.lte.AttributeKeys;

public class SyncNodeServiceTestDataProvider implements TestData {

	private static final Logger logger = Logger
			.getLogger(SyncNodeServiceTestDataProvider.class);

	public static boolean createMultipleNodesInDPS(
			Map<String, NetworkElement> map) {
		int numberOfNodes = map.size();
		logger.info("Called createMultipleNodesInDPS for [" + numberOfNodes
				+ "] number of nodes");

		Boolean createdWithoutError = false;
		int count = 0;

		try {
			for (Map.Entry<String, NetworkElement> entry : map.entrySet()) {
				NetworkElement ne = entry.getValue();
				String nodeName = ne.getName();
				String ipAddress = ne.getIp();

				logger.info("Creating " + nodeName + " " + "with address "
						+ ipAddress + " in DPS");

				Boolean addedSucessfully = true;

				Map<AttributeKeys, String> attributes = new HashMap<AttributeKeys, String>();
				attributes.put(AttributeKeys.IPADDRESS, ipAddress);
				attributes.put(AttributeKeys.NEID, nodeName);
				AddRemoveLteNodeOperator operator = getAddRemoteLteNodeOperator();

				addedSucessfully = operator.addSingleLteNode(nodeName,
						attributes);

				if (!addedSucessfully) {
					logger.error("Network Node could not be Created Successfully");
					break;
				}
				count++;
				logger.info("Completed " + count + "/" + numberOfNodes);
			}
			createdWithoutError = true;

		} catch (Exception e) {
			logger.error("Couldn't Create Any LTE Network Nodes Correctly  .... ");
			e.printStackTrace();
			;
		}
		return createdWithoutError;

	}

	private static AddRemoveLteNodeOperator getAddRemoteLteNodeOperator() {
		return CmSyncNodeTestDependencies.getAddRemoveLteNodeOperator();
	}

	private static SuperSyncLteNodeOperator getSuperSyncLteNodeOperator() {
		return CmSyncNodeTestDependencies.getSupervisionLteNodeOperator();
	}

	public static boolean deleteSingleNodeCLI(final String nodeName) {
		Boolean deletedSuccessResult = false;
		Boolean turnOffSupervision = false;
		logger.info("Called deleteSingleNodeFromDPS for " + nodeName);
		Map<AttributeKeys, String> attributes = new HashMap<AttributeKeys, String>();
		attributes.put(AttributeKeys.NEID, nodeName);

		getSuperSyncLteNodeOperator().disableNotificationsFromNode(nodeName);

		deletedSuccessResult = getAddRemoteLteNodeOperator().removeSingleNode(
				nodeName, attributes);
		logger.info("Result of Deleteing Node " + nodeName);

		return deletedSuccessResult;

	}

	public static boolean deleteNodesCLI(List<String> nodeNames) {
		boolean result = true;
		for (String node : nodeNames) {
			result = deleteSingleNodeCLI(node);
			if (result == false) {
				break;
			}
		}
		return result;
	}

	public static SynchNodeTestData readTopologyForMultipleNodesFromNetsim(
			List<String> simulations, final int numberOfNodes,
			boolean doDumpMoTree) {

		Map<String, NetworkElement> netsimNodeMap = new TreeMap<String, NetworkElement>();
		int neCount = 0;
		for (NetworkElement netsimNE : NetsimHelper
				.getAllStartedNesFromSimList(simulations)) {
			if (neCount < numberOfNodes) { // this check is needed otherwise the
											// all nodes on the netsim would be
											// added to list
				String nodeName = netsimNE.getName();
				netsimNodeMap.put(nodeName, netsimNE);
				if (doDumpMoTree) {
					logger.info("Dumping the netsim topology for "
							+ netsimNE.getName());
					NetsimHelper.dumpFullTopologyTree(netsimNE);
				}

			} else {
				break;
			}
			neCount++;

		}
		if (neCount != 0) {

			SynchNodeTestData syn = new SynchNodeTestData(netsimNodeMap);
			return syn;
		} else
			logger.error("No Active Netsim Nodes could be found please check your Netsim Simulations");
		return null;
	}

	/*
	 * This method is required in the notification test cases whereby the a
	 * netsim has to be re-read following a notification change
	 */
	public static void reReadNetsimAfterNotification(List<String> simulations,
			final int numberOfNodes) {
		int neCount = 0;
		for (NetworkElement netsimNE : NetsimHelper
				.getAllStartedNesFromSimList(simulations, numberOfNodes)) {
			if (neCount < numberOfNodes) {
				logger.info("Dumping the netsim topology for "
						+ netsimNE.getName());
				NetsimHelper.dumpFullTopologyTree(netsimNE);
			}
			neCount++;
		}

	}

	public static boolean deleteNodesFromDPS(List<String> deleteNodeNamesList) {
		logger.info("Called deleteNodesFromDPS for ["
				+ deleteNodeNamesList.size() + "] number of nodes");
		boolean result = true;
		for (String nodeName : deleteNodeNamesList) {
			logger.info("Deleting " + nodeName + "from DPS");
			result = deleteSingleNodeCLI(nodeName);
			if (result == false) {
				break;
			}
		}
		return result;

	}

	public static boolean synchNode(String nodeName) {
		logger.info("Called synchNode for " + nodeName);
		List<String> singleEntry = new ArrayList<String>();
		singleEntry.add(nodeName);
		TopologySyncNodeMonitor monitor = new TopologySyncNodeMonitor(
				singleEntry);
		return monitor.startSynchNodeProcess();

	}

	public static boolean synchMultipleNodesInParallel(List<String> synchFdnList) {
		logger.info("Called synchMultipleNodesInParallel for ["
				+ synchFdnList.size() + "] number of nodes");
		TopologySyncNodeMonitor monitor = new TopologySyncNodeMonitor(
				synchFdnList);
		return monitor.startSynchNodeProcess();

	}

	public static void createMoNotification(String targetNodeName,
			SynchNodeTestData synchNodePojo) {
		List<NotificationMORequest> notifications = synchNodePojo
				.getNotificationRequestData();
		NetsimHelper.createMoNotification(synchNodePojo
				.getNetsimSimulationNames().get(0), targetNodeName,
				notifications);
	}

	public static void deleteMoNotification(String targetNodeName,
			SynchNodeTestData synchNodePojo) {
		List<NotificationMORequest> notifications = synchNodePojo
				.getNotificationRequestData();
		NetsimHelper.deleteMoNotification(synchNodePojo
				.getNetsimSimulationNames().get(0), targetNodeName,
				notifications);

	}

	public static void sendAVCNotification(String targetNodeName,
			SynchNodeTestData synchNodePojo, final String fdn,
			final String attributeName, final Object attributeValue) {

		NetsimHelper.createSetAttributeNotification(synchNodePojo
				.getNetsimSimulationNames().get(0), targetNodeName, fdn,
				attributeName, attributeValue);

	}

}