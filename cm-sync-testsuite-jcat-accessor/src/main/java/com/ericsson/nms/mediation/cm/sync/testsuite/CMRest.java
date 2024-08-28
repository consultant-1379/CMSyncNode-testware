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

import java.util.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.*;
import javax.naming.Context;
import javax.transaction.UserTransaction;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.delegate.DataAccessDelegate;
import com.ericsson.oss.services.dps.DpsHelper;

@Path("/CM")
@RequestScoped
public class CMRest {

    @Inject
    protected UserTransaction utx;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/test")
    public Response getTest() {
        final String result = "TEST RESPONSE";
        final ResponseBuilder builder = Response.ok(result);
        return builder.build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/test/dps/isDpsAvailable")
    public Response isDpsAvailable() {

        DataPersistenceService dataPersistenceService = getDps().getDps();
        if (dataPersistenceService == null) {
            final ResponseBuilder builder = Response.ok("NO");
            return builder.build();

        } else {
            final ResponseBuilder builder = Response.ok("YES");
            return builder.build();
        }
    }

    private DpsHelper getDps() {
        return DpsHelper.getInstance();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/test/dps/getTestMO/{fdn}")
    public String getTestMO(@PathParam("fdn") final String fdn) {
        String result = null;
        try {
            utx.begin();
            result = getDps().getTestMOs(fdn);
        } catch (Exception e) {
            result = "Unable to get Test MOs because of exception: " + e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            try {
                utx.commit();
            } catch (final Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/test/dps/deleteTestMO/{fdn}")
    public String deleteTestMO(@PathParam("fdn") final String fdn) {
        String result = "";
        try {
            utx.begin();
            result = getDps().deleteTestMOs(fdn);
        } catch (Exception e) {
            result = "Could not delete the MO because of " + e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            try {
                utx.commit();
            } catch (final Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/test/dps/getMoAttribute/{fdn}/{attribute}")
    public String getMoAttribute(@PathParam("fdn") final String fdn, @PathParam("attribute") final String attribute) {
        String result = null;
        try {
            utx.begin();
            result = getDps().getMoAttribute(fdn, attribute);
        } catch (Exception e) {
            result = "Unable to get attribute [" + attribute + "] from " + fdn + " because of exception: " + e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            try {
                utx.commit();
            } catch (final Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/test/dps/getMoAttributes/{fdn}")
    public List<String> getMoAttributes(@PathParam("fdn") final String fdn) {
        Map<String, Object> result = null;
        try {
            utx.begin();
            result = getDps().getMoAttributes(fdn);
            System.out.println("The size of attribute map for " + fdn + " is " + result.size());
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                utx.commit();
            } catch (final Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        List<String> attrs = new ArrayList<String>();
        Iterator iterator = result.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            attrs.add((String) mapEntry.getKey() + "=" + mapEntry.getValue());
        }

        return attrs;
    }
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/test/dps/getMoAttributeNameAndType/{fdn}/{attribute}")
    public String getMoAttributeNameAndType(@PathParam("fdn") final String fdn, @PathParam("attribute") final String attribute) {
        String result = null;
        try {
            utx.begin();
            result = getDps().getMoAttributeNameAndType(fdn, attribute);
        } catch (Exception e) {
            result = "Unable to get attribute [" + attribute + "] from " + fdn + " because of exception: " + e.getLocalizedMessage();
            e.printStackTrace();
        } finally {
            try {
                utx.commit();
            } catch (final Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/trigger/{appversion}/{NSpace}/{id}/{type}/{version}/{fdn}/{level}/{name}/{action}/")
    public Response getJobsInCache(@PathParam("appversion") final String versionMedCore, @PathParam("NSpace") final String nameS,
                                   @PathParam("id") final String id, @PathParam("type") final String type,
                                   @PathParam("version") final String version, @PathParam("fdn") final String fdn,
                                   @PathParam("level") final String level, @PathParam("name") final String name,
                                   @PathParam("action") final String action) {
        System.out.println("Params are : " + versionMedCore + nameS + id + type + version + fdn + level + name + action);
        startSomething(versionMedCore, nameS, Long.parseLong(id, 10), type, version, fdn, Short.parseShort(level), name, action);
        final ResponseBuilder builder = Response.ok();
        return builder.build();
    }

    /*
     * This method is no longer required as this is possible via the CM Script Engine. Will remain in code base until further notice
     */
    private void startSomething(final String appVersion, final String nameSpace, final long id, final String type, final String version,
                                final String fdn, final short level, final String name, final String action) {
        try {
            Context ctx = new InitialContext();
            StringBuilder str = new StringBuilder("java:global/mediation-core/mediation-core-ejb-");
            str.append(appVersion);
            str.append("/DataAccessDelegateClientBean!com.ericsson.oss.itpf.datalayer.dps.delegate.DataAccessDelegate");
            DataAccessDelegate manager = (DataAccessDelegate) ctx.lookup(str.toString());
            System.out.println("Invoking: " + str.toString());
            final String elementName = action;
            final Map<String, Object> attrs = new HashMap<>();
            final MeContext mo = new MeContext(nameSpace, id, type, version, fdn, level, name);
            manager.performAction(mo, elementName, attrs);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

}
