#!/bin/bash

# Target Netsim VMs for ENM 228 
# NETSIM_LIST=( ieatnetsimv5012-01 ieatnetsimv5012-02 ieatnetsimv5012-03 ieatnetsimv5012-04 ieatnetsimv5012-05 ieatnetsimv5012-06 ieatnetsimv5012-07 )  
. ./NETWORK_ENV 

SHOW_ACTION=""
SHOWAVCS="/var/tmp/showAvcs"
SPECAVCS="/var/tmp/specAvcs"

if [ $# -lt 2 ] ; then
    echo "Usage: $0 standard start|stop" 
    echo "Usage: $0 peak     start|stop" 
    echo "Usage: $0 storm    start|stop" 
    echo "Usage: $0 special  start|stop|show"
    echo "Usage: $0 creates  start|stop|show"
    echo "Usage: $0 avcs show"
    exit 1
fi

# Set Commands Arguments  , Profile  and  Action 
AVC_PROFILE=$1
AVC_ACTION=$2

ControlAVCSpecial()
{
if [ ${AVC_ACTION} == "start" ] ; then
       SPECIAL_COMMAND="./avc_MoRef_Enum.sh | /netsim/inst/netsim_shell &"
   elif [ ${AVC_ACTION} == "stop" ] ; then
       SPECIAL_COMMAND="pkill -f avc_MoRef_Enum.sh"
   fi;
   for NETSIM in ${NETSIM_LIST[*]} ; do
       if [ ${AVC_ACTION} == "show" ] ; then
           SPECIAL_COMMAND="pgrep -f avc_MoRef_Enum.sh"
           active_pid=`ssh netsim@${NETSIM}.athtem.eei.ericsson.se $SPECIAL_COMMAND`
           if [ "${active_pid}" == "" ] ; then
              echo "After ${AVC_ACTION} action ${NETSIM} has NO active Enum_and_MoRef_Load Process "
           else
              echo "After ${AVC_ACTION} action ${NETSIM} has active Enum_and_MoRef_Load Process ${active_pid} "
           fi;
       else
           echo "${AVC_ACTION} action towards Enum and MoRef Load on Nodes belonging to ${NETSIM}"
           ssh netsim@${NETSIM}.athtem.eei.ericsson.se $SPECIAL_COMMAND &
       fi;
   done;
}

ControlCreateDeletes()
{
   if [ ${AVC_ACTION} == "start" ] ; then
       SPECIAL_COMMAND="./avc_Create_Delete.sh | /netsim/inst/netsim_shell &"
   elif [ ${AVC_ACTION} == "stop" ] ; then
       SPECIAL_COMMAND="pkill -f avc_Create_Delete.sh"
   else
       SPECIAL_COMMAND="pgrep -f avc_Create_Delete.sh"
   fi;

   for NETSIM in ${NETSIM_LIST[*]} ; do
       touch $SPECAVCS$NETSIM
       if [ ${AVC_ACTION} == "show" ] ; then
           active_pid=`ssh netsim@${NETSIM}.athtem.eei.ericsson.se $SPECIAL_COMMAND`
           if [ "${active_pid}" == "" ] ; then
              echo "After ${AVC_ACTION} action ${NETSIM} has NO active Create_and_Delete_Operation Process "
           else
              echo "After ${AVC_ACTION} action ${NETSIM} has active Create_and_Delete_Operation Process ${active_pid} "
           fi;
       else
           echo "${AVC_ACTION} action towards Create_and_Delete_Operation on Nodes belonging to ${NETSIM}"
           ssh netsim@${NETSIM}.athtem.eei.ericsson.se $SPECIAL_COMMAND &
       fi;

   done;
}

ControlStandardPeakStormLoad()
{
   AVC_COMMAND="./avc_bursts.sh $AVC_PROFILE $AVC_ACTION | /netsim/inst/netsim_shell"

      touch $SHOWAVCS
      for NETSIM in ${NETSIM_LIST[*]} ; do
          echo "Reading/Writing Data Nodes from ${NETSIM}"
          ssh netsim@${NETSIM}.athtem.eei.ericsson.se $AVC_COMMAND >> $SHOWAVCS
          # cat  $SHOWAVCS
      done;
      touch summary2
      cat $SHOWAVCS | sed '/.*OK/d' | sed '/.*.select network/d' | sed '/.*showbursts/d' > summary2
      echo "***********************************************************************"
      INACTIVE=`grep -c 'There are no bursts defined on this NE' summary2`
      echo "Number of Inactive Sims ${INACTIVE}"
      ACTIVE=$((${#NETSIM_LIST[@]}-${INACTIVE}))
      echo "Number of Active Sims ${ACTIVE}"
      echo "***********************************************************************"

      standard_load=`cat summary2 | egrep "^[0-9]:|follow|.open"  | sed 's/The following bursts are defined on this NE\:/Standard Load/g' | sed 's/>> .open //g'`
      peak_load=`cat summary2 | egrep "^1[0-9]:|follow|.open" | sed 's/The following bursts are defined on this NE\:/Peak Load/g'     | sed 's/>> .open //g'`
      storm_load=`cat summary2 | egrep "^2[0-9]:|follow|.open" | sed 's/The following bursts are defined on this NE\:/Storm Load/g'    | sed 's/>> .open //g'`
      rm -rf summary2 $SHOWAVCS
      for i in {${standard_load},${peak_load},${storm_load}} ; do
          if [[ ${i} == *"LTE"* ]] ; then
                echo; echo -n "${i} "
          elif [ ${i} == "Load" ] ; then
                echo -n " ${i} "
          else
                echo -n $i
          fi;
       done;
}


if   [ ${AVC_PROFILE} == "special" ] ; then  
     ControlAVCSpecial
elif [ ${AVC_PROFILE} == "creates" ] ; then
     ControlCreateDeletes
else 
     ControlStandardPeakStormLoad
fi; 
echo
echo "Finished"
