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

import java.text.SimpleDateFormat;
import java.util.*;

import javax.transaction.xa.XAException;

import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.BasicConfig.*;
import com.ericsson.nms.umts.ranos.cms.nead.segmentserver.neaccess.cello_p1.idl.ConfigExtended.ConfigurationExtended;
import com.ericsson.oss.itpf.modeling.modelservice.typed.core.DataType;
import com.ericsson.oss.mediation.network.api.util.NodeAttributeModelDTO;
import com.ericsson.oss.mediation.network.converter.outbound.OutboundConverterService;
import com.ericsson.oss.mediation.network.converter.outbound.util.MoIdPairBuilder;

public class NotificationGenerator {

    ConfigurationExtended configurationExtended;
    private Session mociSession;
    private static final Integer SESSION_TIME_OUT = Integer.valueOf(60 * 10);
    private OutboundConverterService outboundConverterService;
    private String globalTxId = "testClietTxId";

    /**
     * @param service
     */
    public NotificationGenerator(final ConfigurationExtended configurationExtended ) {
        this.configurationExtended = configurationExtended;
        this.outboundConverterService = new OutboundConverterService(configurationExtended._orb());
    }

    public void setMoAttribute() {
        try {
            final Map<String, NodeAttributeModelDTO> attributes = populateAttributes();
            final NameValue[] corbaAttributes = outboundConverterService.convertListOf(attributes);
            mociSession = getMOCISession("TEST_CLIENT", SESSION_TIME_OUT, Boolean.FALSE);
            final MOidpair moIdPair = new MoIdPairBuilder().byFdn("MeContext=XXXX,ManagedElement=1");

            configurationExtended.basic_set_MO_attributes(moIdPair, corbaAttributes, mociSession);
            commitSession();
        } catch (final Exception e) {
            rollbackSession();
            e.printStackTrace();
        }
        finally{
            finishSession();
        }

    }


    private Map<String, NodeAttributeModelDTO> populateAttributes() {
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        final String attributeValue = DATE_FORMAT.format(new Date());
        final Map<String, NodeAttributeModelDTO> attributes = new HashMap<String, NodeAttributeModelDTO>();
        final DataType dataType = DataType.STRING;
        final NodeAttributeModelDTO attributeModelDTO = new NodeAttributeModelDTO(dataType, attributeValue);
        attributes.put("userLabel", attributeModelDTO);
        return attributes;
    }

    private Session getMOCISession(final String name, final int timeout, final boolean readOnly) throws ProcessingFailure, TransactionFailure {
        return createMOCISession(name, timeout, readOnly);

    }

    private Session createMOCISession(final String name, final int timeout, final boolean readOnly) throws TransactionFailure, ProcessingFailure {
        final Session session = configurationExtended.create_session(name, timeout);
        if (readOnly) {
            session.rollback();
        }
        return session;
    }

    private void commitSession() throws XAException {
        if (mociSession != null) {
            try {
                mociSession.commit();
            } catch (final Exception e) {
                System.err.println("Was unable to commit the global tx id " + globalTxId + ", " + e.toString());
                throw new XAException(e.getMessage());
            }
        }
    }

    private void rollbackSession() {
        if (mociSession != null) {
            try {
                mociSession.rollback();
            } catch (final Exception e) {
                System.err.println("Was unable to rollback the node transaction " + e.toString());
            }
        }
    }

    private void finishSession() {
        if (mociSession != null) {
            try {
                mociSession.end();
            } catch (final Exception e) {
                System.err.println("Error when tried to finish the session:" + e.getMessage());
            } finally {
                mociSession = null;
            }
        }
    }

}
