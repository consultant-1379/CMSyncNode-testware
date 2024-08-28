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

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.oss.cm.services.test.netsim.NetsimHelper;

public class CmSynchConstants {

	/*
	 * Netsim Constants
	 */
	public static final String NETSIM_NAME_D_1_1 = "LTED1189-FT-LTE02";

	public static final String NETSIM_NAME_D_1_1_NOTIFICATIONS = "LTED1189-FT-LTE02-3";

	// public static final String NETSIM_NAME_D_1_1 =
	// "LTED1189-V2limx160-RV-FDD-LTE11";
	public static final String NETSIM_NAME_D_1K = "LTED1189-FT-LTE0";

	// public static final String NETSIM_NAME = (String)
	// DataHandler.getAttribute("netsim.simulation.name");

	public static final String NETSIM_NAME = netsimSimNameHelper();

	static String netsimSimNameHelper() {

		String providedString = (String) DataHandler
				.getAttribute("netsim.simulation.name");
		if (providedString == null) {
			providedString = "LTED1189-FT-LTE02-3";
		}
		return providedString;
	}

	/*
	 * JCAT Accessor war file name
	 */
	public static final String JCAT_ACCESSOR_FILENAME = "cm-sync-testsuite-jcat-accessor.war";

	/*
	 * D.1.189 ERBS LTE NODE Version
	 */
	public static final String D_1_189_LTE_VERSION = "4.1.189";

	public static final String ACCESS_METHOD = "httpd_su0";
	public static final String DEFAULT_ENM_USER = "administrator";
	public static final String DEFAULT_ENM_USER_PASSWORD = "TestPassw0rd";

	public static final String ID_TOKEN_1 = "IDToken1";
	public static final String ID_TOKEN_2 = "IDToken2";
	public static final String APACHE_LOGIN_URI = "/login";
	public static final String VALID_LOGIN = "0";
	public static final String TOR_USERID = "TorUserID";

	public static final String USERNAME = "mjolnr1";
	public static final String FIRSTNAME = "mj1";
	public static final String LASTNAME = "nr1";
	public static final String EMAIL = "mjolnr1@ericsson.com";
	public static final String PASSWORD = "TestPassw0rd";
	public static final String ENM_ADMIN_USER_ROLE = "ADMINISTRATOR";

}
