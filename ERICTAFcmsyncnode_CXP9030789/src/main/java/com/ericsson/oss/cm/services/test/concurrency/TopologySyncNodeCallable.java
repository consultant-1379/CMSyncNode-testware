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
package com.ericsson.oss.cm.services.test.concurrency;

import static com.ericsson.oss.cm.services.data.CmSynchConstants.ACCESS_METHOD;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.ericsson.oss.cm.services.data.CmSyncNodeTestDependencies;
import com.ericsson.oss.cm.services.utilities.RestUtility;
import com.ericsson.oss.mediation.cm.operator.api.lte.SyncLteNodeOperator;
import com.ericsson.oss.cmsync.operator.api.lte.SuperSyncLteNodeOperator;

public class TopologySyncNodeCallable implements Callable<Boolean> {
	private static final Logger logger = Logger
			.getLogger(TopologySyncNodeCallable.class);
	private String synchFdn = "";

	SyncLteNodeOperator syncNodeOperator;
	SuperSyncLteNodeOperator supervisionSyncNodeOperator;

	public TopologySyncNodeCallable(final String synchFdn) {
		this.synchFdn = synchFdn;
		this.syncNodeOperator = CmSyncNodeTestDependencies
				.getSyncLteNodeOperator();
		this.supervisionSyncNodeOperator = CmSyncNodeTestDependencies
				.getSupervisionLteNodeOperator();
	}

	/*
	 * This method now wait for a period of 300 seconds (retry count of 30 *
	 * sleep period of 10 seconds) for the node to synch. If after this period
	 * the node has not synched, then a default result of false is returned.
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Boolean call() throws Exception {

		Thread.sleep(1000);
		// syncNodeOperator.createCmFunctionMo(synchFdn);

		logger.info(synchFdn + " is starting to Synch on ThreadId["
				+ Thread.currentThread().getId() + "]");
		// syncNodeOperator.syncSingleNode(synchFdn);
		supervisionSyncNodeOperator.syncSingleNode(synchFdn);
		//
		// ScriptEngineRestUtility.synchNode(synchFdn);
		// RestUtility.syncNode(synchFdn);
		String syncState = "";
		int retryCount = 30;
		boolean result = false;
		for (int i = 1; i < retryCount; ++i) {
			// syncState = RestUtility.getSyncStatus(synchFdn);
			// syncState = syncState.replaceAll("\\n", "");

			if (syncNodeOperator.isSyncComplete(synchFdn)) {
				syncState = "SYNCHRONIZED";
			} else {
				syncState = "INTERMEDIATE";
			}

			logger.info("The SYNCH STATE for " + synchFdn + " is [" + syncState
					+ "]. Attempt " + i + "/" + retryCount + ".");
			Thread.sleep(1000);
			if (syncState.equals("SYNCHRONIZED")) {
				result = true;
				break;
			} else {
				result = false;
			}

		}

		return result;
	}

}
