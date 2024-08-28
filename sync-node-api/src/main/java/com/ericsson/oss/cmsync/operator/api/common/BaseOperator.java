package com.ericsson.oss.cmsync.operator.api.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ericsson.oss.cmsync.operator.api.exception.CmMediationOperatorException;
import com.ericsson.oss.cmsync.operator.api.exception.HttpToolNotSetException;

/**
 * This interface provides functionality common to all CM mediation operators. It will be implemented by an abstract operator which concrete CM
 * operators can extend.
 * 
 */
public interface BaseOperator {

    /**
     * This method will return the number of object types i.e. any PO or MO object type.
     * 
     * @param type
     *            The object type i.e. Persistent or Managed Object type name (e.g. EntityAddressInfo, EUtranCell, ENodeBFuntion etc.)
     * 
     * @param namespace
     *            Namespace of the model which the object under test belongs to (e.g. ERBS_NODE_MODEL)
     * 
     * @return The number of instances found
     * 
     * @throws HttpToolNotSetException
     */
    int getNumObjects(String type, String namespace);

    /**
     * This method will retrieve a list of FDNs from the SUT for all objects of the managed object type specified.
     * 
     * @param moType
     *            The managed object type for which an FDN is required
     * 
     * @param namespace
     *            Namespace of the model which the object under test belongs to (e.g. ERBS_NODE_MODEL)
     * 
     * @return A list of Managed Object FDNs
     * 
     * @throws HttpToolNotSetException
     */
    Set<String> getMoFdns(String moType, String namespace);

    /**
     * This method will retrieve all matching FDNs from the specified node on the SUT for the managed object type specified.
     * 
     * @param nodeName
     *            The name of the node from which the FDN should be retrieved i.e. the id part of the MeContext RDN (e.g. For RDN
     *            'MeContext=LTE100ERBS00001' the id is 'LTE100ERBS00001').
     * 
     * @param moType
     *            The managed object type for which FDNs are requested
     * 
     * @return {@code Set} containing list of Strings corresponding to the FDNs requested
     * 
     * @throws HttpToolNotSetException
     */
    Set<String> getMoFdnsFromSpecifiedNode(String nodeName, String moType);

    /**
     * This method will retrieve all matching FDNs from the specified node on the SUT for the managed object types specified.
     * 
     * @param nodeName
     *            The name of the node from which the FDN should be retrieved i.e. the id part of the MeContext RDN (e.g. For RDN
     *            'MeContext=LTE100ERBS00001' the id is 'LTE100ERBS00001').
     * 
     * @param moTypes
     *            The managed object types for which FDNs are requested
     * 
     * @return {@code Set} containing list of Strings corresponding to the FDNs requested
     * 
     * @throws HttpToolNotSetException
     */
    Set<String> getMoFdnsFromSpecifiedNode(String nodeName, List<String> moTypes);

    /**
     * Returns a single attribute for the given MO.
     * 
     * @param moFdn
     *            FDN of the MO you wish to get the attribute value from.
     * 
     * @param attributeName
     *            Name of the attribute to retrieve the value.
     * 
     * @return String of the attribute value.
     * 
     * @throws HttpToolNotSetException
     */
    String getAttribute(String moFDN, String attributeName);

    /**
     * This method will return the value for the given attribute of an MO.
     * 
     * @param nodeName
     *            The name of the node from which the FDN should be retrieved i.e. the id part of the MeContext RDN (e.g. For RDN
     *            'MeContext=LTE100ERBS00001' the id is 'LTE100ERBS00001').
     * 
     * @param moType
     *            The managed object type which contains the attribute
     * 
     * @param moId
     *            The managed object ID which contains the attribute
     * 
     * @param attributeName
     *            Name of the attribute to retrieve the value.
     * 
     * @return String of the attribute value
     * 
     * @throws HttpToolNotSetException
     */
    String getAttribute(final String nodeName, final String moType, final String moId, final String attributeName);

    /**
     * Returns a map of attributes for the given MO.
     * 
     * @param moFdn
     *            FDN of the MO you wish to get the attribute values from.
     * 
     * @param attributeNames
     *            Names of the attributes to retrieve the values.
     * 
     * @return Map
     *            of the attribute names and their values.
     * 
     * @throws HttpToolNotSetException
     */
    Map<String, String> getAttributes(String moFDN, String... attributeName);

    /**
     * This method is provided to allow managed objects to be marked for deletion, so they can be cleaned up when test case execution is complete. The
     * MO and its children will be marked for deletion.
     *
     * @param fdn
     *            Fully distinguished name of the Managed Object
     */
    void markMoForDeletion(String fdn);

    /**
     * This method should be called to delete MOs which have been marked for deletion by the method {@code markMoForDeletion}.
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     * 
     * @throws HttpToolNotSetException
     */
    void deleteMarkedMos() throws CmMediationOperatorException;

    /**
     * This method will delete the Managed Object represented by the FDN supplied. If the Managed Object has children it will delete those too.
     * 
     * @param fdn
     *            Fully distinguished name of the managed object
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     * 
     * @throws HttpToolNotSetException
     */
    void deleteManagedObject(String fdn) throws CmMediationOperatorException;
}
