package com.ericsson.oss.services.dps;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService;
import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.itpf.datalayer.dps.query.Query;
import com.google.gson.Gson;

public class DpsHelper {

    private static DpsHelper dpsHelperInstance = null;
    private static DataPersistenceService dps = null;

    private DpsHelper() {
        resolveDps();
    }

    public static DpsHelper getInstance() {
        if (dpsHelperInstance == null) {
            dpsHelperInstance = new DpsHelper();
        }
        return dpsHelperInstance;
    }

    private static void resolveDps() {
        
        final Context ctx;
        try {
            
            ctx = new InitialContext();
            dps = (DataPersistenceService) ctx.lookup("java:/datalayer/DataPersistenceService");
//            dps = (DataPersistenceService) ctx
//                   .lookup("java:global/data-persistence-service-runtime/dps-ejb-" + version +"/DataPersistenceServiceBean!com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService");
            //        .lookup("java:global/dps-ear-runtime-1.2.7/dps-ejb-1.2.7/DataPersistenceServiceBean!com.ericsson.oss.itpf.datalayer.dps.DataPersistenceService");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public DataPersistenceService getDps() {
        return dps;
    }

    public DataBucket getLiveBucket() {
        return getDps().getLiveBucket();
    }

    public ManagedObject getRootMo(final String rootFdn) {
        final ManagedObject rootMo = findMo(rootFdn);
        return rootMo;
    }
    
    
    private TestManagedObject convertManagedObjectToTestManagedObject(final ManagedObject mo) {
        return new TestManagedObject(mo.getType(), mo.getName(), mo.getFdn());

    }

    public String convertManagedObjectToReadableForm(final ManagedObject mo) {
        
        final TestManagedObject rootTestManagedObject = convertManagedObjectToTestManagedObject(mo);
        recursivePopulateRootTMO(rootTestManagedObject, mo);



        final Gson gson = new Gson();
        final String result = gson.toJson(rootTestManagedObject);
        return result;
    }
    

    private void recursivePopulateRootTMO(TestManagedObject rootTestManagedObject, ManagedObject mo){ 

        getMoAtttribues(rootTestManagedObject, mo);
        getChildrenMo(rootTestManagedObject, mo);
 
        
    }

    private void getChildrenMo(TestManagedObject testManagedObject, ManagedObject mo) {
        final Collection<ManagedObject> children = mo.getChildren();
        for (final ManagedObject child : children) {
            TestManagedObject childTMO = convertManagedObjectToTestManagedObject(child);
            recursivePopulateRootTMO(childTMO,child);
            testManagedObject.addChildren(childTMO);
            
        }
        
    }
    
    private void getMoAtttribues(TestManagedObject testManagedObject, ManagedObject mo) {
        final Map<String, Object> attsMap = mo.getAllAttributes();

        for (final String key : attsMap.keySet()) {
            testManagedObject.setMoAttributes(key.toString() + "=" + attsMap.get(key));
        }        
    }



    public void getFullMoTopology() {
        //Query query = dps.getQueryBuilder().createTypeQuery("CPP_NODE_MODEL", "ManagedElement", "MeContext=Erbs001");
        final Query query = dps.getQueryBuilder().createContainmentQuery("MeContext=Erbs001");
        final Iterator<ManagedObject> queryBuilder = getLiveBucket().getQueryExecutor().execute(query);

        for (final Iterator<ManagedObject> it = queryBuilder; it.hasNext();) {
            // ManagedObject mo = it.next();
        }
    }

    public ManagedObject findMo(final String fdn) {
        return getLiveBucket().findMoByFdn(fdn);

    }
    

    public String getTestMOs(final String fdn) {
        String result = null;
        ManagedObject mo = null;
        try {
            mo = findMo(fdn);

            if (mo != null) {
                result = convertManagedObjectToReadableForm(mo);
            } else {
                result = fdn + " is not in the DPS ";
            }

        } catch (Exception e) {
            result = "Unable to create find MO " + fdn + " because of exception : " + e.getLocalizedMessage();
            e.printStackTrace();
        }

        return result;

    }

    public String deleteTestMOs(final String fdn) {
        String result = null;
        try {
            final ManagedObject mo = findMo(fdn);
            if (mo != null) {
                getDps().getLiveBucket().deletePo(mo);
                result = "Successful deletion of " + fdn;
            } else {
                result = "Unable to delete " + fdn + " because it does not exist in the DPS";
            }

        } catch (Exception e) {
            result = "Unable to create find MO " + fdn + " because of exception : " + e.getLocalizedMessage();
            e.printStackTrace();
        }

        return result;

    }
    
    public String getMoAttribute(final String fdn, final String attribute) {
        String attr = "";
        ManagedObject mo = null;
        try {
            mo = findMo(fdn);
            attr = mo.getAttribute(attribute);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return attr;

    }
    
    public String getMoAttributeNameAndType(final String fdn, final String attribute) {
        String attrNameAndType = "";
        
        ManagedObject mo = null;
        try {
            mo = findMo(fdn);
            Object attrName = mo.getAttribute(attribute);
            if (attrName == null){
                attrNameAndType = "null:::null";
            }
            else {
            Object c = mo.getAttribute(attribute).getClass();
            attrNameAndType =  attrName.toString() + ":::" + c.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return attrNameAndType;

    }
    
    public  Map<String, Object> getMoAttributes(final String fdn) {
        Map<String, Object> attrs = new HashMap<String, Object> ();
        ManagedObject mo = null;
        try {
            mo = findMo(fdn);
            attrs = mo.getAllAttributes();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return attrs;

    }
    

}
