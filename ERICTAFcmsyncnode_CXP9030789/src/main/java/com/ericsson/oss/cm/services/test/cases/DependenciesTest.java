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

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.testng.annotations.*;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.nms.launcher.LauncherOperator;
import com.ericsson.nms.security.OpenIDMOperatorImpl;
import com.ericsson.oss.cm.services.data.CmSyncNodeTestDependencies;
import com.ericsson.oss.cm.services.utilities.EnmUserUtility;
import com.ericsson.oss.mediation.cm.operator.api.lte.AddRemoveLteNodeOperator;
import com.ericsson.oss.mediation.cm.operator.api.lte.SyncLteNodeOperator;
import com.ericsson.oss.mediation.cm.operator.impl.lte.AddRemoveLteNodeOperatorImpl;
import com.ericsson.oss.mediation.cm.operator.impl.lte.SyncLteNodeOperatorImpl;
import com.ericsson.oss.cmsync.operator.api.lte.SuperSyncLteNodeOperator;
import com.ericsson.oss.cmsync.impl.lte.SuperSyncLteNodeOperatorImpl;

public class DependenciesTest extends TorTestCaseHelper implements TestCase {

	private static final Logger logger = Logger
			.getLogger(DependenciesTest.class);

	private final EnmUserUtility userUtility = new EnmUserUtility();
	private static HttpTool httpTool;

	@Inject
	private OperatorRegistry<AddRemoveLteNodeOperator> addOperatorProvider;
	@Inject
	private OperatorRegistry<SyncLteNodeOperator> syncOperatorProvider;
	@Inject
	private OpenIDMOperatorImpl openIDMOperator;
	@Inject
	private LauncherOperator launcherOperator;

	@Inject
	private AddRemoveLteNodeOperatorImpl addOperator;

	@Inject
	private SuperSyncLteNodeOperatorImpl supervisionSyncOperator;

	@Inject
	private SyncLteNodeOperatorImpl syncOperator;

	public void verifyRequiredServicesAreDeployed() {

		User user = new User(DEFAULT_ENM_USER, DEFAULT_ENM_USER_PASSWORD,
				UserType.ADMIN);

		httpTool = launcherOperator.login(user);

		addOperator.setHttpTool(httpTool);
		syncOperator.setHttpTool(httpTool);
		supervisionSyncOperator.setHttpTool(httpTool);

		logger.info("Testing Dependency Testcases");

		CmSyncNodeTestDependencies.setAddRemoveLteNodeOperator(addOperator);
		logger.info("AddOperator Instantiated");

		CmSyncNodeTestDependencies.setSyncLteNodeOperator(syncOperator);
		logger.info("SyncNodeOperator Instantiated");

		CmSyncNodeTestDependencies
				.setSupervisionLteNodeOperator(supervisionSyncOperator);
		logger.info("SuperviosionSyncNodeOperator Instantiated");

	}

	public void afterSuiteTearDown() {
		// userUtility.deleteUserWithOpenIdm(openIDMOperator);
		CmSyncNodeTestDependencies.cleanUp();

		// if (!CmSyncNodeTestDependencies.turnOnPuppet()) {
		// fail("Puppet cannot be turned on");
		// }

	}

	public void verifyAllDependencies() {
		assertNotNull(addOperator);
		assertNotNull(syncOperator);
		// assertTrue("Puppet cannot be turned off",
		// CmSyncNodeTestDependencies.turnOffPuppet());
		assertTrue("Dependencies for the SynchNode have not been validated",
				CmSyncNodeTestDependencies.areTestDependeciesVerified());
	}

}
