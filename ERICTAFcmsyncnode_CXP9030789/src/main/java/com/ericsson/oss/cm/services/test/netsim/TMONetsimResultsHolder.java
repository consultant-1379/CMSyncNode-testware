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
package com.ericsson.oss.cm.services.test.netsim;

import java.util.HashMap;
import java.util.Map;

public class TMONetsimResultsHolder {

    //private static TMONetsimResultsHolder resultsHolder;
    
    private static Map<String, TMONetsimResults > map = new HashMap<String, TMONetsimResults>();

    private TMONetsimResultsHolder() {}
    
    public static void setNetsimResults(final String name, final TMONetsimResults results){
        map.put(name, results);        
    }
    
    
    public static Map<String, TMONetsimResults> getMap() {
        return map;
    }
    
    
    public static  TMONetsimResults getTMONetsimResults(final String nodeName){
        return map.get(nodeName);
    }
}
