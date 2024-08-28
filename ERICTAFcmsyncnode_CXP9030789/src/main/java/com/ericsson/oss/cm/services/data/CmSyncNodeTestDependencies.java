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
package com.ericsson.oss.cm.services.data;

import java.io.File;

import org.apache.log4j.Logger;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.nms.host.HostConfigurator;
import com.ericsson.oss.cm.services.utilities.DeploymentUtility;
import com.ericsson.oss.cm.services.utilities.RestUtility;
import com.ericsson.oss.cm.services.test.deployment.Artifact;
import com.ericsson.oss.mediation.cm.operator.api.lte.AddRemoveLteNodeOperator;
import com.ericsson.oss.mediation.cm.operator.api.lte.SyncLteNodeOperator;
import com.ericsson.oss.cmsync.operator.api.lte.SuperSyncLteNodeOperator;

public class CmSyncNodeTestDependencies {

	private static final Logger logger = Logger
			.getLogger(CmSyncNodeTestDependencies.class);
	private static DeploymentUtility deploymentUtility = new DeploymentUtility();
	private static AddRemoveLteNodeOperator addOperator;
	private static SyncLteNodeOperator syncOperator;
	private static SuperSyncLteNodeOperator supervisionsyncOperator;

	public static boolean areTestDependeciesVerified() {
		if (!isJcatAccessorDeployedOnJboss())
			return false;

		if (!isDpsAvailable())
			return false;

		return true;
	}

	public static boolean turnOffPuppet() {

		String checkPuppetStatus = "service puppet status";
		String stopThePidgeon = "service puppet stop";
		Boolean puppetOff = false;
		String expectedText = "OK";
		String stopped = "stopped";

		Host sc1Host = HostConfigurator.getSC1();
		logger.info("Here is the ip address gathered from the MediationHost.properties: "
				+ sc1Host.getIp());

		CLI cliToSc1 = new CLI(sc1Host);

		Shell shell = cliToSc1.executeCommand(checkPuppetStatus);
		sleep();
		String s = shell.expect("");
		logger.info("Return String is :" + s);
		if (s.contains(stopped)) {
			puppetOff = true;
		} else {
			try {
				shell = cliToSc1.executeCommand(stopThePidgeon);
				sleep();
				try {
					String puppetStatus = shell.expect(expectedText);
					logger.info("Expected result after turning off is "
							+ puppetStatus);
					puppetOff = true;
				} catch (Exception error) {
					puppetOff = false;
				}
			} catch (Exception error) {
				logger.info("No action required on this server");
				puppetOff = true;
			}
		}

		shell.writeln("exit");
		shell.expectClose();
		assert (shell.isClosed());
		return puppetOff;

	}

	public static boolean turnOnPuppet() {
		String startThePidgeon = "service puppet start";

		Host sc1Host = HostConfigurator.getSC1();
		logger.info("Here is the ip address gathered from the MediationHost.properties: "
				+ sc1Host.getIp());

		CLI cliToSc1 = new CLI(sc1Host);

		Shell shell = cliToSc1.executeCommand(startThePidgeon);

		sleep();
		shell.writeln("exit");
		// Check command exit code
		shell.expectClose();
		assert (shell.isClosed());
		return true;
	}

	/**
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	private static void sleep() {
		try {
			Thread.currentThread().sleep(5000);
		} catch (Exception e) {

		}
	}

	public static void cleanUp() {
		// try {
		// deploymentUtility
		// .unDeployFile(CmSynchConstants.JCAT_ACCESSOR_FILENAME);
		// logger.info("Formal undeploy");
		//
		// } catch (Exception e) {
		//
		// } finally {
		deploymentUtility.closeJbossJmx();
		System.out.println();
		// }
	}

	private static boolean isDpsAvailable() {
		logger.info("Checking if DPS is available");

		String response = RestUtility
				.getResultFromRest("cm-sync-testsuite-jcat-accessor/rest/CM/test/dps/isDpsAvailable");
		if (response.contains("YES")) {
			logger.info("DPS is available");
			return true;
		} else {
			logger.info("DPS is not available");
			return false;
		}
	}

	private static boolean isJcatAccessorDeployedOnJboss() {
		final String jcatAccessorFileName = CmSynchConstants.JCAT_ACCESSOR_FILENAME;

		String fileName = "";
		File archiveFile = null;

		File myCurrentDir = new File("");
		String myCurrentDirPath = myCurrentDir.getAbsolutePath();
		logger.info("Here is the current absolute path " + myCurrentDirPath);
		if (System.getProperty("os.name").startsWith("Windows")) {
			fileName = myCurrentDirPath
					+ "\\cm-sync-testsuite-jcat-accessor\\target\\cm-sync-testsuite-jcat-accessor.war"; // works
			// for
			// windows
			return deploymentUtility.deployFile(fileName);
		} else if (System.getProperty("os.name").startsWith("Linux")) {
			fileName = myCurrentDirPath
					+ "/cm-sync-testsuite-jcat-accessor/target/cm-sync-testsuite-jcat-accessor.war";
			return deploymentUtility.deployFile(fileName);

		} else {
			// fileName =
			// "/local/Jenkins_Slaves/BuildServer_2/workspace/CmSyncNode_Regression_14B/cm-sync-testsuite-jcat-accessor/target/"
			// + jcatAccessorFileName; // works on jenkins
			archiveFile = Artifact
					.resolveArtifactWithoutDependencies(Artifact.COM_ERICSSON_NMS_CMMEDIATION_TAF_WAR);

			if (archiveFile == null) {
				throw new IllegalStateException("Unable to resolve artifact "
						+ Artifact.COM_ERICSSON_NMS_CMMEDIATION_TAF_WAR);
			}
			logger.info("fileName is pulled from nexus "
					+ Artifact.COM_ERICSSON_NMS_CMMEDIATION_TAF_WAR.toString());
			return deploymentUtility.deployFile(archiveFile);
		}

	}

	public static void setAddRemoveLteNodeOperator(
			final AddRemoveLteNodeOperator operator) {
		addOperator = operator;
	}

	public static void setSyncLteNodeOperator(final SyncLteNodeOperator operator) {
		syncOperator = operator;
	}

	public static void setSupervisionLteNodeOperator(
			final SuperSyncLteNodeOperator operator) {
		supervisionsyncOperator = operator;
	}

	public static AddRemoveLteNodeOperator getAddRemoveLteNodeOperator() {
		return addOperator;
	}

	public static SyncLteNodeOperator getSyncLteNodeOperator() {
		return syncOperator;
	}

	public static SuperSyncLteNodeOperator getSupervisionLteNodeOperator() {
		return supervisionsyncOperator;
	}

}
