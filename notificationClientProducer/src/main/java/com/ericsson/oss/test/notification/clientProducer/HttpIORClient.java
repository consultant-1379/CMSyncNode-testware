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
package com.ericsson.oss.test.notification.clientProducer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class HttpIORClient {

    /**
     * HttpClient which allows the execution of the http get request
     */
    private final HttpClient client;

    private static final String URL = "http://%s:80/cello/ior_files/nameroot.ior";
    private static final String LAST_MODIFIED_HEADER_NAME = "Last-Modified";
    private static final String LAST_MODIFIED_DATE_FORMAT = "E, dd MMM yyyy HH:mm:ss z";

    /**
     * Timeout for opening the URL connection to cello.
     */
    private static final int TIMEOUT = 5 * 1000;


    public HttpIORClient() {
        final HttpClientParams clientParams = new HttpClientParams();
        clientParams.setConnectionManagerTimeout(TIMEOUT);
        clientParams.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));

        client = new HttpClient(clientParams);
    }

    /**
     * TODO Define custom exception class, replace the throws statement
     *
     * @param ipAddress
     * @return Interoperable Object Reference (ior)
     */
    public HttpIORClient.HttpInfo getIOR(final String ipAddress) throws Exception {
        //System.out.println("HttpIORClient:getIOR():start");
        String responseBodyIor = null;
        Date lastModified = null;
        final String celloURL = String.format(URL, ipAddress);

        final GetMethod getMethod = new GetMethod(celloURL);

        int statusCodeResponse;
        try {
            statusCodeResponse = this.client.executeMethod(getMethod);

            final Header lastModifiedHeader = getMethod.getResponseHeader(LAST_MODIFIED_HEADER_NAME);
            // lastModified = null;
            if (lastModifiedHeader != null) {
                final String lastModifiedString = lastModifiedHeader.getValue();
                final SimpleDateFormat formatter = new SimpleDateFormat(LAST_MODIFIED_DATE_FORMAT);
                try {
                    lastModified = formatter.parse(lastModifiedString);
                } catch (final ParseException e) {
                    System.err.println("Retrieved date from the node with an unexpected format. Expected format: {}, date string");
                }
                //System.out.println("--> last modified header: '{}'");
            } else {
                //System.out.println("--> no last modified header :(");
            }

            if (statusCodeResponse != HttpStatus.SC_OK) {
                System.err.println("Failure status code received =" + statusCodeResponse);
            }
            // Read the response body.
            responseBodyIor = getMethod.getResponseBodyAsString();
            System.out.printf("Received IOR for node [%s] is  %s\n" , ipAddress ,responseBodyIor);
            //System.out.println("HttpIORClient:getIOR():end");
            //  return new HttpInfo(responseBodyIor, lastModified);
        } catch (final HttpException e) {
            System.err.println("HTTP or HttpClient exception has occurred.");
        } catch (final IOException e) {
            System.err.println("Client is unable to establish a connection with the target during the timeout period.");
        } finally {
            getMethod.releaseConnection();
            return new HttpInfo(responseBodyIor, lastModified);
        }
    }

    public class HttpInfo {

        private final String iorReference;

        private final Date restartDate;

        /**
         * @param iorReference
         * @param restartDate
         */
        public HttpInfo(final String iorReference, final Date restartDate) {
            super();
            this.iorReference = iorReference;
            this.restartDate = restartDate;
        }

        /**
         * @return the iorReference
         */
        public String getIorReference() {
            return iorReference;
        }

        /**
         * @return the restartDate
         */
        public Date getRestartDate() {
            return restartDate;
        }

    }
}

