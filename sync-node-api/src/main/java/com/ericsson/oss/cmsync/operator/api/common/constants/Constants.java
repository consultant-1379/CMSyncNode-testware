package com.ericsson.oss.cmsync.operator.api.common.constants;

import java.util.regex.Pattern;

public abstract class Constants {

    // CM Editor operations
    public static final String CMEDIT = "cmedit ";
    public static final String GET = "get";
    public static final String SET = "set";
    public static final String DELETE = "delete";
    public static final String CREATE = "create";
    public static final String ACTION = "action";
    public static final String ACTIVE_STATE_TRUE = "active=true";
    public static final String ACTIVE_STATE_FALSE = "active=false";
    // CM Editor common attributes
    public static final String NAMESPACE_TAG = "-ns=";
    public static final String VERSION_TAG = "-version=";

    // CM Editor responses
    public static final String EXPECTED_MO_CREATE_OR_UPDATE_RSP = "1 instance(s) updated";
    public static final String EXPECTED_SUPERVISION_FUNCTION_MO_CREATE_OR_UPDATE_RSP = "9 instance(s) updated";
    public static final String EXPECTED_SINGLE_MO_GET_RSP = "1 instance(s)";
    public static final String EXPECTED_MO_DELETE_RSP = "instance(s) deleted";
    public static final String EXPECTED_ACTION_RSP = "1 instance(s)";

    // ENM server properties
    public static final String SC1_PROPERTY_NAME = "sc1";
    public static final String SC2_PROPERTY_NAME = "sc2";
    public static final String HTTPD_SU0_PROPERTY_NAME = "httpd_su0";
    public static final String CMSERV_SU0_PROPERTY_NAME = "cmserv_su0";

    // ENM JBOSS log files
    public static final String MSCM_SU_0_LOG = "/var/ericsson/log/jboss/MSCM_su_0_jee_cfg/server.log";
    public static final String MSCM_SU_1_LOG = "/var/ericsson/log/jboss/MSCM_su_1_jee_cfg/server.log";

    /*
     * Regular Expressions for parsing String containing attribute retrieved from Netsim or provided by CLI
     */

    //This regex is required to match the member attribute for struct and list attributes. This will match any primitive type key and value pairs.
    private static final String COMMON_REGEX = "([a-zA-Z0-9]+=[a-zA-Z0-9]+)";
    public static final Pattern COMMON_PATTERN = Pattern.compile(COMMON_REGEX);

    private static final String PRIMITIVE_REGEX = "([a-zA-Z0-9]+)=([a-zA-Z0-9-]+)";
    public static final Pattern PRIMITIVE_PATTERN = Pattern.compile(PRIMITIVE_REGEX);

    private static final String LIST_OF_PRIMITIVE_REGEX = "([a-zA-Z0-9]+)=\\[([a-zA-Z0-9,-]+)\\]";
    public static final Pattern LIST_OF_PRIMITIVE_PATTERN = Pattern.compile(LIST_OF_PRIMITIVE_REGEX);
    public static final String LIST_OF_STRUCT_REGEX = "([a-zA-Z0-9]+)=(\\[\\(.*?\\)\\])";
    public static final Pattern LIST_OF_STRUCT_PATTERN = Pattern.compile(LIST_OF_STRUCT_REGEX);

    public static final String STRUCT_REGEX = "([a-zA-Z0-9]+)=(\\(.*?\\))";
    public static final Pattern STRUCT_PATTERN = Pattern.compile(STRUCT_REGEX);
    private static final String STRUCT_LIST_REGEX = "([a-zA-Z0-9]+=\\[[a-zA-Z0-9,-]+)\\]";
    public static final Pattern STRUCT_LIST_PATTERN = Pattern.compile(STRUCT_LIST_REGEX);
    private static final String NETSIM_SIMPLE_REGEX = "([a-zA-Z0-9]+)=([a-zA-Z0-9,-]+)";
    public static final Pattern NETSIM_SIMPLE_PATTERN = Pattern.compile(NETSIM_SIMPLE_REGEX);
    private static final String NETSIM_LIST_OF_STRUCT_REGEX = "([a-zA-Z0-9]+)=(\\[\\{[a-zA-z0-9\\[\\]\\{\\}\\s\\:\",-.]+)\\]";
    public static final Pattern NETSIM_LIST_OF_STRUCT_PATTERN = Pattern.compile(NETSIM_LIST_OF_STRUCT_REGEX);
    private static final String NETSIM_STRUCT_FINDER_REGEX = "([a-zA-Z0-9]+)=,";
    public static final Pattern NETSIM_STRUCT_PATTERN = Pattern.compile(NETSIM_STRUCT_FINDER_REGEX);

    public static final String HTTP_REGEX= "\\d+ instance";

    // Misc
    public static final String SPACE = " ";
    public static final String EMPTY_STRING = "";
    public static final String COMMA = ",";
    public static final String DOUBLE_QUOTE= "\"";
    public static final String COLON = ":";
    public static final String SEMI_COLON = ";";
    public static final String WILDCARD = "*";
    public static final String DOT = ".";
    public static final String LEFT_ROUND_BRACKET = "(";
    public static final String RIGHT_ROUND_BRACKET = ")";
    public static final String EQUALS = "=";
    public static final String NULL = "null";

    // Logging
    public static final String LOG_RESPONSE = "Response received from cmEditor: [ {} ]";

}
