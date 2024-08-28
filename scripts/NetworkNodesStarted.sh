#!/bin/bash

NETSIM_LIST=( ieatnetsimv5012-01 ieatnetsimv5012-02 ieatnetsimv5012-03 ieatnetsimv5012-04 ieatnetsimv5012-05 ieatnetsimv5012-06 ieatnetsimv5012-07 )  
NODE_COUNT=0

STARTED_COMMAND="echo '.show started' | /netsim/inst/netsim_shell | grep -v server | grep -c LTE"

for NETSIM in ${NETSIM_LIST[*]} ; do 
   CURRENT_NODE_COUNT=0
   echo "Reading Nodes from ${NETSIM}"
   CURRENT_NODE_COUNT=`ssh netsim@${NETSIM}.athtem.eei.ericsson.se $STARTED_COMMAND`
   echo $NODE_COUNT
   NODE_COUNT=$(($NODE_COUNT+$CURRENT_NODE_COUNT));
  
done;
echo "Total Active Nodes $NODE_COUNT "
