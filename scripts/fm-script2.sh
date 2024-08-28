#!/bin/bash
NETSIMDIR="/netsim/netsimdir"
NETSIM_LIST=( ieatnetsimv5012-06  )
SPECIAL_COMMAND="ls $NETSIMDIR | grep LTE | grep -v zip"


getSims() {

        SIM_LIST=`ssh netsim@${NETSIM}.athtem.eei.ericsson.se $SPECIAL_COMMAND` 
}

for NETSIM in ${NETSIM_LIST[*]} ; do
/opt/ericsson/enmutils/bin/netsim list $NETSIM
getSims

	for SIM in ${SIM_LIST[*]}
          do
	    result=`/opt/ericsson/enmutils/bin/netsim fetch ${NETSIM}  ${SIM} /root`
	    result=`/opt/ericsson/enmutils/bin/node_populator parse ${SIM}  /root/`    
            result=`/opt/ericsson/enmutils/bin/workload add  ${SIM} 1-160`
            result=`rm -f /root/*.xml`
           
  	 done
SIM_LIST=""
done
