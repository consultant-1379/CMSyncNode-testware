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
package com.ericsson.oss.cmsync.impl.lte;

import static com.ericsson.oss.cmsync.operator.api.common.constants.Constants.ACTIVE_STATE_FALSE;
import static com.ericsson.oss.cmsync.operator.api.common.constants.Constants.ACTIVE_STATE_TRUE;
import static com.ericsson.oss.cmsync.operator.api.common.constants.Constants.COMMA;
import static com.ericsson.oss.cmsync.operator.api.common.constants.Constants.EQUALS;
import static com.ericsson.oss.cmsync.operator.api.lte.constants.LteConstants.NETWORKELEMENT;
import static com.ericsson.oss.cmsync.operator.api.lte.constants.LteConstants.OSS_NE_CM_DEF_MODEL_TYPE;
import static com.ericsson.oss.cmsync.operator.api.lte.constants.LteConstants.OSS_NE_CM_DEF_MODEL_VERSION;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.oss.cmsync.impl.common.BaseOperatorImpl;
import com.ericsson.oss.cmsync.util.CmEditorCommandUtil;
import com.ericsson.oss.cmsync.operator.api.exception.CmMediationOperatorException;
import com.ericsson.oss.cmsync.operator.api.lte.SuperSyncLteNodeOperator;

/**
 * Implementation of the {@link SyncLteNodeOperator} interface.
 * 
 */
@Operator(context = Context.REST)
public class SuperSyncLteNodeOperatorImpl extends BaseOperatorImpl implements
		SuperSyncLteNodeOperator {

	private final static String SYNC_STATUS = "syncStatus";
	private final static String SYNCHRONIZED = "SYNCHRONIZED";
	private final static String CMFUNCTION_ATTRS = "CmFunctionId=1";
	private final static String CMFUNCTION_RDN = "CmFunction=1";
	private final static String CM_NODE_HEARTBEAT_SUPERVISION_RDN = "CmNodeHeartbeatSupervision=1";

	@Override
	public void setHttpTool(final HttpTool tool) {
		cmEditorOperator.setTool(tool);
		isHttpToolSet = true;
	}

	@Override
	@Deprecated
	public void createCmFunctionMo(final String networkElementId)
			throws CmMediationOperatorException {
		checkHttpToolIsSet();
		final String cmFunctionFdn = createCmFunctionFdn(networkElementId);
		createManagedObject(cmFunctionFdn, CMFUNCTION_ATTRS,
				OSS_NE_CM_DEF_MODEL_TYPE, OSS_NE_CM_DEF_MODEL_VERSION);
	}

	@Override
	public void syncSingleNode(final String networkElementId) {
		checkHttpToolIsSet();
		final String cmNodeHeartbeatSupervisionFdn = getCmNodeHeartbeatSupervisionFdn(networkElementId);
		final String command = CmEditorCommandUtil.updateMO(
				cmNodeHeartbeatSupervisionFdn, ACTIVE_STATE_TRUE);
		executeRestCall(command);
	}

	@Override
	public void disableNotificationsFromNode(final String networkElementId) {
		checkHttpToolIsSet();
		final String cmNodeHeartbeatSupervisionFdn = getCmNodeHeartbeatSupervisionFdn(networkElementId);
		final String command = CmEditorCommandUtil.updateMO(
				cmNodeHeartbeatSupervisionFdn, ACTIVE_STATE_FALSE);
		executeRestCall(command);
	}

	@Override
	public boolean isSyncComplete(final String networkElementId) {
		checkHttpToolIsSet();
		final String cmFunctionFdn = createCmFunctionFdn(networkElementId);
		return getAttribute(cmFunctionFdn, SYNC_STATUS).equals(SYNCHRONIZED);
	}

	private String getCmNodeHeartbeatSupervisionFdn(
			final String networkElementId) {
		return NETWORKELEMENT + EQUALS + networkElementId + COMMA
				+ CM_NODE_HEARTBEAT_SUPERVISION_RDN;
	}

	private String createCmFunctionFdn(final String networkElementId) {
		return NETWORKELEMENT + EQUALS + networkElementId + COMMA
				+ CMFUNCTION_RDN;
	}
}