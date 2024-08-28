package com.ericsson.oss.cmsync.impl.common;

import static com.ericsson.oss.cmsync.operator.api.common.constants.Constants.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.cmsync.util.CmEditorCommandUtil;
import com.ericsson.oss.cmsync.operator.api.common.BaseOperator;
import com.ericsson.oss.cmsync.operator.api.exception.CmMediationOperatorException;
import com.ericsson.oss.cmsync.operator.api.exception.HttpToolNotSetException;
import com.ericsson.oss.services.cm.rest.CmEditorRestOperator;
import com.ericsson.oss.services.scriptengine.spi.dtos.ResponseDto;
import com.google.common.collect.Lists;

/**
 * Implementation of a common CM base operator class.
 * 
 * @See <code>com.ericsson.oss.mediation.cm.operator.api.common.BaseOperator</code>
 */
public abstract class BaseOperatorImpl implements BaseOperator {

	private final Set<String> deleteFdnList = new LinkedHashSet<String>();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BaseOperatorImpl.class);
	protected boolean isHttpToolSet = false;

	@Inject
	protected CmEditorRestOperator cmEditorOperator;

	@Override
	public int getNumObjects(final String type, final String namespace) {

		checkHttpToolIsSet();
		final String command = CmEditorCommandUtil.getMoOrPoListFromAllNes(
				type, namespace);
		final String response = executeRestCall(command);
		return getNumInstancesFromHttpResponse(response);
	}

	protected int getNumInstancesFromHttpResponse(final String response) {
		final String regex = HTTP_REGEX;
		int numPOs = 0;
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(response);

		while (matcher.find()) {
			final String result = matcher.group();
			final String[] parsedResult = result.split(SPACE);
			numPOs = Integer.parseInt(parsedResult[0]);
		}
		return numPOs;
	}

	@Override
	public Set<String> getMoFdns(final String moType, final String namespace) {

		checkHttpToolIsSet();

		final String command = CmEditorCommandUtil.getMoOrPoListFromAllNes(
				moType, namespace);
		final String response = executeRestCall(command);

		return extractListFDNsFromHttpRsp(response);
	}

	@Override
	public Set<String> getMoFdnsFromSpecifiedNode(final String nodeName,
			final String moType) {

		checkHttpToolIsSet();

		final String command = CmEditorCommandUtil.getMoOrPoListFromSpecificNe(
				nodeName, moType);
		final String response = executeRestCall(command);
		LOGGER.debug(LOG_RESPONSE, response);
		return extractListFDNsFromHttpRsp(response);
	}

	@Override
	public Set<String> getMoFdnsFromSpecifiedNode(final String nodeName,
			final List<String> moTypes) {
		checkHttpToolIsSet();

		final String command = CmEditorCommandUtil.getMoOrPoListFromSpecificNe(
				nodeName, moTypes);
		final String response = executeRestCall(command);
		LOGGER.debug(LOG_RESPONSE, response);
		return extractListFDNsFromHttpRsp(response);
	}

	private Set<String> extractListFDNsFromHttpRsp(final String response) {
		final Set<String> fdns = new HashSet<String>();
		final String regex = "MeContext=\\S+";
		final String parsedBody = response.replace("\"", SPACE);
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(parsedBody);

		while (matcher.find()) {
			fdns.add(matcher.group());
		}
		return fdns;
	}

	@Override
	public String getAttribute(final String fdn, final String attributeName) {
		return getAttributes(fdn, attributeName).get(attributeName);
	}

	@Override
	public String getAttribute(final String nodeName, final String moType,
			final String moId, final String attributeName) {

		checkHttpToolIsSet();

		final String cmd = CmEditorCommandUtil.getAttribute(nodeName, moType,
				moId, attributeName);
		final ResponseDto responseDto = cmEditorOperator.executeCommand(cmd);

		final Map<String, Map<String, String>> attributesPerFdn = cmEditorOperator
				.getAttributesPerFdn(responseDto);
		final Map<String, String> attributes = attributesPerFdn.values()
				.iterator().next();
		return attributes.get(attributeName);
	}

	@Override
	public Map<String, String> getAttributes(final String fdn,
			final String... attributeNames) {
		checkHttpToolIsSet();

		final Map<String, String> result = new HashMap<>(attributeNames.length);
		final String command = CmEditorCommandUtil.getFdn(fdn);
		final String response = executeRestCall(command);
		for (String attributeName : attributeNames) {
			final String searchTermForBeiningOfAttributeValue = attributeName
					+ SPACE + COLON + SPACE;
			final int beginIndex = response
					.indexOf(searchTermForBeiningOfAttributeValue)
					+ searchTermForBeiningOfAttributeValue.length();
			final int endIndex = response.indexOf(DOUBLE_QUOTE, beginIndex);
			result.put(attributeName, response.substring(beginIndex, endIndex));
		}
		return result;

	}

	@Override
	public void markMoForDeletion(final String fdn) {
		deleteFdnList.add(fdn);
	}

	@Override
	public void deleteMarkedMos() throws CmMediationOperatorException {

		checkHttpToolIsSet();

		final List<String> list = new LinkedList<String>(deleteFdnList);

		for (final String fdn : Lists.reverse(list)) {
			deleteManagedObject(fdn);
			deleteFdnList.remove(fdn);
		}
	}

	@Override
	public void deleteManagedObject(final String fdn)
			throws CmMediationOperatorException {
		checkHttpToolIsSet();
		final String command = CmEditorCommandUtil.deleteMo(fdn);
		final String response = executeRestCall(command);
		checkResponse(response, EXPECTED_MO_DELETE_RSP);
	}

	protected void createManagedObject(final String fdn,
			final String attributes, final String model, final String version)
			throws CmMediationOperatorException {
		final String command = CmEditorCommandUtil.createMo(fdn, attributes,
				model, version);
		final String response = executeRestCall(command);
		checkResponse(response, EXPECTED_MO_CREATE_OR_UPDATE_RSP);
	}

	/*
	 * Executes the supplied command against the CM Editor REST service using
	 * the CM Editor Operator.
	 * 
	 * @param command - The CM Editor command to be sent
	 * 
	 * @return - String containing the response received in the HTTP response
	 * body.
	 */
	protected String executeRestCall(final String command) {
		LOGGER.info("Sending command to cmEditor: [ {} ]", command);
		return cmEditorOperator.sendRequest(command);
	}

	/*
	 * Parses the body of the <code>HttpResponse</code> and confirms that it
	 * contains the expected response string.
	 * 
	 * @param response - The Http response received from the REST service.
	 * 
	 * @param expectedRspBody - expected response
	 */
	protected void checkResponse(final String response,
			final String expectedRspBody) throws CmMediationOperatorException {
		LOGGER.debug(LOG_RESPONSE, response);
		if (!response.contains(expectedRspBody))
			throw new CmMediationOperatorException(
					"Unexpected response received from the CmEditor: ["
							+ response + "]");
	}

	protected void checkHttpToolIsSet() {
		if (!isHttpToolSet) {
			throw new HttpToolNotSetException();
		}
	}
}