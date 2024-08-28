package com.ericsson.oss.cmsync.operator.api.exception;

/**
 * An exception that will be thrown by the {@link AddRemoveNodeOperatorImpl} in the event that a node fails to get added or deleted.
 */
public class CmMediationOperatorException extends Exception {

    private static final long serialVersionUID = -1563236980228253557L;

    public CmMediationOperatorException(final String message) {
        super(message);
    }

    public CmMediationOperatorException(final Throwable cause) {
        super(cause);
    }

    public CmMediationOperatorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}