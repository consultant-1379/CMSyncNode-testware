package com.ericsson.oss.cmsync.operator.api.exception;

/**
 * Exception that will be thrown if an attempt is made to use any operator methods before the {@code setHttpTool} method has been successfully called
 * on the operator.
 */
public class HttpToolNotSetException extends RuntimeException {

    private static final long serialVersionUID = 4789269306950983793L;
    private static final String HTTPTOOL_NOT_SET_RSP = "The operator cannot be used as the Httptool has not been set via the setHttpTool method.";

    public HttpToolNotSetException() {
        super(HTTPTOOL_NOT_SET_RSP);
    }

}