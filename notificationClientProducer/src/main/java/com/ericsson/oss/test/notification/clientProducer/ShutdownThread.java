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

public class ShutdownThread extends Thread {

    private IShutdownThreadParent mShutdownThreadParent;

    public ShutdownThread(final IShutdownThreadParent mShutdownThreadParent) {
        this.mShutdownThreadParent = mShutdownThreadParent;
    }

    @Override
    public void run() {
        this.mShutdownThreadParent.shutdown();
    }
}