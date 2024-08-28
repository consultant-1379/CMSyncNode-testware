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
package com.ericsson.oss.cm.services.test.data.notifications;

import java.util.*;

import com.ericsson.cifwk.taf.annotations.DataSource;
import com.ericsson.oss.cm.services.data.CmSynchConstants;
import com.ericsson.oss.cm.services.test.data.SynchNodeTestData;
import com.ericsson.oss.cm.services.test.data.TargetNodeData;
import com.ericsson.oss.cm.services.test.netsim.notifications.NotificationMORequest;

public class Target1NodeAVCNotificationsData extends TargetNodeData {

    int numberOfNodes = 1;
    int numberOfNotifications = 1;

    //A custom Java Class should be used if you need more control of the test data
    //A single method annotated with @DataSource is required when using a java class as a data source
    @DataSource
    public List<Map<String, Object>> dataSource() {

        List<String> simList = new ArrayList<String>();
        simList.add(CmSynchConstants.NETSIM_NAME);

        SynchNodeTestData synchNodePojo = generateTestData(numberOfNodes, simList, true);
        synchNodePojo.setNotificationRequestData(setNotificationRequest());

        Map<String, Object> data = Collections.<String, Object> singletonMap("Target1NodeAVCNotificationsData", synchNodePojo);
        return Collections.singletonList(data);
    }

    private List<NotificationMORequest> setNotificationRequest() {
        List<NotificationMORequest> notificationRequestList = new ArrayList<NotificationMORequest>();
        for (int i = 1; i <= numberOfNotifications; i++) {
            NotificationMORequest notificationRequest = new NotificationMORequest("ManagedElement=1,TransportNetwork=1", "Sctp", "TAF" + i);
            notificationRequestList.add(notificationRequest);
           
        }
        return notificationRequestList;
    }

}
