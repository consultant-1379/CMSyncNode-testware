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

import java.util.List;

import org.apache.log4j.Logger;

public class TargetNodeData {

	private static final Logger logger = Logger.getLogger(TargetNodeData.class);

	protected SynchNodeTestData generateTestData(final int numberOfNodes,
			List<String> simList, boolean readNetsim) {

		logger.info("Read Topology Tree from Netsim for " + numberOfNodes
				+ " node(s)");

		SynchNodeTestData synchNodePojo = SyncNodeServiceTestDataProvider
				.readTopologyForMultipleNodesFromNetsim(simList, numberOfNodes,
						readNetsim);

		if (synchNodePojo == null) {
			logger.error("There is No Test Case Data Object, cannot proceed with the TestCase "
					+ synchNodePojo);
			return null;
		} else {
			synchNodePojo.setNetsimSimulationNames(simList);
			logger.info("Test Case Data Objects were created correctly for the following number of nodes  "
					+ synchNodePojo.getNumberOfNodes());

		}
		return synchNodePojo;
	}

}
