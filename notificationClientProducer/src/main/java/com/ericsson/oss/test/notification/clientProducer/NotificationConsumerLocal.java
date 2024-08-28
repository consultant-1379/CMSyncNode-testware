/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.test.notification.clientProducer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.ConfigNotification;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.NotificationConsumerPOA;

public class NotificationConsumerLocal extends NotificationConsumerPOA {

    String host;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    public NotificationConsumerLocal(final String host) {
        this.host = host;
    }

    public void push(final ConfigNotification[] aconfignotification) {

        final Date date = new Date();
        System.out.println(dateFormat.format(date) + " Received the following Notification from node ip address [" + host + "]");
        System.out.println(Arrays.toString(aconfignotification));
    }
}
