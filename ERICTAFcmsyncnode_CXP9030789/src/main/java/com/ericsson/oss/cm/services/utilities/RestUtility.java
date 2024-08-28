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
package com.ericsson.oss.cm.services.utilities;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.cifwk.taf.tools.http.HttpToolBuilder;
import com.ericsson.nms.host.HostConfigurator;
import com.ericsson.oss.services.dps.TestManagedObject;
import com.google.gson.Gson;

public class RestUtility {

	static Logger logger = Logger.getLogger(RestUtility.class);

	public static TestManagedObject getTestMOsFromDPS(final String nodeName) {

		String fdn = "MeContext=" + nodeName;
		logger.info("Calling getTestMOsFromDPS for [" + fdn + "]");
		final String url_getMo = "cm-sync-testsuite-jcat-accessor/rest/CM/test/dps/getTestMO/"
				+ fdn;
		final String response = getResultFromRest(url_getMo);
		final Gson gson = new Gson();
		final TestManagedObject rootMo = gson.fromJson(response,
				TestManagedObject.class);
		return rootMo;
	}

	public static String getSyncStatus(final String nodeName) {
		String fdn = "NetworkElement=" + nodeName + ",CmFunction=1";
		// NetworkElement=LTE07ERBS00001,CmFunction=1
		logger.info("Calling getSyncStatus for [" + fdn + "]");
		final String attribute = "syncStatus";

		final String url_getSynchStatus = "cm-sync-testsuite-jcat-accessor/rest/CM/test/dps/getMoAttribute/"
				+ fdn + "/" + attribute;
		String status = getResultFromRest(url_getSynchStatus);
		return status;
	}

	/**
	 * @param fdn
	 */
	public static List<String> getMOAttributes(final String fdn) {
		logger.info("Calling getMOAttributes for [" + fdn + "]");
		final String url_getMo = "cm-sync-testsuite-jcat-accessor/rest/CM/test/dps/getMoAttributes/"
				+ fdn;
		String response = getResultFromRest(url_getMo);

		logger.info("Response is " + response);
		response = response.replace("{", "");
		response = response.replace("}", "");
		response = response.replace("[", "");
		response = response.replace("]", "");

		List<String> list = Arrays.asList(response.split(","));
		return list;

	}

	public static Long getGenerationCounter(final String nodeName) {
		logger.info("Calling getGenerationCounter for [" + nodeName + "]");
		// NetworkElement=LTE07ERBS00001,CppConnectivityInformation=1
		String fdn = "NetworkElement=" + nodeName
				+ ",CppConnectivityInformation=1";
		final String cmd = "cm-sync-testsuite-jcat-accessor/rest/CM/test/dps/getMoAttributeNameAndType/"
				+ fdn + "/" + "generationCounter";
		String response = getResultFromRest(cmd);

		logger.info("response is " + response);
		String parsedGC = response.substring(0, response.indexOf(":::"));
		logger.info("The GC value for [" + fdn + "] is " + parsedGC);

		return Long.parseLong(parsedGC);
	}

	public static String getMoAttributeNameAndType(final String fdn,
			final String attribute) {

		logger.info("Calling getAttributeNameAndType for [" + fdn + "]" + "["
				+ attribute + "]");
		final String cmd = "cm-sync-testsuite-jcat-accessor/rest/CM/test/dps/getMoAttributeNameAndType/"
				+ fdn + "/" + attribute;
		String response = getResultFromRest(cmd);

		return response;

	}

	public static String getResultFromRest(final String urlString) {
		// logger.info("Called getResultFromRest with args for url: " +
		// urlString);

		HttpTool httpTool = HttpToolBuilder.newBuilder(
				HostConfigurator.getCmService()).build();
		String response = httpTool.get(urlString).getBody();

		httpTool.close();

		return response;
	}

}
