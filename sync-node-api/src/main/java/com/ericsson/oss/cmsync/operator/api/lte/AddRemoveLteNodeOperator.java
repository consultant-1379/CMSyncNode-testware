package com.ericsson.oss.cmsync.operator.api.lte;

import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.enm.data.NetworkNode;
import com.ericsson.oss.cmsync.operator.api.common.BaseOperator;
import com.ericsson.oss.cmsync.operator.api.exception.CmMediationOperatorException;
import com.ericsson.oss.cmsync.operator.api.exception.HttpToolNotSetException;

/**
 * This interface provides methods which clients can use to add and remove LTE network elements to an ENM system. It also provides methods that can be
 * used to help verify whether or not the network element was successfully added.
 * <p>
 * The implementation of this interface will use the CMEditor REST end-point to perform the functionality provided by a lot of the methods. The ENM
 * system provides access to this end-point through a JBOSS container or the Apache server. The preferred option should be to use Apache as it avoids
 * the need for tunnels.
 * <p>
 * The {@code setHttpTool} method must be the first method called prior to using any other functionality provided by this operator. This is required
 * to set the tool to use to contact the REST end-point. Currently the only tool supported is TAFs {@link HttpTool}. The {@link LauncherOperator} can
 * be used to login and generate the {@code HttpTool} instance.
 * <p>
 * Its possible to create a {@code HttpTool} instance that can be used to execute directly against a JBOSS container. However, you must ensure that
 * tunneling is enabled via TAF properties to allow the requests to succeed as the JBOSS containers use private ipAddresses. The peer servers SC1, and
 * SC2 have public ipAddresses and can be used for this purpose. However, be aware that tunneling consumes resources on the peer (~5% of CPU per
 * tunnel created).
 * <p>
 * The Apache server running on the peers can be accessed directly without the need for tunneling.
 * </p>
 */
public interface AddRemoveLteNodeOperator extends BaseOperator {

    /**
     * Sets the tool that will be used by the client to communicate with the REST interface.
     * 
     * @param tool
     *            Instance of tool {@link HttpTool}.
     */
    void setHttpTool(HttpTool tool);

    /**
     * This will create a subnetwork. If the subnetwork FDN contains any parent subnetworks then they will also be created.
     * 
     * @param subNetworkFdn
     *            FDN of the SubNetwork MO(s) to create
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void createSubNetwork(String subNetworkFdn) throws CmMediationOperatorException;

    /**
     * This will remove the subnetwork and any children of the subnetwork.
     * 
     * @param subNetworkFdn
     *            FDN of the SubNetwork MO(s) to delete
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void removeSubNetwork(String subNetworkFdn) throws CmMediationOperatorException;
    
    /**
     * This will create all the MO's required to add an LTE node. It will also verify successful creation of the Supervision and Function MOs.
     * 
     * @param node
     *            Object representing the network element
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void addSingleLteNode(NetworkNode node) throws CmMediationOperatorException;

    /**
     * This method allows clients to add a batch of network elements.
     * <p>
     * The user must provide the ipAddress of the first network element to be added. This ipAddress will be incremented sequentially for each
     * subsequent network element added.
     * <p>
     * Note that the nodes will be added without any SubNetwork prefix. Only the NetworkElement, CppConnectivityInformation and MeContext MOs will be
     * created.
     * </p>
     * <p>
     * The relative distinguished name (RDN) of the MeContext FDN will be constructed from the parameters {@code startingIndex}, and {@code baseName},
     * and will be created according to naming conventions used in NetSim simulations. The following formula will be used to construct the FDN:
     * </p>
     * <p>
     * MeContext={@code baseName} + ERBS00[0][0] + {@code startingIndex}'.
     * </p>
     * 
     * @param firstIpAddress
     *            IpAddress of the first network element to be added.
     * 
     * @param baseName
     *            Name used to construct network element name. Should follow same naming convention as by the network elements in NetSim simulations
     *            e.g. LTE01, LTE02 etc.
     * 
     * @param startingIndex
     *            Index which will be used to construct the RDN of the MeContext FDN
     * 
     * @param numNodes
     *            Number of network elements to be added
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void addBatchOfNodes(String firstIpAddress, String baseName, int startingIndex, int numNodes) throws CmMediationOperatorException;

    /**
     * This method will remove the NetworkElement and MeContext MOs and all their children.
     * 
     * @param node
     *            Object representing the network element
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void removeSingleLteNode(NetworkNode node) throws CmMediationOperatorException;

    /**
     * This method allows clients to remove a batch of nodes using the same parameters that were used to add a batch of nodes.
     * <p>
     * The RDN of the network elements MeContext FDN will be constructed using the formula ' {@code baseName} + ERBS00[0][0] + {@code startingIndex}'.
     * </p>
     * 
     * @param baseName
     *            Name used to construct network element name. Should follow same naming convention as by the network elements in NetSim simulations
     *            e.g. LTE01, LTE02 etc.
     * 
     * @param startingIndex
     *            Index which will be used to construct the RDN of the MeContext FDN
     * 
     * @param numNodes
     *            Number of network elements to be removed
     * 
     * @throws HttpToolNotSetException
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     */
    void removeBatchOfNodes(String baseName, int startingIndex, int numNodes) throws CmMediationOperatorException;

    /**
     * This method can be used to confirm that the CppConnectivityInformation MO exists after a node is added.
     * 
     * @param node
     *            Object representing the network element
     * 
     * @return Boolean value indicating the success or failure of the operation.
     * 
     * @throws HttpToolNotSetException
     *             Runtime exception that will be thrown if the {@code HttpTool} is not set for the operator.
     */
    boolean isCppConnectivityInfoCreated(NetworkNode node);

    /**
     * This method verifies that the ossPrefix attribute of the NetworkElement MO is set.
     * 
     * @param node
     *            Object representing the network element
     * 
     * @return Boolean value indicating the success or failure of the operation.
     * 
     * @throws HttpToolNotSetException
     *             Runtime exception that will be thrown if the {@code HttpTool} is not set for the operator.
     */
    boolean isOssPrefixSet(NetworkNode node);

    /**
     * This method will use TAFs {@link CLI} utility to execute a Versant command on the SUT to fetch the number of associations existing for managed
     * objects of type CppConnectivityInformation.
     * <p>
     * The command is executed on the first peer 'SC-1' which should be defined as a system property.
     * </p>
     * 
     * @return The number of associations
     */
    int getNumberOfCiAssociations();

    /**
     * This method marks the networkElement and MeContext MOs of the node to be deleted by the {@code removeMarkedNodes} method.
     * 
     * @param node
     *            Object representing the network element
     * 
     * @throws HttpToolNotSetException
     *             Runtime exception that will be thrown if the {@code HttpTool} is not set for the operator.
     */
    void markNodeForDeletion(NetworkNode node);

    /**
     * This method deletes the nodes that have been marked for deletion. Note that it will delete even if the node has been synched. It deletes the
     * MeContext MO and all its children, and the NetworkElement MO and all its children.
     * 
     * @throws CmMediationOperatorException
     *             Checked exception that will be thrown if the operation is unsuccessful
     * 
     * @throws HttpToolNotSetException
     */
    void removeMarkedNodes() throws CmMediationOperatorException;
}