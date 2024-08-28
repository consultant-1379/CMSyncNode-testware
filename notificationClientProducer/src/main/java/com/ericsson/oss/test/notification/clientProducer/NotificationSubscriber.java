/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 **
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.test.notification.clientProducer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.omg.CORBA.IntHolder;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
// import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.BasicConfig.Filter;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.BasicConfig.MIBRef;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.BasicConfig.MOidpair;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.BasicConfig.NotDefined;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.BasicConfig.ProcessingFailure;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.BasicConfig.SecurityViolation;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.ConfigurationExtended;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.ConfigurationExtendedHelper;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.MIBChangeInfo;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.MIBInfoExt;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.MOInfo;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.MOInfoSeqHolder;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.NotificationConsumer;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.NotificationConsumerHelper;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.NotificationProducer;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.NotificationProducerPackage.MOTypeAndAttributes;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.NotificationProducerPackage.NotificationFilterExt;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.NotificationProducerPackage.ToOldGenerationCount;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.CosNaming.NameComponent;
import com.ericsson.oss.mediation.network.converter.outbound.exception.IllFormedMONameException;

public class NotificationSubscriber {

    ConfigurationExtended service;

    NotificationConsumerLocal notifConsumer;
    NotificationProducer notifProducer;

    NotificationGenerator notificationGenerator;

    private POA rootpoa;
    private ORB orb;
    private final String nodeAddress;

    final IntHolder id = new IntHolder(0);

    NotificationSubscriber(final POA rootpoa, final ORB orb, final String nodeIPAddress) {
        this.rootpoa = rootpoa;
        this.orb = orb;
        nodeAddress = nodeIPAddress;

        setSystemProperties();
        resolvePoaAndOrb(nodeAddress);

    }

    public void printSubscriptionData() {
        printNodeDetails();
        printRestartTime();
        printGenerationCounterValue();
        printNumberOfMibChanges();
    }

    public void startSubscription() {
        startSubscription(rootpoa, orb, nodeAddress);
    }

    public void generateNotification() {
        notificationGenerator = new NotificationGenerator(service);
        notificationGenerator.setMoAttribute();

    }

    private void resolvePoaAndOrb(final String nodeAddress) {
        try {
            orb = ORB.init(new String[] { nodeAddress }, null);
            rootpoa = POAHelper.narrow(orb.resolve_initial_references("VBRootPOA"));
            rootpoa.the_POAManager().activate();
        } catch (final InvalidName e) {
            e.printStackTrace();
        } catch (final AdapterInactive e) {
            e.printStackTrace();
        }
    }

    private static void setSystemProperties() {

        final String jbossPublicAddress = getJbossPublicAddress();
        System.setProperty("vbroker.security.disable", "true");
        System.setProperty("vbroker.ce.iiop.host", System.getProperty("corba.client.host.v4", jbossPublicAddress));// 141.137.248.15//192.168.0.120
        System.setProperty("vbroker.se.iiop_tp.host", System.getProperty("corba.client.host.v4", jbossPublicAddress));// 141.137.248.15//192.168.0.120
        System.setProperty("vbroker.se.iiop_tp.scm.iiop_tp.listener.port", "59000");
        System.setProperty("vbroker.se.iiop_tp.scm.iiop_tp.listener.portRange", "59001");
        System.setProperty("org.omg.CORBA.ORBClass", "com.inprise.vbroker.orb.ORB");
        System.setProperty("org.omg.CORBA.ORBSingletonClass", "com.inprise.vbroker.orb.ORBSingleton");

    }

    private static String getJbossPublicAddress() {
        final String command = "ps -ef | grep Stand | sed 's/.*-Djboss.bind.address=\\(.*\\)-Djgroups.bind_addr.*/\\1/' | grep -v Stand";
        // System.out.println("Using command to get the public ip address of MSCM : " + command);
        final String ipAddress = executeCommand(command);
        // System.out.println("Resolved the public ip address of MSCM to : " + ipAddress);
        return ipAddress;
    }

