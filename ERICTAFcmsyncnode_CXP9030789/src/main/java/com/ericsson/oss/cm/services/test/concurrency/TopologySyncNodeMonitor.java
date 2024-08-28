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

import java.util.*;
import java.util.concurrent.*;

import org.apache.log4j.Logger;

public class TopologySyncNodeMonitor {

	private static final Logger logger = Logger
			.getLogger(TopologySyncNodeMonitor.class);
	final List<String> synchFdnList;
	ExecutorService executor;
	Map<String, Future<Boolean>> synchJobsFutureMap;

	boolean areSyncnsOngoing = true;
	List<String> successfulSyncList = new ArrayList<String>();
	List<String> unSuccessfulSyncList = new ArrayList<String>();

	public TopologySyncNodeMonitor(final List<String> synchFdnList) {
		this.executor = Executors.newFixedThreadPool(synchFdnList.size());
		this.synchFdnList = synchFdnList;
	}

	public boolean startSynchNodeProcess() {
		Map<String, Future<Boolean>> synchJobsFutureMap = startSyncJobsinParallel(
				executor, synchFdnList);
		return monitorSyncStatus(executor, synchJobsFutureMap);

	}

	private boolean monitorSyncStatus(final ExecutorService executor,
			final Map<String, Future<Boolean>> synchJobsFutureMap) {
		executor.shutdown();
		return checkStatus(synchJobsFutureMap);

	}

	private boolean checkStatus(
			final Map<String, Future<Boolean>> synchJobFutureMap) {

		int totalSynchRequests = synchJobFutureMap.size();
		waitUntilAllFuturesDone(synchJobFutureMap);

		logger.info("************************************************************************************");
		logger.info("The total number of synch requests are ["
				+ totalSynchRequests + "]");
		logger.info("The number of completed synch's are    ["
				+ successfulSyncList.size() + "]");
		logger.info("The number of incompleted synch's are  ["
				+ unSuccessfulSyncList.size() + "]");

		boolean result = false;
		if (unSuccessfulSyncList.isEmpty()) {
			result = true;
		} else {
			logger.info("The following nodes did not synch "
					+ unSuccessfulSyncList);
			result = false;
		}

		logger.info("************************************************************************************");
		return result;

	}

	/**
	 * @param synchJobFutureMap
	 */
	private void waitUntilAllFuturesDone(
			Map<String, Future<Boolean>> synchJobFutureMap) {
		try {

			int numberOfSynchActions = synchJobFutureMap.size();
			logger.info("The number of synchs started is "
					+ numberOfSynchActions);

			while (!synchJobFutureMap.isEmpty()) {

				Iterator<String> it = synchJobFutureMap.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					Future<Boolean> fut = synchJobFutureMap.get(key);
					if (fut.isDone()) {
						if (fut.get()) {
							// logger.info(key + " did synch ");
							it.remove();
							successfulSyncList.add(key);

						} else {
							// logger.info(key + " did not synch ");
							it.remove();
							unSuccessfulSyncList.add(key);
						}

					} else {
						// logger.info("Fut key is not done : " + key);
					}

				}
				logger.info("The number of synch requests        is   ["
						+ numberOfSynchActions + "]");
				logger.info("The number of successful synch's    is   ["
						+ successfulSyncList.size() + "]");
				logger.info("The number of un-successful synch's is   ["
						+ unSuccessfulSyncList.size() + "]");
				logger.info("The number of outstanding synch's   is   ["
						+ synchJobFutureMap.size() + "]");
				sleep(15000);
			}
		} catch (Exception e) {
			logger.error("Exception caught waiting for all synchs to complete : "
					+ e.getLocalizedMessage());
			e.printStackTrace();

		}

	}

	private Map<String, Future<Boolean>> startSyncJobsinParallel(
			final ExecutorService executor, final List<String> synchFdnList) {

		logger.info("**************************************************************************************************************");
		logger.info("STARTING TO SYNCH FOR " + synchFdnList.size() + " NODE(s)");
		logger.info("**************************************************************************************************************");
		Map<String, Future<Boolean>> map = new HashMap<String, Future<Boolean>>();

		for (int i = 0; i < synchFdnList.size(); i++) {
			TopologySyncNodeCallable callable = new TopologySyncNodeCallable(
					synchFdnList.get(i));
			Future<Boolean> future = executor.submit(callable);
			map.put(synchFdnList.get(i), future);
			sleep(310);
		}

		return map;

	}

	private void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
