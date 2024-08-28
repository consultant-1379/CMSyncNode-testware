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

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimCommandHandler;
import com.ericsson.cifwk.taf.handlers.netsim.domain.*;
import com.ericsson.oss.cm.services.test.netsim.notifications.*;

public class NetsimHelper {

    private static final Logger logger = Logger.getLogger(NetsimHelper.class);
    static Map<String, List<NetworkElement>> simulationNEMap = new HashMap<String, List<NetworkElement>>();

    private static NetsimHelper netsimHelper;

    private NetsimHelper() {
    }

    public static NetsimHelper getInstance() {
        if (netsimHelper == null) {
            netsimHelper = new NetsimHelper();
        }
        return netsimHelper;
    }

    // This method for getAllNEs to provide a list of NEs for a given simulation
    private static List<NetworkElement> getAllStartedNEs(final NetSimCommandHandler netSimCommandHandler, final String simNamePattern) {
        List<NetworkElement> neList = new ArrayList<NetworkElement>();
        List<NetworkElement> startedNeList = new ArrayList<NetworkElement>();

        if (simNamePattern == "") {
            return neList;
        }

        logger.info("The simulation pattern is " + simNamePattern);

        SimulationGroup mySims = netSimCommandHandler.getAllSimulations();

        List<String> list = new ArrayList<String>();
        for (Simulation simName : mySims) {
            String simulationName = simName.getName();
            if (simulationName.startsWith(simNamePattern)) {
                logger.info("The following simulation exists " + simulationName);
                list.add(simulationName);
            }
        }

        String[] str_Arr = list.toArray(new String[list.size()]);
        NeGroup neGroup = netSimCommandHandler.getSimulationNEs(str_Arr);
        logger.info("The number of neGroups is " + neGroup.size());
        neList = neGroup.getNetworkElements();
        for (NetworkElement ne : neList) {
            startedNeList.add(ne);
        }

        return startedNeList;
    }

    private static void addToSimNeMap(String simulationName, NetworkElement networkElement) {
        List<NetworkElement> list = simulationNEMap.get(simulationName);
        if (list == null) {
            list = new ArrayList<NetworkElement>();
            list.add(networkElement);
        } else {
            list.add(networkElement);
        }
        simulationNEMap.put(simulationName, list);
    }

    public static List<NetworkElement> getAllStartedNesFromSimList(List<String> simulationNames) {

        populateStartedNesMap(simulationNames);

        final Set<String> names = simulationNEMap.keySet();
        List<NetworkElement> allTheNodes = new ArrayList<>();
        logger.info(" getNetsimNEFromMap was here  ");
        for (String sim : simulationNames) {
            for (String name : names) {
                if (sim.startsWith(name)) {
                    allTheNodes.addAll(simulationNEMap.get(name));
                }
            }
        }
        return allTheNodes;
    }

    public static List<NetworkElement> getAllStartedNesFromSimList(List<String> simulationNames, final int numberOfNodes) {

        populateStartedNesMap(simulationNames, numberOfNodes);

        final Set<String> names = simulationNEMap.keySet();
        List<NetworkElement> allTheNodes = new ArrayList<>();
        logger.info(" getNetsimNEFromMap was here  ");
        for (String sim : simulationNames) {
            for (String name : names) {
                if (sim.startsWith(name)) {
                    allTheNodes.addAll(simulationNEMap.get(name));
                }
            }
        }
        return allTheNodes;
    }

    private static void populateStartedNesMap(List<String> simulations, int numberOfNodes) {
        clearMap();
        int tmpCount = 0;
        for (String sim : simulations) {
            List<NetworkElement> neList = getAllStartedNEs(getNetsimCommandHandler(), sim);
            logger.info("neList size is " + neList.size());

            for (NetworkElement networkElement : neList) {
                if (tmpCount < numberOfNodes) {
                    logger.info("Adding the following to map- Name[" + networkElement.getName() + "],ipaddress[" + networkElement.getIp()
                            + "],simulation[" + networkElement.getSimulation() + "]");
                    tmpCount++;
                    addToSimNeMap(sim, networkElement);
                } else {
                    logger.info("The correct number of netsims have been added to the map");
                    break;
                }
            }

        }

    }

    private static void populateStartedNesMap(List<String> simulations) {
        clearMap();
        for (String sim : simulations) {
            List<NetworkElement> neList = getAllStartedNEs(getNetsimCommandHandler(), sim);
            logger.info("neList size is " + neList.size());

            for (NetworkElement networkElement : neList) {
                logger.debug("Adding the following to map- Name[" + networkElement.getName() + "],ipaddress[" + networkElement.getIp()
                        + "],simulation[" + networkElement.getSimulation() + "]");
                addToSimNeMap(sim, networkElement);
            }
        }

    }

    private static NetSimCommandHandler getNetsimCommandHandler() {
        Host netsimHost = DataHandler.getHostByName("Netsim");
        return NetSimCommandHandler.getInstance(netsimHost);

    }

    public static void dumpFullTopologyTree(NetworkElement ne) {
        NetsimReader.dumpFullTopologyTree(ne);

    }

    public static List<String> dumpMOAttributes(NetworkElement networkElement, String fdn) {
        return NetsimReader.dumpMOAttributes(networkElement, fdn);

    }

    public static void createMoNotification(final String simulation, final String netsimNodeName, List<NotificationMORequest> notificationRequest) {
        NetsimNotificationExecutor.getInstance().createMo(simulation, netsimNodeName, notificationRequest);
    }

    public static void deleteMoNotification(final String simulation, final String netsimNodeName,
                                            final List<NotificationMORequest> notificationRequest) {
        NetsimNotificationExecutor.getInstance().deleteMo(simulation, netsimNodeName, notificationRequest);

    }

    public static void createSetAttributeNotification(final String simulation, final String netsimNodeName, final String fdn,
                                                      final String attributeName, final Object attributeValue) {
        NetsimNotificationExecutor.getInstance().setAttribute(simulation, netsimNodeName, fdn, attributeName, attributeValue);
    }


    private static void clearMap() {
        simulationNEMap.clear();
    }
    
    public static void startNE(NetworkElement ne) {
        ne.start();

    }
    
    public static void stopNE(NetworkElement ne) {
        ne.stop();
                
        }

}