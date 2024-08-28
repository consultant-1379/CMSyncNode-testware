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

import java.util.*;

import com.ericsson.oss.services.dps.TestManagedObject;
/*
 * This class is a helper class that is used to convert the data read from netsim into TestManagedObject.
 * This allows the data that is read from DPS to be compared with the Netsim data.
 * 
 * This code is tightly coupled with the format of the output of the Netsim DumpMoTree command. 
 * It is not ideal but it works for the moment to allow the Topology Information to be read from Netsim. 
 * 
 * NOTE:  Do NOT change this code with full knowledge of the output of the Netsim DumpMoTree command.
 */
public class TMOCreator {

    List<String> mos = new ArrayList<String>();
    List<Integer> tabCountList = new ArrayList<Integer>();
    Map<Integer, String> tabFdn = new LinkedHashMap<Integer, String>();
    TMONetsimResults netsimResults = new TMONetsimResults();
    final String nodeName;

    int previousTabCount;

    public TMOCreator(final String nodeName, final List<String> mos, final List<Integer> tabCount) {
        this.mos = mos;
        this.tabCountList = tabCount;
        this.nodeName = nodeName ;

    }

    public void createMOs() {
        String parent = "";

        for (int i = 0; i < mos.size(); i++) {
            int tab = tabCountList.get(i);
            String mo = mos.get(i);
            setTabFdn(tab, mo);
            parent = getTabParent(tab);
            if (parent.contains("," + mo)) {
                String newParent = parent.substring(0, parent.lastIndexOf("," + mo));
                parent = newParent;
            }

            createMO(parent, mo);

        }
        
        TMONetsimResultsHolder.setNetsimResults(nodeName, netsimResults);
    }



        private void createMO(String parentFdn, final String moFdn) {
            String fdn = "";
            TestManagedObject managedObject;
            if (parentFdn == ""){
                 managedObject = new TestManagedObject("MeContext", nodeName, "MeContext=" +  nodeName);
                 netsimResults.setTestManagedObject(managedObject);
                 
                 
                 managedObject = new TestManagedObject(moFdn.split("=")[0], moFdn.split("=")[1],"MeContext=" + nodeName + "," + moFdn);
                 parentFdn =  "MeContext=" + nodeName;
            }
            else {
                parentFdn =  "MeContext=" + nodeName + "," + parentFdn;
                fdn = parentFdn + "," + moFdn;
                managedObject = new TestManagedObject(moFdn.split("=")[0], moFdn.split("=")[1], fdn);
               
            }
       
                         
            TestManagedObject parent = netsimResults.getParent(parentFdn);
            if(parent != null){
                parent.addChildren(managedObject);
            }
        }

    private void setTabFdn(final int tab, final String mo) {
        tabFdn.put(tab, mo);
    }

    /**
     * @param tab
     * 
     */
    private String getTabParent(final int tab) {
        if (tab == 0) {
            return "";
        }
        String parent = "";
        int mapSize = tabFdn.size();
        int count = 1;
        if (previousTabCount > tab) {
            count = 2;
        }

        for (int key : tabFdn.keySet()) {
            if (count == mapSize) {
                break;
            }
            if (parent == "") {
                parent = tabFdn.get(key);
            } else
                parent = parent + "," + tabFdn.get(key);

            count++;
        }
        previousTabCount = tab;
        return parent;

    }

}
