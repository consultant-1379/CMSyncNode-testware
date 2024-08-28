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
package com.ericsson.oss.cmsync.util;

import static com.ericsson.oss.cmsync.operator.api.common.constants.Constants.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.nms.host.HostConfigurator;

/**
 * This class provides a means for operator classes to create and cache
 * instances of the TAF tools used by the operators.
 */
public class ToolGetter {

	final private static Logger LOGGER = LoggerFactory
			.getLogger(ToolGetter.class);
	final private static ConcurrentMap<String, CLI> cliToolList = new ConcurrentHashMap<String, CLI>();

	/**
	 * Returns a {@link CLI} object that clients can use to execute commands on
	 * the host identified by the {@code hostPropertyName} parameter. A new
	 * {@code CLI} object will only be created if one does not already exist for
	 * the host.
	 * 
	 * @param hostPropertyName
	 *            - name of the system property that identifies the DNS name or
	 *            ipAddress of the host (SC-1, SC-2, MS etc.)for which the
	 *            {@code CLI} is to be created.
	 * 
	 * @return Instance of {@code CLI} object.
	 * 
	 */
	public synchronized static CLI getCliTool(final String hostPropertyName) {

		LOGGER.info("Fetching CLI instance for: {}", hostPropertyName);

		CLI cli = cliToolList.get(hostPropertyName);

		if (cli == null) {
			final Host host = getHost(hostPropertyName);

			LOGGER.info("Creating new CLI session for: {}", host.getIp());
			cli = new CLI(host);
			cliToolList.putIfAbsent(hostPropertyName, cli);
		}
		return cli;
	}

	private static Host getHost(final String hostPropertyName) {

		Host host;

		switch (hostPropertyName) {
		case SC1_PROPERTY_NAME:
			host = HostConfigurator.getSC1();
			break;
		case SC2_PROPERTY_NAME:
			host = HostConfigurator.getSC2();
			break;
		case CMSERV_SU0_PROPERTY_NAME:
			host = HostConfigurator.getCmService();
			break;
		default:
			LOGGER.info("Unrecognised property, using DataHandler to get host.");
			host = DataHandler.getHostByName(hostPropertyName);
		}
		return host;
	}
}