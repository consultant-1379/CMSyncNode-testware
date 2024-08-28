#!/bin/bash

#################################################################
#   Script to generate AVCs for Atrribute Types MoRef and Enum 
#################################################################

SIM_LIST=""
AVC_ENUM="/netsim/AvcEnum"
NETSIMDIR="/netsim/netsimdir" 

FDN_ADMISSION="ManagedElement=1,ENodeBFunction=1,AdmissionControl=1" 
FDN_MOREF="ManagedElement=1,Equipment=1,AntennaUnitGroup=2,AntennaNearUnit=1" 
	
avcSequence() {
   echo "setmoattribute:mo=\"${FDN_ADMISSION}\", attributes=\"arpBasedPreEmptionState enum(ActivationVals)=1\";" >> $AVC_ENUM 
   echo "setmoattribute:mo=\"${FDN_MOREF}\", attributes=\"rfPortRef moref=11\";" >> $AVC_ENUM
   echo "setmoattribute:mo=\"${FDN_ADMISSION}\", attributes=\"arpBasedPreEmptionState enum(ActivationVals)=0\";" >> $AVC_ENUM
   echo "setmoattribute:mo=\"${FDN_MOREF}\", attributes=\"rfPortRef moref=12\";" >> $AVC_ENUM 
}

shortDelay() {
   sleep 40
}

getSims() {
        for filename in `ls $NETSIMDIR | grep LTE | grep -v zip`; do
                SIM_LIST+=($filename)
        done
}


monitorShellAVC() {
   FAIL=0
   for morefJob in `jobs -p`
   do
       wait $morefJob || let "FAIL+=1"
   done
   if [ "$FAIL" == "0" ];
   then
      echo "All AVC Mos were successfull"
   else
      echo "All AVC Mos were NOT successfull ($FAIL)"
   fi
}

performAvcSequence() {
  echo "Starting MoRef and Enum AVCs"
  touch $AVC_ENUM 
  echo ".open $SIM" > $AVC_ENUM
  LTEPREFIXANDNODENAME=""
  for NODENAME in {1..160}; do
       if [ "$NODENAME" -le "9" ]
       then
       ####   001 to 009  ####
           LTEPREFIXANDNODENAME=$LTEPREFIX"00"$NODENAME 
       elif [ "$NODENAME" -ge "10" ] && [ "$NODENAME" -lt 100 ]
       ####   010 to 099  ####
       then
           LTEPREFIXANDNODENAME=$LTEPREFIX"0"$NODENAME 
       else
       ####   100 to 160  ####
           LTEPREFIXANDNODENAME=$LTEPREFIX$NODENAME
       fi
       echo ".select $LTEPREFIXANDNODENAME" >> $AVC_ENUM
       avcSequence
       MOREFCOUNT=$((MOREFCOUNT+4));
   done
       /netsim/inst/netsim_pipe <  $AVC_ENUM &

   echo "AVC MoRef & ENumCounter is" $MOREFCOUNT
   monitorShellAVC
}

getSims
#echo "hello"
#while [ 1 ] 
secs=4000   # Set interval (duration) in seconds.

endTime=$(($(date +%s)+40000)) # Calculate end time.
echo " time " $endTime


while [ $(date +%s) -lt $endTime ]; do
   SIM_INDEX=0
   for SIM in ${SIM_LIST[*]}; do
      PREFIX=`echo $SIM | sed 's/.*-LTE/LTE/'` 
      LTEPREFIX=${PREFIX}"ERBS00"
      performAvcSequence
      shortDelay
      SIM_INDEX=$((SIM_INDEX+1))
  done 
  echo "Total Moref & Enum Avcs" $MOREFCOUNT
  shortDelay
done 
