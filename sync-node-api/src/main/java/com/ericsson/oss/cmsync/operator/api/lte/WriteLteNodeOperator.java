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

import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.oss.cmsync.operator.api.common.BaseOperator;
import com.ericsson.oss.cmsync.operator.api.exception.CmMediationOperatorException;
import com.ericsson.oss.cmsync.operator.api.exception.HttpToolNotSetException;


/**
 * This interface provides methods which clients can use to perform CUDA (Create/Update/Delete/Action) operations towards ERBS network elements to a
 * TOR system. It also provides methods that can be used to help verify whether or not the operation succeeded.
 * <p>
 * The implementation of this interface will use the CMEditor REST end-point to perform the functionality provided by a lot of the methods. The ENM
 * system provides access to this end-point through a JBOSS container or the Apache server. The preferred option should be to supply the DNS name of
 * the Apache server in the methods below as it avoids the need for tunnels.
 * <p>
 * The {@code setHttpTool} method must be the first method called prior to using any other functionality provided by this operator. This is required
 * to set the tool to use to contact the REST end-point. Currently the only tool supported is TAFs {@link HttpTool}. The {@link LauncherOperator} can
 * be used to login and generate the {@code HttpTool} instance.
 * <p>
 * Its possible to create a {@code HttpTool} instance that can be used to execute directly against a JBOSS container. However, you must ensure that
 * tunneling is enabled via TAF properties to allow the requests to succeed as the JBOSS containers use private ipAddresses. The peer servers SC1, and
 * SC2 have public ipAddresses and can be used for this purpose. However, be aware that tunneling consumes resources on the peer (~5% of CPU per
 * tunnel created).
 * <p>
 * The Apache server running on the peers can be accessed directly without the need for tunneling.
 * </p>
 */
public interface WriteLteNodeOperator extends BaseOperator {

    /**
     * Sets the {@code HttpTool} instance to be used by the operator.
     *
     * @param tool
     *            Instance of tool {@link HttpTool}.
     */
    void setHttpTool(HttpTool tool);

    /**
     * Creates MOs on the LTE node i.e. within the 'ERBS_NODE_MODEL' namespace.
     * 
     * Note: This is not to be used only to create an MO in the DPS (ERBS NAME_SPACE only), because this method will always result in the creation of
     * an MO on the node itself (Netsim node).
     * 
     * @param moFdn
     *            Full Distinguished name of the MO to delete
     * 
     * @param attributes
     *            Comma separated list of attributes and their values (e.g. attr1=1,attr2="test")
     * 
     * @param version
     *            Model version of the node being updated (e.g. E.1.49).
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void createManagedObject(String moFdn, String attributes, LteMIMVersion version) throws CmMediationOperatorException;

    /**
     * Updates an MO on the SUT.
     * 
     * Note: This is not to be used only to update an MO in the DPS (ERBS NAME_SPACE only). This method will always result in the update of an MO on
     * the node itself (Netsim node)."
     * 
     * @param moFdn
     *            Full Distinguished name of the MO to update
     * 
     * @param attributes
     *            Comma separated list of attributes to be updated and their values (e.g. attr1=1,attr2="test")
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void updateManagedObject(String moFdn, String attributes) throws CmMediationOperatorException;

    /**
     * Updates an attribute(s) on all MOs of the specified type.
     * 
     * Note: This is not to be used only to update all attributes of all MOs in the DPS (ERBS NAME_SPACE only). This method will always result in the
     * update of MOs on the node itself (Netsim node).
     * 
     * @param filter
     *            Determines the node from which the MOs should be determined (e.g. for all nodes with MeContextId starting with 'LTE01' the filter
     *            provided should be 'LTE01'). The method will use CmEditors set functionality with a wildcard (*) if an empty string or null is
     *            provided for the filter.
     * 
     * @param moType
     *            FDN to be updated
     * 
     * @param attributes
     *            Comma separated list of attributes to be updated and their values (e.g. attr1=1,attr2="test")
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void updateManagedObjects(String filter, String moType, String attributes) throws CmMediationOperatorException;

    /**
     * Executes the action on the MO on the Node.
     * 
     * @param fdn
     *            The fdn of the MO to perform the action on
     * 
     * @param action
     *            The name of the action to perform e.g. action <strong>install</strong>
     * 
     * @param attributes
     *            Any attributes required by the action
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void executeActionOnMo(String fdn, String action, String attributes) throws CmMediationOperatorException;
    
}
