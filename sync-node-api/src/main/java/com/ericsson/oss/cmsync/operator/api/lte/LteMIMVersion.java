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
package com.ericsson.oss.cmsync.operator.api.lte;

/**
 * This Enum will be used to provide the supported MIM versions.
 */
public enum LteMIMVersion {
    D1189("D.1.189"), D144("D.1.44"), E1120("E.1.120"), E149("E.1.49");

    private final String text;

    private LteMIMVersion(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