    private static String executeCommand(final String command) {

        final StringBuffer output = new StringBuffer();
        try {
            final Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
            p.waitFor();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                if (!line.contains("(")) {
                    output.append(line);
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }

        String res = output.toString();
        res = res.replaceAll("\\s", "");
        return res.toString();

    }

    private void printNodeDetails() {
        try {
            final MIBRef[] mibRefArray = service.get_MIBs(null);

            final MIBInfoExt mibInfoExt = service.get_MIB_info_ext(mibRefArray[0], null);
            final StringBuilder details = new StringBuilder("Node Details: ");
            details.append("name: ").append(mibInfoExt.name);
            details.append(", type: ").append(mibInfoExt.type);
            details.append(", version: ").append(mibInfoExt.version);
            details.append(", release: ").append(mibInfoExt.release);
            details.append(", id: ").append(mibInfoExt.id);
            details.append(", prefix: ").append(nameComponentToFdn(mibInfoExt.prefix));
            System.out.println(details.toString());
        } catch (final Exception e) {
            System.out.println("Exception reading the node details: " + e);
            e.printStackTrace();
        }

    }

    private void printRestartTime() {
        long timeTaken = 0;
        final long startTime = System.currentTimeMillis();
        final HttpIORClient iorClient = new HttpIORClient();
        HttpIORClient.HttpInfo httpInfo = null;

        try {
            httpInfo = iorClient.getIOR(nodeAddress);
            timeTaken = System.currentTimeMillis() - startTime;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        System.out.println("The node was restarted last on : " + httpInfo.getRestartDate().toLocaleString() + ", timeTaken: " + timeTaken);

    }

    private void printNumberOfMibChanges() {
        try {
            final int gc = notifProducer.get_generation_count();
            MIBChangeInfo[] mibChanges;
            mibChanges = notifProducer.get_MIB_changes(gc);

            System.out.println("Number of outstanding MIB Changes are : " + mibChanges.length);
            for (final MIBChangeInfo mibChange : mibChanges) {
                System.out.println("");
                System.out.println(mibChange.toString());

            }
        } catch (final ToOldGenerationCount e) {
            e.printStackTrace();
        }

    }

    private void printGenerationCounterValue() {
        try {
            final int gc = notifProducer.get_generation_count();
            System.out.println("The current Generation Counter for the node is : " + gc);
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    private void startSubscription(final POA rootpoa, final ORB orb, final String nodeAddress) {
        System.out.println("Starting the subscription for node : " + nodeAddress);
        final GetIor getIor = new GetIor(nodeAddress);
        getIor.run();
        String result = null;
        try {
            result = getIor.getResult(5000L);
        } catch (final Exception e) {
            System.out.println(e);
        }

        final String celloNamingIor = result;// get the ior from getIor
        final org.omg.CORBA.Object obj = orb.string_to_object(celloNamingIor);
        final NamingContext celloNaming = NamingContextHelper.narrow(obj);
        final org.omg.CosNaming.NameComponent[] nc = { new org.omg.CosNaming.NameComponent("CelloConfigurationService", "") };
        org.omg.CORBA.Object pmOpObj = null;
        try {
            pmOpObj = celloNaming.resolve(nc);
        } catch (final NotFound e) {
            System.out.println(e);
        } catch (final CannotProceed e) {
            System.out.println(e);
        } catch (final org.omg.CosNaming.NamingContextPackage.InvalidName e) {
            System.out.println(e);
        }
        service = ConfigurationExtendedHelper.unchecked_narrow(pmOpObj);
        notifConsumer = new NotificationConsumerLocal(nodeAddress);
        notifProducer = service.get_notification_producer();

        try {

            final NotificationConsumer cons = NotificationConsumerHelper.narrow(rootpoa.servant_to_reference(notifConsumer));

            notifProducer.subscribe_ext(cons, new NotificationFilterExt(new MOTypeAndAttributes[0], true, true, "", -1), 18000, id);

            System.out.println("Subscription created with SubscriptionId : " + id.value);
        } catch (final Exception e) {
            System.out.println("Caught exception from subscribe_ext interface," + e.toString());
        }

    }

    public boolean isNodeSubscribed() {
        return notifProducer.get_subscription_status(id.value);

    }

    public void removeSubscription() {
        notifProducer.unsubscribe(id.value);

    }

    /**
     *
     */
    public void printContainmentQuery() {
        int iterator;
        try {
            final String rootMo = getFullDN(service.get_root_MO(null));
            iterator = service.get_MO_containment(rootMo, 10000, new Filter(), null);
            boolean isAnyLeft = true;
            while (isAnyLeft) {
                final MOInfoSeqHolder resultHolder = new MOInfoSeqHolder();
                isAnyLeft = service.next_MO_info(iterator, 100000, null, resultHolder);
                final MOInfo[] moInfoValues = resultHolder.value;
                for (final MOInfo moInfo : moInfoValues) {
                    System.out.println("MOInfo is : type = " + moInfo.type + " localDN = " + moInfo.localDN
                            + " prefix is = " + moInfo.prefix);
                }
            }

        } catch (final NotDefined e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final SecurityViolation e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final ProcessingFailure e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getFullDN(final MOInfo info) {
        final StringBuilder builder = new StringBuilder();
        if (info.prefix != null && !info.prefix.isEmpty()) {
            builder.append(info.prefix).append(',');
        }
        return builder.append(info.localDN).toString();
    }

    /**
     * @param string
     */
    public void printMO(final String string) {
        System.out.println("HOw many instances on node are there " + service.get_mo_instance_count());
        try {

            System.out.println("The MO string exists on the node " + service.basic_is_existing(createMoIdPair(string), null));
        } catch (final NotDefined e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final SecurityViolation e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final ProcessingFailure e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public MOidpair createMoIdPair(final String moDName) {
        try {
            final MOidpair result = new MOidpair();
            result.name(str2name(moDName));
            return result;
        } catch (final IllFormedMONameException e) {

        }
        return null;
    }

    public static NameComponent[] str2name(final String moFDName) throws IllFormedMONameException {
        final LinkedList result = new LinkedList();
        String head = moFDName;

        while (head.length() > 0) {
            final String tail = getRDName(head);
            result.addFirst(new NameComponent(tail, "")); // notice that all is in ID part
            head = getParentDName(head);
        }
        return (NameComponent[]) result.toArray(new NameComponent[result.size()]);
    }

    public static String nameComponentToFdn(final NameComponent[] nameComponents) {
        final StringBuilder fdn = new StringBuilder(100);
        if (nameComponents != null && nameComponents.length > 0) {
            for (int i = 0; i < nameComponents.length; ++i) {
                if (i > 0) {
                    fdn.append(",");
                }
                fdn.append(nameComponents[i].id);
            }
        }
        return fdn.toString();
    }

    public static String getRDName(final String dName) throws IllFormedMONameException {
        final int lastComma = dName.lastIndexOf(",");

        if (lastComma >= 3) { // probably ok, don't validate the MO name, it is costly
            {
                return dName.substring(lastComma + 1);
            }
        }
        return dName;
    }

    public static String getParentDName(final String dName) throws IllFormedMONameException {
        final int lastComma = dName.lastIndexOf(",");

        if (lastComma >= 3) { // probably ok, don't validate the MO name, it is costly
            {
                return dName.substring(0, lastComma);
            }
        }
        return "";
    }
}
