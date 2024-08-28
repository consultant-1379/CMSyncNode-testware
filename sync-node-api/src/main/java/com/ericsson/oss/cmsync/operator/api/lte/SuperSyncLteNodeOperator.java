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
 * This interface provides methods which clients can use to synch LTE network
 * elements in an ENM system. It also provides methods that can be used to help
 * verify whether or not the network element was successfully synched.
 * <p>
 * The implementation of this interface will use the CMEditor REST end-point to
 * perform the functionality provided by a lot of the methods. The ENM system
 * provides access to this end-point through a JBOSS container or the Apache
 * server. The preferred option should be to use Apache as it avoids the need
 * for tunnels.
 * <p>
 * The {@code setHttpTool} method must be the first method called prior to using
 * any other functionality provided by this operator. This is required to set
 * the tool to use to contact the REST end-point. Currently the only tool
 * supported is TAFs {@link HttpTool}. The {@link LauncherOperator} can be used
 * to login and generate the {@code HttpTool} instance.
 * <p>
 * Its possible to create a {@code HttpTool} instance that can be used to
 * execute directly against a JBOSS container. However, you must ensure that
 * tunneling is enabled via TAF properties to allow the requests to succeed as
 * the JBOSS containers use private ipAddresses. The peer servers SC1, and SC2
 * have public ipAddresses and can be used for this purpose. However, be aware
 * that tunneling consumes resources on the peer (~5% of CPU per tunnel
 * created).
 * <p>
 * The Apache server running on the peers can be accessed directly without the
 * need for tunneling.
 * </p>
 * 
 * TODO - Temporary operator which will be removed once we have a SUT with a
 * populated database or an operator that is provided by synch node team.
 * 
 * @See http://jira-oss.lmera.ericsson.se/browse/TORF-20426
 */
public interface SuperSyncLteNodeOperator extends BaseOperator {

	/**
	 * Sets the {@code HttpTool} instance to be used by the operator.
	 * 
	 * @param tool
	 *            Instance of tool {@link HttpTool}.
	 */
	void setHttpTool(HttpTool tool);

	/**
	 * Creates the CmFunction MO which is required to trigger the sync action on
	 * the node.
	 * 
	 * <p>
	 * Note: This method is deprecated because this does not belong here. The
	 * creation of CmFucntion ManagedObject will be automated as part of
	 * TORF-24718
	 * <p>
	 * 
	 * @param networkElementId
	 *            The Id of the NetworkElement MO to create i.e. For
	 *            NetworkElement=LTE01ERBS1 the id is LTE01ERBS1.
	 * 
	 * @throws HttpToolNotSetException
	 * 
	 * @throws CmMediationOperatorException
	 *             Checked exception that will be thrown if the operation is
	 *             unsuccessful
	 */
	@Deprecated
	void createCmFunctionMo(String networkElementId)
			throws CmMediationOperatorException;

	/**
	 * This method will trigger a synch of an LTE network element. The
	 * CmFunction MO must exist on the node for this method call to succeed.
	 * 
	 * @param networkElementId
	 *            The Id of the NetworkElement MO to create i.e. For
	 *            NetworkElement=LTE01ERBS1 the id is LTE01ERBS1.
	 * 
	 * @throws HttpToolNotSetException
	 */
	void syncSingleNode(String networkElementId);

	/**
	 * This method will verify that the synch was successful by checking the
	 * synch status attribute on the meContext Managed Object.
	 * 
	 * @param networkElementId
	 *            - The Id of the NetworkElement MO to create i.e. For
	 *            NetworkElement=LTE01ERBS1 the id is LTE01ERBS1.
	 * 
	 * @return boolean value indicating the success or failure of the operation.
	 * 
	 * @throws HttpToolNotSetException
	 */
	boolean isSyncComplete(String networkElementId);

	/**
	 * This method will disable AVCs from being sent from the network element to
	 * the SUT.
	 * 
	 * @param networkElementId
	 *            The Id of the NetworkElement MO to create i.e. For
	 *            NetworkElement=LTE01ERBS1 the id is LTE01ERBS1.
	 * 
	 * @throws HttpToolNotSetException
	 */
	void disableNotificationsFromNode(String networkElementId);
}