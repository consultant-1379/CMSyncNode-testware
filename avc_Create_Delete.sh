#!/bin/bash

CREATECOUNT=0
DELETECOUNT=0
ACTION="createOnly"
NETSIMDIR="/netsim/netsimdir"

SIM_LIST=""

createSequence() {
       echo "createmo:parentid=\"ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1\",type=\"ExternalENodeBFunction\",name=\"A1\";" >>/netsim/createMe
       echo "createmo:parentid=\"ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=A1\",type=\"ExternalEUtranCellFDD\",name=\"A2\";" >> /netsim/createMe
       echo "createmo:parentid=\"ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=A1\",type=\"TermPointToENB\",name=\"A3\";" >> /netsim/createMe
}

deleteSequence() {
       echo "deletemo:moid=\"ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=A1,TermPointToENB=A3\";" >> /netsim/deleteMe
       echo "deletemo:moid=\"ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=A1,ExternalEUtranCellFDD=A2\";" >> /netsim/deleteMe
       echo "deletemo:moid=\"ManagedElement=1,ENodeBFunction=1,EUtraNetwork=1,ExternalENodeBFunction=A1\";" >> /netsim/deleteMe
}


shortDelay() {
    sleep 3
}

monitorShellCreate() { 
  FAIL=0
  for createJob in `jobs -p`
  do
       wait $createJob || let "FAIL+=1"
  done

  if [ "$FAIL" == "0" ];
  then
       echo "All CreateMos were successfull"
  else
       echo "All CreateMos were NOT successfull ($FAIL)"
  fi
}

monitorShellDelete() {
   FAIL=0

   for deleteJob in `jobs -p`
     do
       wait $deleteJob || let "FAIL+=1"
     done

   if [ "$FAIL" == "0" ];
   then
      echo "All Delete Mos were successfull"
   else
      echo "All Delete Mos were NOT successfull ($FAIL)"
   fi
}

performCreateSequence() { 
  touch /netsim/createMe
  echo ".open $SIM" > /netsim/createMe

  for NODENAME in {1..160}; do
       if [ "$NODENAME" -le "9" ]
       then 
           echo ".select $LTEPREFIX"00"$NODENAME" >> /netsim/createMe
       elif [ "$NODENAME" -ge "10" ] && [ "$NODENAME" -lt 100 ]
       then
           echo ".select $LTEPREFIX"0"$NODENAME" >> /netsim/createMe
       else 
           echo ".select $LTEPREFIX$NODENAME" >> /netsim/createMe
       fi
       createSequence
       CREATECOUNT=$((CREATECOUNT+3));
  done
       /netsim/inst/netsim_shell < /netsim/createMe &

  echo "Create Counter is "$CREATECOUNT
  monitorShellCreate
}

performDeleteSequence() { 

  ACTION="deleteOnly"
  echo "Starting Deletes"
  touch /netsim/deleteMe
  echo ".open $SIM" > /netsim/deleteMe

  for NODENAME in {1..160}; do
       if [ "$NODENAME" -le "9" ]
       then
           echo ".select $LTEPREFIX"00"$NODENAME" >> /netsim/deleteMe
       elif [ "$NODENAME" -ge "10" ] && [ "$NODENAME" -lt 100 ]
       then
           echo ".select $LTEPREFIX"0"$NODENAME" >> /netsim/deleteMe
       else
           echo ".select $LTEPREFIX$NODENAME" >> /netsim/deleteMe
       fi
       deleteSequence
       DELETECOUNT=$((DELETECOUNT+3));

   done
       /netsim/inst/netsim_shell < /netsim/deleteMe &

   echo "Delete Counter is" $DELETECOUNT
   monitorShellDelete
}

getSims() {
        for filename in `ls $NETSIMDIR | grep LTE | grep -v zip`; do
                SIM_LIST+=($filename)
        done
}

getSims 
secs=400   # Set interval (duration) in seconds.
endTime=$(($(date +%s)+400)) # Calculate end time.
echo " time " $endTime


while [ $(date +%s) -lt $endTime ]; do
   SIM_INDEX=0
   for SIM in ${SIM_LIST[*]}; do
      PREFIX=`echo $SIM | sed 's/.*-LTE/LTE/'`
      LTEPREFIX=${PREFIX}"ERBS00"
       performCreateSequence
       shortDelay
       performDeleteSequence
      shortDelay
      SIM_INDEX=$((SIM_INDEX+1))
  done
  echo "Total Creates " $CREATECOUNT
  echo "Total Deletes " $DELETECOUNT
  shortDelay
done
