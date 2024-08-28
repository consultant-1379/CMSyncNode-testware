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

import java.util.*;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimCommandHandler;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimSession;
import com.ericsson.cifwk.taf.handlers.netsim.commands.NetSimCommands;

public class NetsimNotificationExecutor {

    private static NetsimNotificationExecutor notificationGenerator;
    Host host;
    private static final Logger logger = Logger.getLogger(NetsimNotificationExecutor.class);

    NetSimSession session;

    private NetsimNotificationExecutor() {
        host = DataHandler.getHostByName("Netsim");

    }

    public static NetsimNotificationExecutor getInstance() {
        if (notificationGenerator == null) {
            notificationGenerator = new NetsimNotificationExecutor();
        }
        return notificationGenerator;
    }

    public void createMo(String simulation, String netsimNodeName, List<NotificationMORequest> notificationRequests) {
        session = NetSimCommandHandler.getSession(host);
        List<String> cmds = new ArrayList<String>();
        String createCmd = "createmo:parentid=";
        for (NotificationMORequest notificationRequest : notificationRequests) {
            String parentId = notificationRequest.getParent();
            String moType = notificationRequest.getType();
            String moName = notificationRequest.getName();

            StringBuilder builder = new StringBuilder();
            builder.append(createCmd);
            builder.append("\"" + parentId + "\"");
            builder.append(",type=\"" + moType + "\"");
            builder.append(",name=\"" + moName + "\";");
            String command = netsimNodeName + "\n" + builder;
            //logger.info("Sending the following create mo notification command: " + command);
            cmds.add(command);

        }

        //String command = netsimNodeName + "\n" + builder;
        // logger.info("Sending the following create mo notification command: " + command);
        String[] cmdArray = cmds.toArray(new String[cmds.size()]);
        for (String s : cmdArray) {
            logger.info("Sending the following create mo notification command:" + s);
            session.exec(NetSimCommands.open(simulation), NetSimCommands.selectnocallback(s));
        }

        //        session.exec(NetSimCommands.open(simulation), NetSimCommands.selectnocallback(cmdArray));
        session.close();
    }

    public void deleteMo(String simulation, String netsimNodeName, List<NotificationMORequest> notificationRequests) {
        session = NetSimCommandHandler.getSession(host);
        // deletemo:moid="ManagedElement=1,TransportNetwork=1,Sctp=Brian";
        String deleteCmd = "deletemo:moid=";
        List<String> cmds = new ArrayList<String>();
        for (NotificationMORequest notificationRequest : notificationRequests) {
            String parentId = notificationRequest.getParent();
            String moType = notificationRequest.getType();
            String moName = notificationRequest.getName();

            StringBuilder builder = new StringBuilder();
            builder.append(deleteCmd);
            builder.append("\"" + parentId);
            builder.append("," + moType + "=" + moName + "\";");

            String command = netsimNodeName + "\n" + builder;
            cmds.add(command);
        }

        String[] cmdArray = cmds.toArray(new String[cmds.size()]);
        for (String s : cmdArray) {
            logger.info("Sending the following delete mo notification command:" + s);
            session.exec(NetSimCommands.open(simulation), NetSimCommands.selectnocallback(s));
        }

        session.close();

    }

  //setmoattribute:mo="ManagedElement=1,TransportNetwork=1,Sctp=BrianB", attributes="userLabel=ul4";
    public void setAttribute(String simulation, String netsimNodeName, final String fdn, final String attributeName, final Object attributeValue) {
        if (session==null || session.isClosed()){
            session = NetSimCommandHandler.getSession(host);
        }
        // setmoattribute:mo="ManagedElement=1,TransportNetwork=1,Sctp=Brian", attributes="userLabel=ul6";
        String setAttributeCmd = "setmoattribute:mo=";
        List<String> cmds = new ArrayList<String>();

        StringBuilder builder = new StringBuilder();
        builder.append(setAttributeCmd);
        builder.append("\"" + fdn + "\"");
        builder.append(",attributes=\"");
        builder.append(attributeName + "=" + attributeValue + "\";");

        String command = netsimNodeName + "\n" + builder;
        cmds.add(command);

        String[] cmdArray = cmds.toArray(new String[cmds.size()]);
        for (String s : cmdArray) {
            logger.info("Sending the following set mo attribute notification command:" + s);
            session.exec(NetSimCommands.open(simulation), NetSimCommands.selectnocallback(s));
        }

        // logger.info("Sending the following set mo attribute notification command: " + command);
        //  session.exec(NetSimCommands.open(simulation), NetSimCommands.selectnocallback(command));
        session.close();
        session = null;
        

    }


}