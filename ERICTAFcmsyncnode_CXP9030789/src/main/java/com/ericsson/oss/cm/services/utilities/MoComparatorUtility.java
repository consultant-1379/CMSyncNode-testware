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
package com.ericsson.oss.cm.services.utilities;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement;
import com.ericsson.oss.cm.services.test.netsim.NetsimHelper;
import com.ericsson.oss.services.dps.TestManagedObject;

public class MoComparatorUtility {

    private static final Logger logger = Logger.getLogger(MoComparatorUtility.class);

    public static boolean areEqual(final TestManagedObject actual, final TestManagedObject expected) {

        // logger.info("################################################################################################################################################################");
        logger.info("Validating  " + actual.getFdn());
        logger.info("################################################################################################################################################################");

        boolean result = validate(actual, expected);
        if (!result) {
            return false;
        }

        // result = validateChildrenMO(actual,expected);

        logger.info("################################################################################################################################################################");
        logger.info("Final Validation result of [" + actual.getFdn() + "]:   " + result);
        logger.info("################################################################################################################################################################");

        return result;
    }

    private static boolean validate(final TestManagedObject actual, final TestManagedObject expected) {

        logger.info("--------------------------------------------------------------------------------------------------------------------------");
        logger.info("Validating  " + actual.getFdn());
        logger.info("--------------------------------------------------------------------------------------------------------------------------");

        boolean result = isMoFdnValid(actual, expected);
        if (!result)
            return result;

        result = isMoTypeValid(actual, expected);
        if (!result)
            return result;

        result = isMoNameValid(actual, expected);
        if (!result)
            return result;

        //        result = isMoAttributesValid(actual, expected);
        //        if (!result)
        //            return result;

        result = areMoChildrenFdnValid(actual, expected); // this implies all children types and names are correct

        return result;
    }

    private static boolean getNumberOfMoChildren(final TestManagedObject actual, final TestManagedObject expected) {

        if (actual.getChildrenCount() == expected.getChildrenCount()) {
            logger.info("The number of children [" + actual.getChildrenCount() + "] contained is correct");
            return true;
        } else {
            logger.info("The number of children do not match");
            logger.info("The number of children contain in the actual is " + actual.getChildrenCount());
            logger.info("The number of children contain in the expected is " + expected.getChildrenCount());

            areMoChildrenFdnValid(actual, expected);
            return false;
        }
    }

    private static boolean validateChildrenMO(final TestManagedObject actual, final TestManagedObject expected) {
        boolean result = true;
        for (final TestManagedObject expChild : expected.getChildren()) {
            for (final TestManagedObject actChild : actual.getChildren()) {
                if (!validate(actChild, expChild)) {
                    return false;
                }
                result = validateChildrenMO(actChild, expChild);
            }
        }
        return result;

    }

    public static boolean isMoFdnValid(final TestManagedObject actual, final TestManagedObject expected) {
        if (actual.getFdn().equals(expected.getFdn())) {
            logger.info(actual.getFdn() + " is a valid FDN");
            return true;
        } else {
            logger.info(actual.getFdn() + " is not a valid FDN");
            return false;
        }
    }

    public static boolean isMoTypeValid(final TestManagedObject actual, final TestManagedObject expected) {
        if (actual.getMoType().equals(expected.getMoType())) {
            logger.info(actual.getMoType() + " is a valid type");
            return true;
        } else {
            logger.info(actual.getFdn() + " is not valid type");
            return false;
        }
    }

    public static boolean isMoNameValid(final TestManagedObject actual, final TestManagedObject expected) {
        if (actual.getMoName().equals(expected.getMoName())) {
            logger.info(actual.getFdn() + " is a valid name");
            return true;
        } else {
            logger.info(expected.getFdn() + " is not a valid name");
            return false;
        }
    }

    public static boolean isMoAttributesValid(final TestManagedObject actual, final TestManagedObject expected) {
        logger.info("ACTUAL MO attributes for [" + actual.getFdn() + "] are: " + actual.getMoAttributes().toString());
        logger.info("EXPECTED MO attributes for [" + expected.getFdn() + "] are: " + expected.getMoAttributes().toString());

        ArrayList<String> commonList = (ArrayList<String>) CollectionUtils.retainAll(actual.getMoAttributes(), expected.getMoAttributes());

        if (commonList.size() != actual.getMoAttributes().size()) {
            for (final String s : expected.getMoAttributes()) {
                actual.getMoAttributes().remove(s);
            }

            logger.info("The following attribute(s) are missing/invalid:" + actual.getMoAttributes().toString());
            return false;
        } else {
            logger.info("Attributes are valid ");
            return true;
        }
    }

