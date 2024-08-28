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

import java.io.File;

import com.ericsson.cifwk.taf.handlers.JbossHandler;
import com.ericsson.nms.host.HostConfigurator;

public class DeploymentUtility {

	JbossHandler jbossHandler;

	public DeploymentUtility() {
		jbossHandler = new JbossHandler(HostConfigurator.getCmService(),
				HostConfigurator.getSC1());
	}

	public boolean deployFile(final String targerEar) {
		Boolean activate = true;
		Boolean forcedeploy = true;
		File targetEarFile = new File(targerEar);
		return jbossHandler.deployFile(targetEarFile, activate, forcedeploy);
	}

	public boolean deployFile(final File targetEarFile) {
		Boolean activate = true;
		Boolean forcedeploy = true;
		return jbossHandler.deployFile(targetEarFile, activate, forcedeploy);
	}

	public void unDeployFile(final String targerEar) {
		Boolean removeContent = true;
		jbossHandler.undeployFile(targerEar, removeContent);
	}

	public void closeJbossJmx() {
		jbossHandler.getJmxService().close();
	}

}
