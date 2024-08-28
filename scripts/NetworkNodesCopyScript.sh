#!/bin/bash

. ./NETWORK_ENV 

NODE_COUNT=0

STARTED_COMMAND="echo '.show started' | /netsim/inst/netsim_shell | grep -v server | grep -c LTE"

for NETSIM in ${NETSIM_LIST[*]} ; do 
   CURRENT_NODE_COUNT=0
   echo "Copying files to  ${NETSIM}"
   CURRENT_NODE_COUNT=`scp avc_*.sh netsim@${NETSIM}.athtem.eei.ericsson.se:/netsim`
   echo ${CURRENT_NODE_COUNT};
   ssh netsim@${NETSIM}.athtem.eei.ericsson.se "chmod +x avc_*.sh " 
done;
echo "Done" 
