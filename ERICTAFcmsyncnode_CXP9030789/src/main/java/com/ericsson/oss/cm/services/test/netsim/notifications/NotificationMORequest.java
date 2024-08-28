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
package com.ericsson.oss.cm.services.test.netsim.notifications;

import java.util.HashMap;
import java.util.Map;

public class NotificationMORequest {
    String parentFdn; 
    String type;
    String name;
    String fdn;
    Map<String, Object> map = new HashMap<String, Object>();
   
 

    
    public NotificationMORequest( final String parentFdn, final String type, final String name){
        this.parentFdn = parentFdn;
        this.type = type;
        this.name = name;
        this.fdn = parentFdn + "," + type +"="+name;
        
    }
    
    public String getFdn(){
        return fdn;
    }
    

    public String getParent() {
        return parentFdn;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getAttributes(){
        return map;
    }
    
    public void setAttributes(Map<String, Object> map){
        this.map = map;
    }

}