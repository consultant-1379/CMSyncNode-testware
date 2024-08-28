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

import com.ericsson.cifwk.taf.handlers.netsim.NetSimResult;
import com.ericsson.cifwk.taf.handlers.netsim.commands.DumpmotreeCommand;
import com.ericsson.cifwk.taf.handlers.netsim.commands.NetSimCommands;
import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement;

public class NetsimReader {

    public static void dumpFullTopologyTree(NetworkElement networkElement) {
        DumpmotreeCommand dumpmotreeCommand = NetSimCommands.dumpmotree();
        dumpmotreeCommand.setMoid("ManagedElement=1");
        NetSimResult results = networkElement.exec(dumpmotreeCommand);
        final String raw = results.getRawOutput();
        String[] split = raw.split("\r\n");
        List<String> rawList = Arrays.asList(split);  
        int index = rawList.indexOf("ManagedElement=1");
        List<String> topologyMOList =  new ArrayList<String> (rawList.subList(index,rawList.size() -2));
        createMOsFromList(networkElement.getName(), topologyMOList);
    }

    
    public static List<String> dumpMOAttributes(NetworkElement networkElement, String fdn) {
        String nodeName = "MeContext="  + networkElement.getName()+ ",";
        String convertedFdn = fdn.replace(nodeName, "");
        
        DumpmotreeCommand dumpmotreeCommand = NetSimCommands.dumpmotree();
        dumpmotreeCommand.setMoid(convertedFdn);
        dumpmotreeCommand.setScope(0);
        dumpmotreeCommand.setPrintattrs(true);
        
        NetSimResult results = networkElement.exec(dumpmotreeCommand);
        final String raw = results.getRawOutput();
        String[] split = raw.split("\r\n");
        List<String> rawList = Arrays.asList(split);  
        List<String> attributesList =  new ArrayList<String> (rawList.subList(5,rawList.size() -3));
        return attributesList;
    }


    private static void createMOsFromList(final String nodeName, final List<String> list) {
        final List<String> mosList = new ArrayList<String>();
        final List<Integer> tabCountList = new ArrayList<Integer>();
        for (String s : list) {
            char[] chars = s.toCharArray();
            int currentTab = 0;

            for (int i = 0; i < chars.length; i++) {
                if (Character.isWhitespace(chars[i])) {
                    currentTab++;
                }
            }

            s = s.replaceAll(" ", "");

            mosList.add(s);
            tabCountList.add(currentTab);

        }

        final TMOCreator moCreater = new TMOCreator(nodeName, mosList, tabCountList);
        moCreater.createMOs();
    }

}
