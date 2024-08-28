
package com.ericsson.oss.test.notification.clientProducer;

import java.net.InetAddress;
import java.net.MalformedURLException;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;

public class NotificationClientStarter implements IShutdownThreadParent {

    POA rootpoa = null;
    ORB orb = null;
    NotificationSubscriber notificationSubscriber = null;
    String ipAddress = "";
    String GET_CONTAINMENT_QUERY = "getContainmentQuery";

    private ShutdownThread fShutdownThread;

    public static void main(final String[] args) throws MalformedURLException {
        System.out.println(args[0] + " and length " + args.length);
        if (args.length < 1) {
            printUsage();
            System.exit(-1);
        }
        printTestClientDescription();
        sleep(8);
        validateArguments(args);
        new NotificationClientStarter().invokeCommand(args);
    }

    private void invokeCommand(final String[] args) {
        ipAddress = args[0];
        if (args.length == 1) {
            new NotificationClientStarter().start(args);
        }
        for (final String str : args) {
            if (str.contains(GET_CONTAINMENT_QUERY)) {
                final NotificationClientStarter client = new NotificationClientStarter();
                client.printContainmentQuery();
            }
        }
    }

    /**
     *
     */
    private void printContainmentQuery() {
        notificationSubscriber = new NotificationSubscriber(rootpoa, orb, ipAddress);
        notificationSubscriber.startSubscription();
        notificationSubscriber.printContainmentQuery();

    }

    private static void validateArguments(final String[] args) {

        printLineMessage("########## 1) Verifies that the node can be reached");
        sleep(4);
        if (!isAddressReachable(args[0])) {
            System.err.println("The following address is not valid or is not reachable : " + args[0]);
            System.exit(-1);
        }
        printLineMessage("The node can be reached");

    }

    private static void printUsage() {
        printTestClientDescription();
        System.err.println("Incorrect usage: You need to pass the node ipAddress");
        System.err.println("e.g. 10.20.97.1");
    }

    private static boolean isAddressReachable(final String ipAddress) {
        boolean isReachable = false;
        try {
            isReachable = InetAddress.getByName(ipAddress).isReachable(5000);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return isReachable;
    }

    private void start(final String[] args) {
        sleep(4);
        ipAddress = args[0];
        fShutdownThread = new ShutdownThread(this);
        Runtime.getRuntime().addShutdownHook(fShutdownThread);

        printLinePadding();
        printLineMessage("########## 2) Subscribes to the node");
        sleep(4);
        notificationSubscriber = new NotificationSubscriber(rootpoa, orb, ipAddress);
        notificationSubscriber.startSubscription();
        printLinePadding();
        printLineMessage("########## 3) Get some basic node details");
        sleep(4);
        notificationSubscriber.printSubscriptionData();
        printLinePadding();
        printLineMessage("########## 4) Generates 2 avc notifications");
        sleep(4);
        notificationSubscriber.generateNotification();
        sleep(4);
        notificationSubscriber.generateNotification();
        sleep(4);
        printLinePadding();
        printLineMessage("########## 5) Listening indefinitely for notifications");
        notificationSubscriber.printContainmentQuery();
        notificationSubscriber.printMO("ManagedElement=1,ENodeBFunction=1,RbsConfiguration=1");

        // while (true) {
        // ;
        // }
    }

    private static void printTestClientDescription() {
        printLinePadding();
        printLineMessage("########## This test client verifies basic connectivity between the MSCM and netsim node. The client performs the following checks:");
        printLineMessage("########## 1) Verifies that the node can be reached");
        printLineMessage("########## 2) Subscribes to the node");
        printLineMessage("########## 3) Get some basic node details");
        printLineMessage("########## 4) Generates 2 avc notifications");
        printLineMessage("########## 5) Listening indefinitely for notifications");
        printLineMessage("########## 6) Unsubscribes to a node (if the process is killed or ctrl+c)");
        printLineMessage("########## NOTE: THIS CLIENT SHOULD ONLY BE USED AGAINST NETSIM IN A TEST ENVIRONMENT ");
        printLinePadding();
    }

    private static void printLinePadding() {
        System.out.println("###########################################################################################################");
        System.out.println("###########################################################################################################");
    }

    private static void printLineMessage(final String message) {
        System.out.println(message);
    }

    private static void sleep(final int delayInSecs) {
        try {
            Thread.currentThread();
            Thread.sleep(1000 * delayInSecs);
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void shutdown() {
        printLinePadding();
        printLineMessage("########## 6) Unsubscribes to a node (if the process is killed or ctrl+c)");
        final boolean subStatus = notificationSubscriber.isNodeSubscribed();
        if (!subStatus) {
            printLineMessage("The node is unsubscribed ");
        }
        else {
            printLineMessage("The node is currently subscribed. Going to unsubscribe now.");
            notificationSubscriber.removeSubscription();
            sleep(4);
        }

        System.out.println(notificationSubscriber.isNodeSubscribed() ? "The node is still subscribed" : "The node is unsubscribed.");

        printLinePadding();
        printLineMessage("Test client is finished");
        printLinePadding();
    }

}
