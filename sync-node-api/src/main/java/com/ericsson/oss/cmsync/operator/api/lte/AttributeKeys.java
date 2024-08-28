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
 * This Enum will be used to supply keys for the node details when adding a node via the {@link AddRemoveLteNodeOperator} methods.
 * <p>
 * <ul>
 * <li>NEID - key for the 'networkElementId' attribute of the NetworkElement MO.</li>
 * <li>IPADDRESS - key for the network elements ipaddress.</li>
 * <li>OSSPREFIX - key for the 'ossPrefix' attribute of the NetworkElement MO.</li>
 * <ul>
 * </p>
 */
public enum AttributeKeys {
    NEID, IPADDRESS, OSSPREFIX
}
