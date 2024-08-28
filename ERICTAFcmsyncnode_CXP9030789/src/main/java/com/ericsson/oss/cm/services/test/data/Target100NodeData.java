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

import java.util.*;

import com.ericsson.cifwk.taf.annotations.DataSource;

public class Target100NodeData extends TargetNodeData {

	public static final String NETSIM_NAME_D_1_189_FARM2 = "LTED1189-FT-LTE01";

	// A custom Java Class should be used if you need more control of the test
	// data
	// A single method annotated with @DataSource is required when using a java
	// class as a data source
	@DataSource
	public List<Map<String, Object>> dataSource() {

		List<String> simList = new ArrayList<String>();
		simList.add(NETSIM_NAME_D_1_189_FARM2);

		SynchNodeTestData synchNodePojo = generateTestData(100, simList, false);

		Map<String, Object> data = Collections.<String, Object> singletonMap(
				"TargetSync100Nodes", synchNodePojo);
		return Collections.singletonList(data);
	}
}