    public static boolean areMoChildrenFdnValid(final TestManagedObject dpsTMO, final TestManagedObject netsimTMO) {

        List<String> expectedChildrenFdn = netsimTMO.getChildrenFdn();
        List<String> actualChildrenFdn = dpsTMO.getChildrenFdn();

        Boolean foundFirst = false;
        Boolean foundSecond = false;
        Boolean foundThird = false;
        logger.debug("Removing Superflous DPS Objects, starting ");

        for (Iterator<String> itr = actualChildrenFdn.iterator(); itr.hasNext();) {
            String child = itr.next();
            if (child.contains(",ERBSConnectivityInfo=")) {
                logger.debug("Removing Superflous DPS object ERBSConnectivityInfo ");
                itr.remove();
                foundFirst = true;
            }
            if (child.contains(",FmSupervision=")) {
                logger.debug("Removing Superflous DPS object FmSupervision ");
                itr.remove();
                foundSecond = true;
            }
            if (child.contains(",Inventory=")){
                logger.debug("Removing Superflous DPS object Inventory ");
                itr.remove();   
                foundThird =true; 
            }
          //  logger.info("Removing Superflous DPS Objects, completion ");
            if ( foundFirst && foundSecond && foundThird)  
                break;

        }

        List<String> list = new ArrayList(CollectionUtils.disjunction(actualChildrenFdn, expectedChildrenFdn));

        if (list.isEmpty()) {
            logger.info("The number of children[" + actualChildrenFdn.size() + "] for this fdn is valid ");
            for (String actualChild : actualChildrenFdn) {
                logger.debug(actualChild);
            }
            logger.info("There was [" + actualChildrenFdn.size() + "] children created.");
            return true;
        } else {
            Collections.sort(list);
            logger.info("There are [" + list.size() + "] missing.The following fdns are missing/invalid:");
            for (String missingFdn : list) {
                logger.info(missingFdn);
            }
            logger.info("There was [" + list.size() + "] children missing.");
            return false;
        }

    }

    public static boolean verifyBasicAttributeKeys(final NetworkElement netsimNe, TestManagedObject tmo) {

        boolean result = false;
        List<String> allChildren = tmo.getChildrenFdn();
        List<String> childrenFdn = new ArrayList<String>();
        List<String> randomFdns = new ArrayList<String>();

        /*
         * Required to remove unnecessary data to be read from netsim e.g. ERBSConnectivityInfo
         */
        for (String s : allChildren) {
            if (s.contains(",ERBSConnectivityInfo=")) {
                continue;
            }
            else if (s.contains(",Inventory=")){
                continue;
            } else if (s.contains(",Inventory=")) {
                continue;
            }
            childrenFdn.add(s);
        }

        Random randomGenerator = new Random();

        for (int i = 0; i < 5; i++) {
            int randomIndex = randomGenerator.nextInt(childrenFdn.size());
            randomFdns.add(childrenFdn.get(randomIndex));
        }

        for (String fdn : randomFdns) {
            logger.info("******************************************************************************************************");
            logger.info("Checking DPS attributes for fdn - " + fdn);
            List<String> dpsAttrs = RestUtility.getMOAttributes(fdn);

            logger.info("Checking NETSIM attributes for fdn - " + fdn);
            //            List<String> list = new ArrayList<String>(1);
            //            list.add(netsim);
            //            List<NetworkElement> netsimNEList = NetsimHelper.getAllStartedNesFromSimList(list);
            //            for (NetworkElement ne: netsimNEList){
            String neName = netsimNe.getName();
            tmo.getMoName();
            if (neName.equals(tmo.getMoName())) {
                List<String> netsimAttrs = NetsimHelper.dumpMOAttributes(netsimNe, fdn);

                logger.info("******************************************************************************************************");

                result = compareAttributes(dpsAttrs, netsimAttrs);

            }
            //    }

            //            List<String> netsimAttrs = NetsimHelper.dumpMOAttributes(netsimNEList.get(0), fdn);
            //
            //            logger.info("******************************************************************************************************");
            //
            //            result = compareAttributes(dpsAttrs, netsimAttrs);
            if (result == false) {
                return false;

            }

        }

        return result;

    }

    public static boolean compareAttributes(List<String> dpsAttrsRaw, List<String> netsimAttrsRaw) {

        List<String> dpsAttrs = new ArrayList<String>();

        for (String a : dpsAttrsRaw) {
            String trimmed = a.replaceAll("\\s", "");
            dpsAttrs.add(trimmed);
        }

        List<String> netsimAttrs = new ArrayList<String>();
        for (String b : netsimAttrsRaw) {
            String trimmed = b.replaceAll("\\s", "");
            netsimAttrs.add(trimmed);
        }

        Collections.sort(dpsAttrs);
        Collections.sort(netsimAttrs);

        logger.info("******************************************************************************************************");
        logger.info("Number of Attributes in NETSIM is " + netsimAttrs.size());
        for (String missingAttr : netsimAttrs) {
            logger.info(missingAttr);
        }

        logger.info("******************************************************************************************************");
        logger.info("Number of Attributes in DPS is " + dpsAttrs.size());
        logger.info("******************************************************************************************************");
        for (String dpsAttr : dpsAttrs) {
            logger.info(dpsAttr);
        }
        return true;

    }

}
