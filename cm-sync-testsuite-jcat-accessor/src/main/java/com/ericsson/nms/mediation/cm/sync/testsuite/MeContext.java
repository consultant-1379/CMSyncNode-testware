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
package com.ericsson.nms.mediation.cm.sync.testsuite;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.ericsson.oss.itpf.datalayer.dps.exception.general.AlreadyDefinedException;
import com.ericsson.oss.itpf.datalayer.dps.exception.model.ModelConstraintViolationException;
import com.ericsson.oss.itpf.datalayer.dps.exception.model.NotDefinedInModelException;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;

public class MeContext implements ManagedObject, Serializable {

    private String nameSpace;
    private long id;
    private String type;
    private String version;
    private String fdn;
    private short level;
    private String name;

    public MeContext(final String nameSpace, final long id, final String type,
            final String version, final String fdn, final short level,
            final String name) {
        this.nameSpace = nameSpace;
        this.id = id;
        this.type = type;
        this.version = version;
        this.fdn = fdn;
        this.level = level;
        this.name = name;
    }


    @Override
    public String getNamespace() {
        return nameSpace;
    }

    @Override
    public long getPoId() {
        return id;
    }

    @Override
    public String getType() {
        return type;

    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Map<String, Object> getAllAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getAttribute(final String arg0)
            throws NotDefinedInModelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getAttributes(final Collection<String> arg0)
            throws NotDefinedInModelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getAttributes(final String[] arg0)
            throws NotDefinedInModelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(final String arg0, final Object arg1)
            throws NotDefinedInModelException, ModelConstraintViolationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setAttributes(final Map<String, Object> arg0)
            throws NotDefinedInModelException, ModelConstraintViolationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void addAssociation(final String arg0, final PersistenceObject arg1)
            throws ModelConstraintViolationException, NotDefinedInModelException,
            AlreadyDefinedException {
        // TODO Auto-generated method stub

    }

    @Override
    public <T extends PersistenceObject> Map<String, Collection<T>> getAssociations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends PersistenceObject> Collection<T> getAssociations(
            final String arg0) throws NotDefinedInModelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeAllAssociations(final String arg0)
            throws NotDefinedInModelException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAssociation(final String arg0, final PersistenceObject arg1)
            throws ModelConstraintViolationException, NotDefinedInModelException {
        // TODO Auto-generated method stub

    }

    @Override
    public PersistenceObject getEntityAddressInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntityAddressInfo(final PersistenceObject arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<ManagedObject> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFdn() {
        return fdn;
    }

    @Override
    public short getLevel() {
        return level;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ManagedObject getParent() {
        return null;
    }

    @Override
    public Object performAction(final String actionName,
            final Map<String, Object> actionArguments)
            throws NotDefinedInModelException, ModelConstraintViolationException {
        // TODO Auto-generated method stub
        return null;
    }

}