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
package com.ericsson.oss.cmsync.util;

import static com.ericsson.oss.cmsync.operator.api.common.constants.Constants.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * Utility class which provides methods to construct CMEditor commands.
 */
public class CmEditorCommandUtil {

	/*
	 * This method will construct a command that can be used to update a Managed
	 * Object.
	 * 
	 * e.g. cmedit set
	 * MeContext=LTE100ERBS00001,ManagedElement=1,ENodeBFunction=1
	 * userLabel=test
	 * 
	 * @param fdn - Full distinguished name of the MO to update
	 * 
	 * @param attributes - Comma separated list of the attributes to update.
	 * 
	 * @return - CM Editor command.
	 */
	public static String updateMO(final String fdn, final String attributes) {

		final StringBuilder command = new StringBuilder(CMEDIT);
		command.append(SET).append(SPACE).append(fdn).append(SPACE)
				.append(attributes);

		return command.toString();
	}

	/*
	 * This method will construct a command that can be used to update multiple
	 * Managed Objects.
	 * 
	 * e.g. cmedit set * EUtranCellFDD userLabel=test,tac=2
	 * 
	 * @param filter - All nodes will be searched unless a filter is provided
	 * (e.g. LTE01 would search all nodes whose RDN starts with LTE01)
	 * 
	 * @param moType - MO type to update
	 * 
	 * @param attributes - Comma separated list of the attributes to update.
	 * 
	 * @return - CM Editor command.
	 */
	public static String updateMultipleMos(final String filter,
			final String moType, final String attributes) {

		final StringBuilder command = new StringBuilder(CMEDIT);
		command.append(SET).append(SPACE).append(filter).append(WILDCARD)
				.append(SPACE).append(moType).append(SPACE).append(attributes);

		return command.toString();
	}

	/*
	 * This method will construct a command that can be used to create a Managed
	 * Object.
	 * 
	 * e.g. cmedit create MeContext=LTE100ERBS00001 MeContextId=1 -ns=OSS_TOP
	 * -version=1.1.0
	 * 
	 * @param fdn - Full distinguished name of the MO to create (e.g.
	 * MeContext=LTE100ERBS00001).
	 * 
	 * @param attributes - Comma separated list of the attributes for the MO
	 * (e.g. MeContextId=1). Can be null if the MO to be created has no
	 * attributes.
	 * 
	 * @param namesapce - Namespace of the model the MO belongs to (e.g.
	 * OSS_TOP)
	 * 
	 * @param version - Version of the model that the MO belongs to (e.g. 1.1.0)
	 * 
	 * @return - CM Editor command.
	 */
	public static String createMo(final String fdn, final String attributes,
			final String namespace, final String version) {

		final StringBuilder command = new StringBuilder(CMEDIT);
		command.append(CREATE).append(SPACE).append(fdn).append(SPACE);
		if (attributes != null) {
			command.append(attributes).append(SPACE);
		}
		command.append(NAMESPACE_TAG).append(namespace).append(SPACE)
				.append(VERSION_TAG).append(version);
		return command.toString();
	}

	/*
	 * This method will construct a command that can be used to retrieve a list
	 * of all matching MOs or POs from a network element of a specific name.
	 * 
	 * e.g. cmedit get LTE100ERBS00001 ENodeBFunction -ns=ERBS_NODE_MODEL
	 * 
	 * @param neName - Name of the network element to search i.e. the relative
	 * distinguished name (RDN) from the network elements MeContext FDN
	 * (LTE100ERBS00001 in example above).
	 * 
	 * @param target - Name of object type (MO or PO) to search for
	 * (ENodeBFunction in example above).
	 * 
	 * @return - CM Editor command.
	 */
	public static String getMoOrPoListFromSpecificNe(final String neName,
			final String target) {

		final StringBuilder command = new StringBuilder(CMEDIT);
		command.append(GET).append(SPACE).append(neName).append(SPACE)
				.append(target).append(SPACE);

		return command.toString();
	}

	/*
	 * This method will construct a command that can be used to retrieve a list
	 * of all matching MOs or POs from a network element of a specific name.
	 * 
	 * e.g. cmedit get LTE100ERBS00001 ENodeBFunction;Sctp
	 * 
	 * @param neName - Name of the network element to search i.e. the relative
	 * distinguished name (RDN) from the network elements MeContext FDN
	 * (LTE100ERBS00001 in example above).
	 * 
	 * @param target - Names of object types (MO or PO) to search for
	 * (ENodeBFunction and Sctp in example above).
	 * 
	 * @return - CM Editor command.
	 */
	public static String getMoOrPoListFromSpecificNe(final String neName,
			final List<String> targets) {

		final StringBuilder command = new StringBuilder(CMEDIT);
		command.append(GET).append(SPACE).append(neName);

		String prefix = SPACE;
		for (final String target : targets) {
			command.append(prefix);
			prefix = SEMI_COLON;
			command.append(target);
		}
		return command.toString();
	}

