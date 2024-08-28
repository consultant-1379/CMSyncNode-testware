/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.cm.services.utilities;

import static com.ericsson.oss.cm.services.data.CmSynchConstants.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.enm.data.ENMUser;
import com.ericsson.nms.security.OpenIDMOperatorImpl;

public class EnmUserUtility {

	private static Logger LOGGER = LoggerFactory
			.getLogger(EnmUserUtility.class);

	public ENMUser createUserWithOpenIdm(
			final OpenIDMOperatorImpl openIDMOperator) {
		LOGGER.info("Logging in with default username and password... "
				+ DEFAULT_ENM_USER + ", " + DEFAULT_ENM_USER_PASSWORD);

		openIDMOperator.connect(DEFAULT_ENM_USER, DEFAULT_ENM_USER_PASSWORD);

		final ENMUser enmUser = createEnmUser();
		LOGGER.info("Creating user ... " + enmUser.getUsername());
		openIDMOperator.createUser(enmUser);
		openIDMOperator.assignUsersToRole(ENM_ADMIN_USER_ROLE,
				enmUser.getUsername());
		return enmUser;
	}

	public ENMUser createEnmUser() {
		final ENMUser enmUser = createEnmUser();

		// enmUser.setUsername(USERNAME);
		// enmUser.setFirstName(FIRSTNAME);
		// enmUser.setLastName(LASTNAME);
		// enmUser.setEmail(EMAIL);
		// enmUser.setEnabled(true);
		// enmUser.setPassword(PASSWORD);

		return enmUser;
	}

	public void deleteUserWithOpenIdm(final OpenIDMOperatorImpl openIDMOperator) {
		LOGGER.info("Deleting user [" + USERNAME + "]");
		openIDMOperator.deleteUser(USERNAME);
	}
}
