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
package com.ericsson.oss.services.dps;

import java.util.*;

/**
 * This is a scaled down version of the ManagedObject. This is to be used for Test purposes for comparing the ManagedObjects defined in the DPS with
 * ManagedObjects defined on Netsim/Node. This should be updated when necessary.

 * 
 * @author EBRIONE
 * 
 */

public class TestManagedObject {

    private String moName;
    private String moType;
    private String fdn;
    private String ipAddress;

    List<TestManagedObject> children = new ArrayList<TestManagedObject>();
    List<String> moAttributes = new ArrayList<String>();

    public TestManagedObject(final String moType, final String moName, final String fdn) {
        this.moType = moType;
        this.moName = moName;
        this.fdn = fdn;

    }

    public String getFdn() {
        return fdn;
    }

    public String getMoName() {
        return moName;
    }

    public String getMoType() {
        return moType;
    }

    public void addChildren(TestManagedObject mo) {
        children.add(mo);
    }

    public List<TestManagedObject> getChildren() {
        return children;
    }

    public void setMoAttributes(String attribute) {
        moAttributes.add(attribute);
    }

    public List<String> getMoAttributes() {
        return moAttributes;
    }
    
    public List<String> getChildrenFdn(){
        List<String> fdnList = new ArrayList<String>();
        for (TestManagedObject tmo: children){
            fdnList.add(tmo.fdn);
            recursiveGetChildrenFdn(tmo, fdnList);
        }
        Collections.sort(fdnList);
        return fdnList;
    }
    
    private List<String> recursiveGetChildrenFdn(TestManagedObject tmo, List<String> list) {
        for (TestManagedObject childTMO: tmo.children){
            list.add(childTMO.fdn);
            recursiveGetChildrenFdn(childTMO, list);
            
        }
        return list;
        
    }
    
    
    public int getChildrenCount(){
        int count = children.size();
        for (TestManagedObject tmo: children){
            count = recursiveGetChildrenCount(tmo, count);
        }
        
        return count;
    }

    private int recursiveGetChildrenCount(TestManagedObject tmo, int tmpCount) {
        tmpCount += tmo.getChildren().size();
        for (TestManagedObject childTMO: tmo.children){
            tmpCount = recursiveGetChildrenCount(childTMO, tmpCount);
            
        }
        return tmpCount;
        
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    
}