	/*
	 * This method will construct a command that can be used to retrieve a list
	 * of all matching MOs or POs from all network elements in the SUT matching
	 * the specified MO or PO type.
	 * 
	 * e.g. cmedit get * ENodeBFunction -ns=ERBS_NODE_MODEL -version=1.1.419
	 * 
	 * @param target - Name of object type (MO or PO) to search for
	 * (ENodeBFunction in example above).
	 * 
	 * @param namespace - Namespace of the model the MO or PO belongs to.
	 * (ERBS_NODE_MODEL)
	 * 
	 * @return - CM Editor command.
	 */
	public static String getMoOrPoListFromAllNes(final String target,
			final String namespace) {

		final String WILDCARD = "*";
		final StringBuilder command = new StringBuilder(CMEDIT);
		command.append(GET).append(SPACE).append(WILDCARD).append(SPACE)
				.append(target).append(SPACE).append(NAMESPACE_TAG)
				.append(namespace);

		return command.toString();
	}

	/*
	 * This method will construct a command that can be used to perform a GET
	 * request for a specific FDN.
	 * 
	 * e.g. cmedit get
	 * MeContext=LTE100ERBS00001,ManagedElement=1,ENodeBFunction=1
	 * 
	 * @param fdn - Full distinguished name of the object of interest.
	 * 
	 * @return - CM Editor command.
	 */
	public static String getFdn(final String fdn) {

		final StringBuilder command = new StringBuilder(CMEDIT);
		command.append(GET).append(SPACE).append(fdn);

		return command.toString();
	}

	/*
	 * This method will construct a command that can be used to perform a DELETE
	 * request for a specific FDN and its children.
	 * 
	 * e.g. cmedit delete
	 * MeContext=LTE100ERBS00001,ManagedElement=1,ENodeBFunction=1 -ALL
	 * 
	 * @param fdn - Full distinguished name of the Managed Object to be deleted.
	 * 
	 * @return - CM Editor command.
	 */
	public static String deleteMo(final String fdn) {

		final String DELETE_ALL = "-ALL";
		final StringBuilder command = new StringBuilder(CMEDIT);
		command.append(DELETE).append(SPACE).append(fdn).append(SPACE)
				.append(DELETE_ALL);

		return command.toString();
	}

	/*
	 * Creates a action command e.g. cmedit action fdn attributes
	 * 
	 * @param fdn - Full distinguished name of the object on which the action is
	 * to be performed.
	 * 
	 * @param action - Name of action to perform on the MO
	 * 
	 * @param attributes - attributes required by the action
	 * 
	 * @return
	 */
	public static String performAction(final String fdn, final String action,
			final Map<String, String> attributes) {
		// handle command
		final StringBuilder command = new StringBuilder();
		command.append(CMEDIT);
		command.append(ACTION);
		command.append(SPACE);
		command.append(fdn);
		command.append(SPACE);
		command.append(action);
		// handle attributes
		if (attributes != null && !attributes.isEmpty()) {
			command.append(DOT);
			command.append(LEFT_ROUND_BRACKET);
			final Iterator<String> keyIter = attributes.keySet().iterator();
			while (keyIter.hasNext()) {
				final String key = keyIter.next();
				command.append(key);
				command.append(EQUALS);
				command.append(attributes.get(key));
				if (keyIter.hasNext()) {
					command.append(COMMA);
				}
			}
			command.append(RIGHT_ROUND_BRACKET);
		}
		return command.toString();
	}

	/**
	 * Creates a GET request for an attribute with: <li>A specific network
	 * element name <li>A specific MO type <li>A specific MO ID.<br>
	 * 
	 * Example output: 'cmedit get RIV-LTE09ERBS00007
	 * Sctp.(SctpId==syncTestId,nPercentage)'
	 * 
	 * @param nodeName
	 *            Specific network element name
	 * @param moType
	 *            Specific MO type
	 * @param moId
	 *            Specific MO ID
	 * @param attributeName
	 *            Attribute requested
	 * @return CM Editor command
	 */
	public static String getAttribute(final String neName, final String moType,
			final String moId, final String attribute) {

		final StringBuilder command = new StringBuilder(CMEDIT);
		command.append(GET).append(SPACE);
		command.append(neName).append(SPACE);
		command.append(moType).append(DOT);
		command.append(LEFT_ROUND_BRACKET);
		command.append(moType).append("Id").append(EQUALS).append(EQUALS)
				.append(moId).append(COMMA).append(attribute);
		command.append(RIGHT_ROUND_BRACKET);

		return command.toString();
	}
}