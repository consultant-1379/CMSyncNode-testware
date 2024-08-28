package com.ericsson.oss.cmsync.operator.api.lte.constants;

import static com.ericsson.oss.cmsync.operator.api.common.constants.Constants.EQUALS;

public class LteConstants {

    private LteConstants() {
    }

    // MO Model details
    public static final String ERBS_NODE_MODEL = "ERBS_NODE_MODEL";

    public static final String OSS_TOP_MODEL_TYPE = "OSS_TOP";
    public static final String OSS_TOP_MODEL_VERSION = "2.0.0";

    public static final String OSS_NE_DEF_MODEL_TYPE = "OSS_NE_DEF";
    public static final String OSS_NE_DEF_MODEL_VERSION = "1.0.0";

    public static final String ERBS_NODE_MODEL_TYPE = "ERBS_NODE_MODEL";
    public static final String ERBS_NODE_MODEL_VERSION = "1.0.0";

    public static final String CPPCONNINFO_MODEL_TYPE = "CPP_MED";
    public static final String CPPCONNINFO_MODEL_VERSION = "1.0.0";

    public static final String OSS_NE_CM_DEF_MODEL_TYPE = "OSS_NE_CM_DEF";
    public static final String OSS_NE_CM_DEF_MODEL_VERSION = "1.0.0";

    // MO types used in the operators
    public static final String MECONTEXT = "MeContext";
    public static final String NETWORKELEMENT = "NetworkElement";
    public static final String CPPCONNECTIVITYINFORMATION = "CppConnectivityInformation";

    public static final String CPPCONNECTIVITYINFORMATION_RDN = CPPCONNECTIVITYINFORMATION + EQUALS + "1";
    public static final String NETWORKELEMENTID = "networkElementId";
    public static final String MECONTEXTID = "MeContextId";
    public static final String SUBNETWORK_ATTRS = "SubNetworkId=1";
    public static final String NETWORKELEMENT_ATTRS = ",neType=ERBS,platformType=CPP,ossPrefix=\"";
    public static final String MECONTEXT_ATTRS = ",neType=ERBS,platformType=CPP";
    public static final String CPPCONNECTIVITYINFORMATION_ATTRS = "CppConnectivityInformationId=1,port=80,ipAddress=\"";
}
