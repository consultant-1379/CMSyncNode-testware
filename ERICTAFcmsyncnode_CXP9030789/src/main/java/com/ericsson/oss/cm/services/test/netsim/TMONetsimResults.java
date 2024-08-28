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

public class TMONetsimResults {

    private TestManagedObject testManagedObject;

    public TestManagedObject getTestManagedObject() {
        return testManagedObject;
    }

    public void setTestManagedObject(TestManagedObject tmo) {
        testManagedObject = tmo;

    }

    public TestManagedObject getParent(final String parent) {
        TestManagedObject parentTMO = null;
        if (testManagedObject.getFdn().equals(parent)) {
            parentTMO = testManagedObject;
        } else {
            parentTMO = getParent(testManagedObject, parent);
        }

        return parentTMO;

    }


    private TestManagedObject getParent(final TestManagedObject child, final String parent) {
        List<TestManagedObject> list = child.getChildren();
        TestManagedObject parentTMO = null;
        for (TestManagedObject tm : list) {
            if (tm.getFdn().equals(parent)) {
                parentTMO = tm;
            } else {
                parentTMO = getParent(tm, parent);
            }
        }

        return parentTMO;

    }

}

class TestManagedObjectComparator implements Comparator<TestManagedObject> {

    public int compare(TestManagedObject o1, TestManagedObject o2) {
        return o1.getFdn().compareTo(o2.getFdn());
    }

}
