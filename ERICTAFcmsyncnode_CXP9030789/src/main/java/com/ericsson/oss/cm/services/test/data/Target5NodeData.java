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
import com.ericsson.oss.cm.services.data.CmSynchConstants;

public class Target5NodeData extends TargetNodeData{
    


    //A custom Java Class should be used if you need more control of the test data
    //A single method annotated with @DataSource is required when using a java class as a data source
    @DataSource
    public List<Map<String, Object>> dataSource() {

        List<String> simList = new ArrayList<String>();
        //simList.add(CmSynchConstants.NETSIM_NAME_D_1_1);
        simList.add(CmSynchConstants.NETSIM_NAME);
        
        SynchNodeTestData synchNodePojo = generateTestData(5,simList,true);
        
        Map<String, Object> data = Collections.<String, Object> singletonMap("TargetSync5Nodes", synchNodePojo);
        return Collections.singletonList(data);
    }
}